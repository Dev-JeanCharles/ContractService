package service.imp;

import application.web.controllers.builder.Builders;
import application.web.controllers.dto.responses.ContractResponse;
import domain.model.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.postgres.ContractRepository;
import repository.postgres.dao.ContractDAO;
import service.ContractService;

@Service
public class ContractServiceImp implements ContractService {

    @Autowired
    private ContractRepository repository;

    private final Builders builders = new Builders();

    @Override
    public ContractResponse create(Contract contract) {

        ContractDAO contractDAO = new ContractDAO();
        Contract contracts = new Contract();
        ContractDAO contractSaved = repository.save(assembleObject(contract, contractDAO));

        return builders.toContractResponse(assembleContract(contracts, contractSaved));
    }

    private ContractDAO assembleObject(Contract contract, ContractDAO contractDAO) {

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

    private Contract assembleContract(Contract contract, ContractDAO contractDAO) {

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

