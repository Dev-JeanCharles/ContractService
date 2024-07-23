package com.service.contract_service.application.web.controllers;

import com.service.contract_service.application.web.controllers.builder.ContractBuilder;
import com.service.contract_service.application.web.controllers.dto.requesties.ContractRequest;
import com.service.contract_service.application.web.controllers.dto.responses.ContractResponse;
import com.service.contract_service.domain.commons.UnauthorizationException;
import com.service.contract_service.domain.model.Contract;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.service.contract_service.service.interfaces.ContractService;

@Slf4j
@RestController
@RequestMapping(value = "/contract")
public class ContractController {

    private final ContractService contractService;
    private final ContractBuilder contractBuilder;

    @Autowired
    public ContractController(ContractService contractService, ContractBuilder contractBuilder) {
        this.contractService = contractService;
        this.contractBuilder = contractBuilder;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContractResponse> createContract(@RequestBody ContractRequest contractRequest) {
        log.info("[CREATE-CONTRACT]-[Controller] Starting contract creation for request: [{}]", contractRequest);

        try {
            Contract contract = contractBuilder.toContractEntity(contractRequest);
            ContractResponse contractResponse = contractService.create(contract);
            log.info("[CREATE-CONTRACT]-[Controller] Contract creation completed successfully for request: {}", contractRequest);

            return new ResponseEntity<>(contractResponse, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            log.error("[CREATE-CONTRACT]-[Controller] DataIntegrityViolationException: ", e);
            throw e;
        } catch (UnauthorizationException e) {
            log.error("[CREATE-CONTRACT]-[Controller] UnauthorizationException: ", e);
            throw e;
        }
    }
}
