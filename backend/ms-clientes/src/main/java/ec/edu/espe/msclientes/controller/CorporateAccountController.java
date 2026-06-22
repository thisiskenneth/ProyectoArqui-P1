package ec.edu.espe.msclientes.controller;

import ec.edu.espe.msclientes.dto.request.CorporateAccountRequest;
import ec.edu.espe.msclientes.dto.response.CorporateAccountResponse;
import ec.edu.espe.msclientes.service.CorporateAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/corporate-accounts")
@RequiredArgsConstructor
public class CorporateAccountController {
    private final CorporateAccountService corporateAccountService;

    @GetMapping
    public ResponseEntity<List<CorporateAccountResponse>> getAll() {
        return ResponseEntity.ok(corporateAccountService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CorporateAccountResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(corporateAccountService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CorporateAccountResponse> create(@Valid @RequestBody CorporateAccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(corporateAccountService.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CorporateAccountResponse> update(@PathVariable UUID id,
                                                           @Valid @RequestBody CorporateAccountRequest request) {
        return ResponseEntity.ok(corporateAccountService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        corporateAccountService.delete(id);
        return ResponseEntity.noContent().build();
    }
}