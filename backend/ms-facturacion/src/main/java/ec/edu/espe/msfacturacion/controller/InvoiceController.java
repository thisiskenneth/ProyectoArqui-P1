package ec.edu.espe.msfacturacion.controller;

import ec.edu.espe.msfacturacion.entity.Invoice;
import ec.edu.espe.msfacturacion.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<List<Invoice>> getAll() {
        return ResponseEntity.ok(invoiceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getById(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.findById(id));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Invoice>> getByOrderId(@PathVariable String orderId) {
        return ResponseEntity.ok(invoiceService.findByOrderId(orderId));
    }
}
