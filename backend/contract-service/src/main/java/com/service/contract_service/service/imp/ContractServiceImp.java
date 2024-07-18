package com.service.contract_service.service.imp;

import com.service.contract_service.application.web.controllers.builder.ContractBuilder;
import com.service.contract_service.application.web.controllers.dto.responses.ContractResponse;
import com.service.contract_service.domain.model.Contract;
import com.service.contract_service.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.service.contract_service.repository.postgres.ContractRepository;
import com.service.contract_service.repository.postgres.dao.ContractDAO;

@Service
public class ContractServiceImp implements ContractService {

    private final ContractRepository repository;
    private final ContractBuilder contractBuilder;

    @Autowired
    public ContractServiceImp(ContractRepository repository, ContractBuilder contractBuilder) {
        this.repository = repository;
        this.contractBuilder = contractBuilder;
    }

    @Override
    public ContractResponse create(Contract contract) {

        ContractDAO contractDAO = convertToContractDAO(contract);
        ContractDAO savedContractDAO = repository.save(contractDAO);

        Contract savedContract = convertToContract(savedContractDAO);

        return contractBuilder.toContractResponse(savedContract);
    }

    private ContractDAO convertToContractDAO(Contract contract) {
        ContractDAO contractDAO = new ContractDAO();

        contractDAO.setId(contract.getId());
        contractDAO.setPersonId(contract.getPersonId());
        contractDAO.setProductId(contract.getProductId());
        contractDAO.setStatus(contract.getStatus());
        contractDAO.setIntegrationPersonPending(contract.getIntegrationPersonPending());
        contractDAO.setIntegrationProductPending(contract.getIntegrationProductPending());
        contractDAO.setCreatedAt(contract.getCreatedAt());
        contractDAO.setUpdatedAt(contract.getUpdatedAt());
        contractDAO.setCancelamentDat(contract.getCancelamentDat());

        return contractDAO;

    }

    private Contract convertToContract(ContractDAO contractDAO) {
        Contract contract = new Contract();

        contract.setId(contractDAO.getId());
        contract.setPersonId(contractDAO.getPersonId());
        contract.setProductId(contractDAO.getProductId());
        contract.setStatus(contractDAO.getStatus());
        contract.setIntegrationPersonPending(contractDAO.getIntegrationPersonPending());
        contract.setIntegrationProductPending(contractDAO.getIntegrationProductPending());
        contract.setCreatedAt(contractDAO.getCreatedAt());
        contract.setUpdatedAt(contractDAO.getUpdatedAt());
        contract.setCancelamentDat(contractDAO.getCancelamentDat());

        return contract;

    }
}

