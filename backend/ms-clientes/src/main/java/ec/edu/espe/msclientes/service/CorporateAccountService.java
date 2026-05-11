package ec.edu.espe.msclientes.service;

import ec.edu.espe.msclientes.dto.request.CorporateAccountRequest;
import ec.edu.espe.msclientes.dto.response.CorporateAccountResponse;
import ec.edu.espe.msclientes.entity.CorporateAccount;
import ec.edu.espe.msclientes.repository.CorporateAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CorporateAccountService {
    private final CorporateAccountRepository corporateAccountRepository;

    public List<CorporateAccountResponse> findAll() {
        return corporateAccountRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public CorporateAccountResponse save(CorporateAccountRequest request) {
        CorporateAccount ca = convertToEntity(request);
        return convertToResponse(corporateAccountRepository.save(ca));
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
        return CorporateAccount.builder()
                .ruc(request.getRuc())
                .businessName(request.getBusinessName())
                .creditLimit(request.getCreditLimit())
                .industry(request.getIndustry())
                .build();
    }
}
