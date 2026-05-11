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

@RestController
@RequestMapping("/api/corporate-accounts")
@RequiredArgsConstructor
public class CorporateAccountController {
    private final CorporateAccountService corporateAccountService;

    @GetMapping
    public ResponseEntity<List<CorporateAccountResponse>> getAll() {
        return ResponseEntity.ok(corporateAccountService.findAll());
    }

    @PostMapping
    public ResponseEntity<CorporateAccountResponse> create(@Valid @RequestBody CorporateAccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(corporateAccountService.save(request));
    }
}
