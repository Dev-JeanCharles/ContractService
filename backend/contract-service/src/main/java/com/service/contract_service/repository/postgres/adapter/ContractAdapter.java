package com.service.contract_service.repository.postgres.adapter;

import com.service.contract_service.domain.model.Contract;
import org.springframework.stereotype.Component;

@Component
public class ContractAdapter {

    public ContractDAO convertToContractDAO(Contract contract) {
        ContractDAO contractDAO = new ContractDAO();

        contractDAO.setId((contract.getId()));
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

    public Contract convertToContract(ContractDAO contractDAO) {
        Contract contract = new Contract();

        contract.setId((contractDAO.getId()));
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
