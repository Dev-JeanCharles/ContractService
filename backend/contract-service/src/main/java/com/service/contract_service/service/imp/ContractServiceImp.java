package com.service.contract_service.service.imp;

import com.service.contract_service.application.web.controllers.builder.ContractBuilder;
import com.service.contract_service.application.web.controllers.dto.responses.ContractResponse;
import com.service.contract_service.domain.enums.ContractStatusEnum;
import com.service.contract_service.domain.model.Contract;
import com.service.contract_service.infrastructure.gateways.imp.PersonServiceImp;
import com.service.contract_service.infrastructure.gateways.person.dto.PersonResponseData;
import com.service.contract_service.repository.postgres.adapter.ContractAdapter;
import com.service.contract_service.repository.postgres.interfaces.ContractRepository;
import com.service.contract_service.repository.postgres.adapter.ContractDAO;
import com.service.contract_service.service.interfaces.ContractService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class ContractServiceImp implements ContractService {


    private final ContractRepository repository;
    private final ContractBuilder contractBuilder;
    private final PersonServiceImp personService;
    private final ContractAdapter adapter;


    @Autowired
    public ContractServiceImp(ContractRepository repository, ContractBuilder contractBuilder, PersonServiceImp personService, ContractAdapter adapter) {
        this.repository = repository;
        this.contractBuilder = contractBuilder;
        this.personService = personService;
        this.adapter = adapter;
    }

    @Override
    public ContractResponse create(@Valid Contract contract) {
        log.info("[CREATE-CONTRACT]-[Service] Creating a new contract with personId: {}", contract.getPersonId());

        try {
            verifyExistingContractsEquals(contract);
            PersonResponseData person = personService.getPersonById(contract.getPersonId());
            log.info("[CREATE-CONTRACT]-[Gateway] Successfully completed integration with service-person, data: {}", person);

            ContractDAO contractDAO = adapter.convertToContractDAO(contract);
            ContractDAO savedContractDAO = repository.save(contractDAO);
            log.info("[CREATE-CONTRACT]-[Postgres] Contract saved in the database with id: {} and status: {}", savedContractDAO.getId(), savedContractDAO.getStatus());

            ContractDAO updatedContract = contractDocumentByPerson(savedContractDAO, person);
            ContractDAO finalContract = repository.save(updatedContract);
            log.info("[CREATE-CONTRACT]-[Postgres] Contract and Person saved in the database with id: {}", finalContract.getId());

            return contractBuilder.toContractResponse(adapter.convertToContract(finalContract));
        } catch (ConstraintViolationException e) {
            log.error("[CREATE-CONTRACT]-[Service] ConstraintViolationException: ", e);
            throw e;

        } catch (DataIntegrityViolationException e) {
            log.error("[CREATE-CONTRACT]-[Service] DataIntegrityViolationException: ", e);
            throw e;
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private ContractDAO contractDocumentByPerson(ContractDAO contractDAO, PersonResponseData person) {
        ContractDAO personSaved = new ContractDAO();

        contractDAO.setIntegrationPersonPending(false);
        contractDAO.setStatus(ContractStatusEnum.ACTIVE);
        contractDAO.setFullNamePerson(person.getFirstName().concat(person.getLastName()));
        contractDAO.setGenderPerson(person.getGender());
        contractDAO.setCpfPerson(person.getCpf());
        contractDAO.setBirthdayAtPerson(person.getBirthdayAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        contractDAO.setUpdatedAt(LocalDateTime.now());

        return personSaved;
    }

    private void verifyExistingContractsEquals(Contract contract) {
        List<ContractDAO> existingServicesId = repository.findByPersonIdAndProductId(contract.getPersonId(), contract.getProductId());
        List<ContractDAO> existingContractId = repository.findById(contract.getId());

        if (!existingServicesId.isEmpty()) {
            String errorMessage = String.format("Contract with personId '%s' and productId '%s' already exists.",
                    contract.getPersonId(), contract.getProductId());
            log.error("[CREATE-CONTRACT]-[Service] {}", errorMessage);
            throw new DataIntegrityViolationException(errorMessage);
        }
        if (!existingContractId.isEmpty()) {
            String errorMessage = String.format("Contract with id '%s' already exist.",
                    contract.getId());
            log.error("[CREATE-CONTRACT]-[Service] {}", errorMessage);
            throw new DataIntegrityViolationException(errorMessage);
        }
    }
}

