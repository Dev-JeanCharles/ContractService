package com.service.contract_service.repository.postgres.adapter;

import com.service.contract_service.domain.enums.ContractStatusEnum;
import com.service.contract_service.domain.model.Contract;
import com.service.contract_service.infrastructure.gateways.person.dto.PersonResponseData;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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

    public ContractDAO contractDocumentByPerson(ContractDAO contractDAO, PersonResponseData person) {
        contractDAO.setIntegrationPersonPending(true);
        contractDAO.setStatus(ContractStatusEnum.ACTIVE);

        String fullName = (person.getFirstName() != null ? person.getFirstName() : "") +
                (person.getLastName() != null ? " " + person.getLastName() : "");
        contractDAO.setFullNamePerson(fullName.trim()); // Ensure no leading/trailing spaces

        contractDAO.setGenderPerson(person.getGender() != null ? person.getGender() : "Unknown");

        contractDAO.setCpfPerson(person.getCpf() != null ? person.getCpf() : "Not provided");

        String birthdayAt = (person.getBirthdayAt() != null) ? person.getBirthdayAt().toString() : "Not provided";
        contractDAO.setBirthdayAtPerson(birthdayAt);

        contractDAO.setNameProduct(null);
        contractDAO.setDescriptionProduct(null);
        contractDAO.setQuantityProduct(null);
        contractDAO.setOriginProduct(null);

        contractDAO.setUpdatedAt(LocalDateTime.now());

        return contractDAO;
    }
}
