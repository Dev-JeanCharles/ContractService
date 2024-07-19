package com.service.contract_service.service.imp;

import com.service.contract_service.application.web.controllers.builder.ContractBuilder;
import com.service.contract_service.application.web.controllers.dto.responses.ContractResponse;
import com.service.contract_service.domain.commons.DatabaseConnectionException;
import com.service.contract_service.domain.model.Contract;
import com.service.contract_service.repository.interfaces.ContractRepository;
import com.service.contract_service.repository.postgres.dao.ContractDAO;
import com.service.contract_service.service.interfaces.ContractService;
import jakarta.validation.Valid;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractServiceImp implements ContractService {

    private static final Logger logger = LoggerFactory.getLogger(ContractServiceImp.class);

    private final ContractRepository repository;
    private final ContractBuilder contractBuilder;


    @Autowired
    public ContractServiceImp(ContractRepository repository, ContractBuilder contractBuilder) {
        this.repository = repository;
        this.contractBuilder = contractBuilder;
    }

    @Override
    public ContractResponse create(@Valid Contract contract) {
        logger.info("[CREATE-CONTRACT]-[Service] Creating a new contract with personId: {}", contract.getPersonId());

        try {
            List<ContractDAO> existingContracts = repository.findByPersonIdAndProductId(contract.getPersonId(), contract.getProductId());
            if (!existingContracts.isEmpty()) {
                throw new DataIntegrityViolationException("Contract with the same person_id and product_id already exists.");
            }

            ContractDAO contractDAO = convertToContractDAO(contract);
            logger.debug("[CREATE-CONTRACT]-[Service] ContractDAO created: {}", contractDAO);

            ContractDAO savedContractDAO = repository.save(contractDAO);
            logger.info("[CREATE-CONTRACT]-[Service] Contract saved with id: {}", savedContractDAO.getId());

            Contract savedContract = convertToContract(savedContractDAO);
            logger.debug("[CREATE-CONTRACT]-[Service] Converted saved ContractDAO to Contract: {}", savedContract);

            return contractBuilder.toContractResponse(savedContract);
        } catch (CannotGetJdbcConnectionException e) {
            logger.error("[CREATE-CONTRACT]-[Service] Database connection error: ", e);
            throw new DatabaseConnectionException("Database connection error: " + e.getMessage());
        } catch (DataAccessResourceFailureException e) {
            logger.error("[CREATE-CONTRACT]-[Service] Data access resource failure: ", e);
            throw new DatabaseConnectionException("Data access resource failure: " + e.getMessage());
        } catch (ConstraintViolationException e) {
            logger.error("[CREATE-CONTRACT]-[Service] ConstraintViolationException: ", e);
            throw e;
        } catch (DataIntegrityViolationException e) {
            logger.error("[CREATE-CONTRACT]-[Service] DataIntegrityViolationException: ", e);
            throw e;
        }
    }

    private ContractDAO convertToContractDAO(Contract contract) {
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

    private Contract convertToContract(ContractDAO contractDAO) {
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

