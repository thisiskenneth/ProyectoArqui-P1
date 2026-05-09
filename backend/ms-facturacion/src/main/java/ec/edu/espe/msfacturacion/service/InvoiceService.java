package ec.edu.espe.msfacturacion.service;

import ec.edu.espe.msfacturacion.dto.OrderEvent;
import ec.edu.espe.msfacturacion.entity.Invoice;
import ec.edu.espe.msfacturacion.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;

    public void createInvoiceFromEvent(OrderEvent event) {
        log.info("Procesando evento de facturación para pedido: {}", event.getOrderId());

        // Prevención de duplicados: Verificar si ya existe una factura para este pedido
        if (!invoiceRepository.findByOrderId(event.getOrderId()).isEmpty()) {
            log.warn("La factura para el pedido {} ya existe. Omitiendo generación de duplicado.", event.getOrderId());
            return;
        }

        // 1. Tarifa base por nivel geográfico
        double baseRate = switch (event.getGeographicLevel()) {
            case "LOCAL" -> 5.0;
            case "PROVINCIAL" -> 10.0;
            case "NATIONAL" -> 20.0;
            default -> 15.0;
        };

        // 2. Recargo por peso (ej. $0.50 por kg)
        double weightSurcharge = event.getWeightKg() * 0.5;

        // 3. Ajuste por tipo de vehículo
        double vehicleAdjustment = switch (event.getVehicleType()) {
            case "HEAVY" -> 10.0;
            case "LIGHT" -> 2.0;
            default -> 5.0;
        };

        double total = baseRate + weightSurcharge + vehicleAdjustment;

        Invoice invoice = Invoice.builder()
                .invoiceNumber("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .orderId(event.getOrderId())
                .clientId(event.getCustomerEmail())
                .weightKg(event.getWeightKg())
                .geographicLevel(event.getGeographicLevel())
                .vehicleType(event.getVehicleType())
                .baseRate(baseRate)
                .weightSurcharge(weightSurcharge)
                .vehicleAdjustment(vehicleAdjustment)
                .totalAmount(total)
                .status("PENDING")
                .issuedAt(LocalDateTime.now())
                .build();

        invoiceRepository.save(invoice);
        log.info("Factura generada: {} por un total de ${}", invoice.getInvoiceNumber(), total);
    }

    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }

    public Invoice findById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
    }

    public List<Invoice> findByOrderId(String orderId) {
        return invoiceRepository.findByOrderId(orderId);
    }
}
