package ec.edu.espe.msauth.services.impl;

import ec.edu.espe.msauth.dto.request.RoleRequest;
import ec.edu.espe.msauth.dto.response.RoleResponse;
import ec.edu.espe.msauth.entity.RoleEntity;
import ec.edu.espe.msauth.repository.RoleRepository;
import ec.edu.espe.msauth.repository.UserRoleRepository;
import ec.edu.espe.msauth.services.RoleService;
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
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public List<RoleResponse> findAll() {
        return roleRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RoleResponse findById(UUID id) {
        return toResponse(roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con id: " + id)));
    }

    @Override
    public RoleResponse findByName(String name) {
        return toResponse(roleRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con nombre: " + name)));
    }

    @Override
    @Transactional
    public RoleResponse create(RoleRequest request) {
        if (roleRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalStateException("Ya existe un rol con el nombre: " + request.getName());
        }
        RoleEntity role = RoleEntity.builder()
                .name(request.getName().toUpperCase())
                .description(request.getDescription())
                .build();
        RoleEntity saved = roleRepository.save(role);
        log.info("Rol creado: {}", saved.getName());
        return toResponse(saved);
    }

    @Override
    @Transactional
    public RoleResponse update(UUID id, RoleRequest request) {
        RoleEntity role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con id: " + id));

        if (!role.getName().equalsIgnoreCase(request.getName())
                && roleRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalStateException("Ya existe un rol con el nombre: " + request.getName());
        }

        role.setName(request.getName().toUpperCase());
        role.setDescription(request.getDescription());
        log.info("Rol actualizado: {}", role.getName());
        return toResponse(roleRepository.save(role));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        RoleEntity role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con id: " + id));

        // Regla de negocio: no eliminar rol si tiene usuarios asignados
        if (!userRoleRepository.findByUserId(id).isEmpty()) {
            throw new IllegalStateException(
                "No se puede eliminar el rol '" + role.getName() + "' porque tiene usuarios asignados.");
        }
        roleRepository.delete(role);
        log.info("Rol eliminado: {}", role.getName());
    }

    private RoleResponse toResponse(RoleEntity r) {
        return RoleResponse.builder()
                .id(r.getId())
                .name(r.getName())
                .description(r.getDescription())
                .createdAt(r.getCreatedAt())
                .build();
    }
}
