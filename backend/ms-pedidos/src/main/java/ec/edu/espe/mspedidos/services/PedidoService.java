package ec.edu.espe.mspedidos.services;

import ec.edu.espe.mspedidos.dto.request.CambiarEstadoRequest;
import ec.edu.espe.mspedidos.dto.request.PedidoCreateRequest;
import ec.edu.espe.mspedidos.dto.response.HistorialEstadoResponse;
import ec.edu.espe.mspedidos.dto.response.PedidoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface PedidoService {
    PedidoResponse crearPedido(PedidoCreateRequest request);
    PedidoResponse obtenerPedidoPorId(UUID id);
    Page<PedidoResponse> listarPedidos(Pageable pageable);
    Page<PedidoResponse> listarPedidosPorCliente(UUID clienteId, Pageable pageable);
    PedidoResponse cambiarEstado(UUID id, CambiarEstadoRequest request);
    List<HistorialEstadoResponse> obtenerHistorial(UUID id);
}
