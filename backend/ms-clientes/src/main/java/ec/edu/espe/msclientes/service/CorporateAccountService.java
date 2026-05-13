package ec.edu.espe.msclientes.service;

import ec.edu.espe.msclientes.dto.request.CorporateAccountRequest;
import ec.edu.espe.msclientes.dto.response.CorporateAccountResponse;
import ec.edu.espe.msclientes.entity.CorporateAccount;
import ec.edu.espe.msclientes.repository.CorporateAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CorporateAccountService {

    private final CorporateAccountRepository corporateAccountRepository;

    public List<CorporateAccountResponse> findAll() {
        return corporateAccountRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public CorporateAccountResponse findById(UUID id) {
        return convertToResponse(corporateAccountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta corporativa no encontrada con id: " + id)));
    }

    @Transactional
    public CorporateAccountResponse save(CorporateAccountRequest request) {
        if (corporateAccountRepository.existsByRuc(request.getRuc())) {
            throw new IllegalStateException("Ya existe una cuenta corporativa con el RUC: " + request.getRuc());
        }
        CorporateAccount saved = corporateAccountRepository.save(convertToEntity(request));
        log.info("Cuenta corporativa creada: {} - {}", saved.getRuc(), saved.getBusinessName());
        return convertToResponse(saved);
    }

    @Transactional
    public CorporateAccountResponse update(UUID id, CorporateAccountRequest request) {
        CorporateAccount ca = corporateAccountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta corporativa no encontrada con id: " + id));

        if (!ca.getRuc().equals(request.getRuc()) && corporateAccountRepository.existsByRuc(request.getRuc())) {
            throw new IllegalStateException("Ya existe una cuenta corporativa con el RUC: " + request.getRuc());
        }

        ca.setRuc(request.getRuc());
        ca.setBusinessName(request.getBusinessName());
        ca.setCreditLimit(request.getCreditLimit());
        ca.setIndustry(request.getIndustry());
        log.info("Cuenta corporativa actualizada: {}", id);
        return convertToResponse(corporateAccountRepository.save(ca));
    }

    @Transactional
    public void delete(UUID id) {
        if (!corporateAccountRepository.existsById(id)) {
            throw new EntityNotFoundException("Cuenta corporativa no encontrada con id: " + id);
        }
        corporateAccountRepository.deleteById(id);
        log.info("Cuenta corporativa eliminada: {}", id);
    }

    private CorporateAccountResponse convertToResponse(CorporateAccount entity) {
        CorporateAccountResponse response = new CorporateAccountResponse();
        response.setId(entity.getId());
        response.setRuc(entity.getRuc());
        response.setBusinessName(entity.getBusinessName());
        response.setCreditLimit(entity.getCreditLimit());
        response.setIndustry(entity.getIndustry());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }

    private CorporateAccount convertToEntity(CorporateAccountRequest request) {
        return CorporateAccount.builder()
                .ruc(request.getRuc())
                .businessName(request.getBusinessName())
                .creditLimit(request.getCreditLimit())
                .industry(request.getIndustry())
                .build();
    }
}
