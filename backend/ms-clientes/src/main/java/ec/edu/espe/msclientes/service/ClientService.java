package ec.edu.espe.msclientes.service;

import ec.edu.espe.msclientes.dto.request.ClientRequest;
import ec.edu.espe.msclientes.dto.response.ClientResponse;
import ec.edu.espe.msclientes.entity.Client;
import ec.edu.espe.msclientes.entity.CorporateAccount;
import ec.edu.espe.msclientes.repository.ClientRepository;
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
public class ClientService {

    private final ClientRepository clientRepository;
    private final CorporateAccountRepository corporateAccountRepository;

    public List<ClientResponse> findAll() {
        return clientRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ClientResponse findById(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + id));
        return convertToResponse(client);
    }

    @Transactional
    public ClientResponse save(ClientRequest request) {
        // Validar email único
        if (clientRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new IllegalStateException("Ya existe un cliente con el email: " + request.getEmail());
        }
        Client client = buildClient(new Client(), request);
        Client saved = clientRepository.save(client);
        log.info("Cliente creado: {} {}", saved.getFirstName(), saved.getLastName());
        return convertToResponse(saved);
    }

    @Transactional
    public ClientResponse update(UUID id, ClientRequest request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + id));

        // Si cambia el email, validar unicidad
        if (!client.getEmail().equalsIgnoreCase(request.getEmail())
                && clientRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new IllegalStateException("Ya existe un cliente con el email: " + request.getEmail());
        }

        buildClient(client, request);
        log.info("Cliente actualizado: {}", id);
        return convertToResponse(clientRepository.save(client));
    }

    @Transactional
    public void delete(UUID id) {
        if (!clientRepository.existsById(id)) {
            throw new EntityNotFoundException("Cliente no encontrado con id: " + id);
        }
        clientRepository.deleteById(id);
        log.info("Cliente eliminado: {}", id);
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private Client buildClient(Client client, ClientRequest request) {
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setEmail(request.getEmail().toLowerCase());
        client.setPhone(request.getPhone());
        client.setAddress(request.getAddress());

        if (request.getCorporateAccountId() != null && !request.getCorporateAccountId().isBlank()) {
            UUID corpId = UUID.fromString(request.getCorporateAccountId());
            CorporateAccount ca = corporateAccountRepository.findById(corpId)
                    .orElseThrow(() -> new EntityNotFoundException(
                        "Cuenta corporativa no encontrada con id: " + request.getCorporateAccountId()));
            client.setCorporateAccount(ca);
        } else {
            client.setCorporateAccount(null);
        }
        return client;
    }

    private ClientResponse convertToResponse(Client client) {
        ClientResponse response = new ClientResponse();
        response.setId(client.getId());
        response.setFirstName(client.getFirstName());
        response.setLastName(client.getLastName());
        response.setEmail(client.getEmail());
        response.setPhone(client.getPhone());
        response.setAddress(client.getAddress());
        response.setCreatedAt(client.getCreatedAt());
        response.setUpdatedAt(client.getUpdatedAt());
        if (client.getCorporateAccount() != null) {
            response.setCorporateAccountId(client.getCorporateAccount().getId());
        }
        return response;
    }
}
