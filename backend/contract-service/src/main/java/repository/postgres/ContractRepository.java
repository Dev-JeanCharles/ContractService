package repository.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import repository.postgres.dao.ContractDAO;

import java.util.UUID;

@Repository
public interface ContractRepository extends JpaRepository<ContractDAO, UUID> {
}
