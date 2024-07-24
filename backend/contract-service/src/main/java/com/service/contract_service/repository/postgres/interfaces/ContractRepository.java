package com.service.contract_service.repository.postgres.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.service.contract_service.repository.postgres.adapter.ContractDAO;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<ContractDAO, Long> {

    List<ContractDAO> findByPersonIdAndProductId(String personId, String productId);
    List<ContractDAO> findById(String id);
}
