package ec.edu.espe.msclientes.repository;

import ec.edu.espe.msclientes.entity.CorporateAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CorporateAccountRepository extends JpaRepository<CorporateAccount, UUID> {
    Optional<CorporateAccount> findByRuc(String ruc);
    boolean existsByRuc(String ruc);
}
