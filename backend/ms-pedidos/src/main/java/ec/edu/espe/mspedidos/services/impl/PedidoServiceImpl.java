package ec.edu.espe.mspedidos.services.impl;

import ec.edu.espe.mspedidos.dto.request.CambiarEstadoRequest;
import ec.edu.espe.mspedidos.dto.request.PedidoCreateRequest;
import ec.edu.espe.mspedidos.dto.response.HistorialEstadoResponse;
import ec.edu.espe.mspedidos.dto.response.PaqueteResponse;
import ec.edu.espe.mspedidos.dto.response.PedidoResponse;
import ec.edu.espe.mspedidos.entity.HistorialEstado;
import ec.edu.espe.mspedidos.entity.Paquete;
import ec.edu.espe.mspedidos.entity.Pedido;
import ec.edu.espe.mspedidos.enums.EstadoPedido;
import ec.edu.espe.mspedidos.enums.Prioridad;
import ec.edu.espe.mspedidos.repository.HistorialEstadoRepository;
import ec.edu.espe.mspedidos.repository.PedidoRepository;
import ec.edu.espe.mspedidos.services.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final HistorialEstadoRepository historialEstadoRepository;

    @Override
    @Transactional
    public PedidoResponse crearPedido(PedidoCreateRequest request) {
        Pedido pedido = Pedido.builder()
                .clienteId(request.getClienteId())
                .origen(request.getOrigen())
                .destino(request.getDestino())
                .peso(request.getPeso())
                .prioridad(request.getPrioridad() != null ? request.getPrioridad() : Prioridad.NORMAL)
                .estado(EstadoPedido.CREADO)
                .notas(request.getNotas())
                .build();

        request.getPaquetes().forEach(paqueteReq -> {
            Paquete paquete = Paquete.builder()
                    .descripcion(paqueteReq.getDescripcion())
                    .peso(paqueteReq.getPeso())
                    .dimensiones(paqueteReq.getDimensiones())
                    .fragil(paqueteReq.getFragil() != null ? paqueteReq.getFragil() : false)
                    .build();
            pedido.addPaquete(paquete);
        });

        HistorialEstado historial = HistorialEstado.builder()
                .estadoAnterior(EstadoPedido.CREADO)
                .estadoNuevo(EstadoPedido.CREADO)
                .observaciones("Pedido creado")
                .build();
        pedido.addHistorialEstado(historial);

        Pedido savedPedido = pedidoRepository.save(pedido);
        return mapToPedidoResponse(savedPedido);
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoResponse obtenerPedidoPorId(UUID id) {
        Pedido pedido = findPedidoOrThrow(id);
        return mapToPedidoResponse(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PedidoResponse> listarPedidos(Pageable pageable) {
        return pedidoRepository.findAll(pageable).map(this::mapToPedidoResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PedidoResponse> listarPedidosPorCliente(UUID clienteId, Pageable pageable) {
        return pedidoRepository.findByClienteId(clienteId, pageable).map(this::mapToPedidoResponse);
    }

    @Override
    @Transactional
    public PedidoResponse cambiarEstado(UUID id, CambiarEstadoRequest request) {
        Pedido pedido = findPedidoOrThrow(id);

        EstadoPedido estadoActual = pedido.getEstado();
        EstadoPedido estadoNuevo = request.getEstadoNuevo();

        if (estadoActual == EstadoPedido.ENTREGADO || estadoActual == EstadoPedido.CANCELADO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede modificar un pedido que ya está " + estadoActual);
        }

        if (!isValidTransition(estadoActual, estadoNuevo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transición de estado no válida de " + estadoActual + " a " + estadoNuevo);
        }

        pedido.setEstado(estadoNuevo);

        HistorialEstado historial = HistorialEstado.builder()
                .estadoAnterior(estadoActual)
                .estadoNuevo(estadoNuevo)
                .usuarioId(request.getUsuarioId())
                .observaciones(request.getObservaciones())
                .build();
        pedido.addHistorialEstado(historial);

        Pedido updatedPedido = pedidoRepository.save(pedido);
        return mapToPedidoResponse(updatedPedido);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialEstadoResponse> obtenerHistorial(UUID id) {
        if (!pedidoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado");
        }
        return historialEstadoRepository.findByPedidoIdOrderByFechaDesc(id).stream()
                .map(this::mapToHistorialResponse)
                .collect(Collectors.toList());
    }

    private Pedido findPedidoOrThrow(UUID id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado con ID: " + id));
    }

    private boolean isValidTransition(EstadoPedido actual, EstadoPedido nuevo) {
        if (nuevo == EstadoPedido.CANCELADO) {
            return true; // Asumiendo que se puede cancelar desde CREADO, ASIGNADO, EN_RUTA
        }
        return switch (actual) {
            case CREADO -> nuevo == EstadoPedido.ASIGNADO;
            case ASIGNADO -> nuevo == EstadoPedido.EN_RUTA;
            case EN_RUTA -> nuevo == EstadoPedido.ENTREGADO;
            default -> false;
        };
    }

    private PedidoResponse mapToPedidoResponse(Pedido pedido) {
        List<PaqueteResponse> paquetes = pedido.getPaquetes().stream()
                .map(p -> PaqueteResponse.builder()
                        .id(p.getId())
                        .descripcion(p.getDescripcion())
                        .peso(p.getPeso())
                        .dimensiones(p.getDimensiones())
                        .fragil(p.getFragil())
                        .createdAt(p.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return PedidoResponse.builder()
                .id(pedido.getId())
                .clienteId(pedido.getClienteId())
                .origen(pedido.getOrigen())
                .destino(pedido.getDestino())
                .peso(pedido.getPeso())
                .estado(pedido.getEstado())
                .prioridad(pedido.getPrioridad())
                .notas(pedido.getNotas())
                .createdAt(pedido.getCreatedAt())
                .updatedAt(pedido.getUpdatedAt())
                .paquetes(paquetes)
                .build();
    }

    private HistorialEstadoResponse mapToHistorialResponse(HistorialEstado historial) {
        return HistorialEstadoResponse.builder()
                .id(historial.getId())
                .estadoAnterior(historial.getEstadoAnterior())
                .estadoNuevo(historial.getEstadoNuevo())
                .usuarioId(historial.getUsuarioId())
                .observaciones(historial.getObservaciones())
                .fecha(historial.getFecha())
                .build();
    }
}
