package ec.edu.espe.msclientes.service;

import ec.edu.espe.msclientes.dto.CorporateAccountDto;
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

    public List<CorporateAccountDto> findAll() {
        return corporateAccountRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public CorporateAccountDto save(CorporateAccountDto dto) {
        CorporateAccount ca = convertToEntity(dto);
        return convertToDto(corporateAccountRepository.save(ca));
    }

    private CorporateAccountDto convertToDto(CorporateAccount entity) {
        CorporateAccountDto dto = new CorporateAccountDto();
        dto.setId(entity.getId());
        dto.setRuc(entity.getRuc());
        dto.setBusinessName(entity.getBusinessName());
        dto.setCreditLimit(entity.getCreditLimit());
        dto.setIndustry(entity.getIndustry());
        return dto;
    }

    private CorporateAccount convertToEntity(CorporateAccountDto dto) {
        return CorporateAccount.builder()
                .ruc(dto.getRuc())
                .businessName(dto.getBusinessName())
                .creditLimit(dto.getCreditLimit())
                .industry(dto.getIndustry())
                .build();
    }
}
