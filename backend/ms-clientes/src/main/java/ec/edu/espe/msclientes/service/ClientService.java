package ec.edu.espe.msclientes.service;

import ec.edu.espe.msclientes.dto.event.ClienteCreatedEvent;
import ec.edu.espe.msclientes.dto.request.ClientRequest;
import ec.edu.espe.msclientes.dto.response.ClientResponse;
import ec.edu.espe.msclientes.entity.Client;
import ec.edu.espe.msclientes.entity.CorporateAccount;
import ec.edu.espe.msclientes.publisher.ClientePublisher;
import ec.edu.espe.msclientes.repository.ClientRepository;
import ec.edu.espe.msclientes.repository.CorporateAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final CorporateAccountRepository corporateAccountRepository;
    private final ClientePublisher clientePublisher;

    public List<ClientResponse> findAll() {
        return clientRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ClientResponse findById(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        return convertToResponse(client);
    }

    public ClientResponse save(ClientRequest request) {
        Client client = convertToEntity(request);
        Client saved = clientRepository.save(client);

        // Publica el evento de dominio. ms-auth lo consume y, si viene password,
        // provisiona el usuario de acceso (rol CLIENT) con el email como username.
        clientePublisher.publishClienteCreado(ClienteCreatedEvent.builder()
                .clientId(saved.getId())
                .email(saved.getEmail())
                .firstName(saved.getFirstName())
                .lastName(saved.getLastName())
                .password(request.getPassword())
                .build());

        return convertToResponse(saved);
    }

    public ClientResponse update(UUID id, ClientRequest request) {
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

    public void delete(UUID id) {
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
