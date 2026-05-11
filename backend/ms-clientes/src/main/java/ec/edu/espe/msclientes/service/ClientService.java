package ec.edu.espe.msclientes.service;

import ec.edu.espe.msclientes.dto.request.ClientRequest;
import ec.edu.espe.msclientes.dto.response.ClientResponse;
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

    public List<ClientResponse> findAll() {
        return clientRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ClientResponse findById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        return convertToResponse(client);
    }

    public ClientResponse save(ClientRequest request) {
        Client client = convertToEntity(request);
        return convertToResponse(clientRepository.save(client));
    }

    public ClientResponse update(Long id, ClientRequest request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());
        client.setAddress(request.getAddress());

        if (request.getCorporateAccountId() != null) {
            CorporateAccount ca = corporateAccountRepository.findById(request.getCorporateAccountId())
                    .orElseThrow(() -> new RuntimeException("Corporate Account not found"));
            client.setCorporateAccount(ca);
        } else {
            client.setCorporateAccount(null);
        }

        return convertToResponse(clientRepository.save(client));
    }

    public void delete(Long id) {
        clientRepository.deleteById(id);
    }

    private ClientResponse convertToResponse(Client client) {
        ClientResponse response = new ClientResponse();
        response.setId(client.getId());
        response.setFirstName(client.getFirstName());
        response.setLastName(client.getLastName());
        response.setEmail(client.getEmail());
        response.setPhone(client.getPhone());
        response.setAddress(client.getAddress());
        if (client.getCorporateAccount() != null) {
            response.setCorporateAccountId(client.getCorporateAccount().getId());
        }
        return response;
    }

    private Client convertToEntity(ClientRequest request) {
        Client client = Client.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();
        
        if (request.getCorporateAccountId() != null) {
            CorporateAccount ca = corporateAccountRepository.findById(request.getCorporateAccountId())
                    .orElseThrow(() -> new RuntimeException("Corporate Account not found"));
            client.setCorporateAccount(ca);
        }
        return client;
    }
}
