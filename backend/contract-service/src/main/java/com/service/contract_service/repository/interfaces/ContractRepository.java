package com.service.contract_service.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.service.contract_service.repository.postgres.dao.ContractDAO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContractRepository extends JpaRepository<ContractDAO, Long> {

    List<ContractDAO> findByPersonIdAndProductId(String personId, String productId);
}
