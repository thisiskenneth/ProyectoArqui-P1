package ec.edu.espe.msauth.services;

import ec.edu.espe.msauth.dto.request.RoleRequest;
import ec.edu.espe.msauth.dto.response.RoleResponse;

import java.util.List;
import java.util.UUID;

public interface RoleService {
    List<RoleResponse> findAll();
    RoleResponse findById(UUID id);
    RoleResponse findByName(String name);
    RoleResponse create(RoleRequest request);
    RoleResponse update(UUID id, RoleRequest request);
    void delete(UUID id);
}
