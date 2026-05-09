package ec.edu.espe.msclientes.service;

import ec.edu.espe.msclientes.dto.ClientDto;
import ec.edu.espe.msclientes.entity.Client;
import ec.edu.espe.msclientes.entity.CorporateAccount;
import ec.edu.espe.msclientes.repository.ClientRepository;
import ec.edu.espe.msclientes.repository.CorporateAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final CorporateAccountRepository corporateAccountRepository;

    public List<ClientDto> findAll() {
        return clientRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ClientDto findById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        return convertToDto(client);
    }

    public ClientDto save(ClientDto dto) {
        Client client = convertToEntity(dto);
        return convertToDto(clientRepository.save(client));
    }

    public ClientDto update(Long id, ClientDto dto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        
        client.setFirstName(dto.getFirstName());
        client.setLastName(dto.getLastName());
        client.setEmail(dto.getEmail());
        client.setPhone(dto.getPhone());
        client.setAddress(dto.getAddress());

        if (dto.getCorporateAccountId() != null) {
            CorporateAccount ca = corporateAccountRepository.findById(dto.getCorporateAccountId())
                    .orElseThrow(() -> new RuntimeException("Corporate Account not found"));
            client.setCorporateAccount(ca);
        } else {
            client.setCorporateAccount(null);
        }

        return convertToDto(clientRepository.save(client));
    }

    public void delete(Long id) {
        clientRepository.deleteById(id);
    }

    private ClientDto convertToDto(Client client) {
        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        dto.setFirstName(client.getFirstName());
        dto.setLastName(client.getLastName());
        dto.setEmail(client.getEmail());
        dto.setPhone(client.getPhone());
        dto.setAddress(client.getAddress());
        if (client.getCorporateAccount() != null) {
            dto.setCorporateAccountId(client.getCorporateAccount().getId());
        }
        return dto;
    }

    private Client convertToEntity(ClientDto dto) {
        Client client = Client.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .build();
        
        if (dto.getCorporateAccountId() != null) {
            CorporateAccount ca = corporateAccountRepository.findById(dto.getCorporateAccountId())
                    .orElseThrow(() -> new RuntimeException("Corporate Account not found"));
            client.setCorporateAccount(ca);
        }
        return client;
    }
}
