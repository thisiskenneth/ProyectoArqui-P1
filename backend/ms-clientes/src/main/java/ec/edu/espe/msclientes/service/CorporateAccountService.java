package ec.edu.espe.msclientes.service;

import ec.edu.espe.msclientes.dto.request.CorporateAccountRequest;
import ec.edu.espe.msclientes.dto.response.CorporateAccountResponse;
import ec.edu.espe.msclientes.entity.CorporateAccount;
import ec.edu.espe.msclientes.repository.CorporateAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CorporateAccountService {
    private final CorporateAccountRepository corporateAccountRepository;

    public List<CorporateAccountResponse> findAll() {
        return corporateAccountRepository.findAll().stream().map(this::convertToResponse).toList();
    }

    public CorporateAccountResponse findById(UUID id) {
        return corporateAccountRepository.findById(id).map(this::convertToResponse)
                .orElseThrow(() -> new RuntimeException("Corporate account not found with id: " + id));
    }

    public CorporateAccountResponse save(CorporateAccountRequest request) {
        return convertToResponse(corporateAccountRepository.save(convertToEntity(request)));
    }

    public CorporateAccountResponse update(UUID id, CorporateAccountRequest request) {
        CorporateAccount account = corporateAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Corporate account not found with id: " + id));
        account.setRuc(request.getRuc());
        account.setBusinessName(request.getBusinessName());
        account.setCreditLimit(request.getCreditLimit());
        account.setIndustry(request.getIndustry());
        return convertToResponse(corporateAccountRepository.save(account));
    }

    public void delete(UUID id) {
        if (!corporateAccountRepository.existsById(id)) {
            throw new RuntimeException("Corporate account not found with id: " + id);
        }
        corporateAccountRepository.deleteById(id);
    }

    private CorporateAccountResponse convertToResponse(CorporateAccount entity) {
        CorporateAccountResponse response = new CorporateAccountResponse();
        response.setId(entity.getId());
        response.setRuc(entity.getRuc());
        response.setBusinessName(entity.getBusinessName());
        response.setCreditLimit(entity.getCreditLimit());
        response.setIndustry(entity.getIndustry());
        return response;
    }

    private CorporateAccount convertToEntity(CorporateAccountRequest request) {
        return CorporateAccount.builder().ruc(request.getRuc()).businessName(request.getBusinessName())
                .creditLimit(request.getCreditLimit()).industry(request.getIndustry()).build();
    }
}