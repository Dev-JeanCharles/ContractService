package com.service.contract_service.application.web.controllers;

import com.service.contract_service.application.web.controllers.builder.ContractBuilder;
import com.service.contract_service.application.web.controllers.dto.requesties.ContractRequest;
import com.service.contract_service.application.web.controllers.dto.responses.ContractResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.service.contract_service.service.ContractService;

@RestController
@RequestMapping(value = "/contract")
public class ContractController {

    private final ContractService contractService;
    private static final Logger logger = LoggerFactory.getLogger(ContractController.class);
    private final ContractBuilder contractBuilder;

    @Autowired
    public ContractController(ContractService contractService, ContractBuilder contractBuilder) {
        this.contractService = contractService;
        this.contractBuilder = contractBuilder;
    }

    @PostMapping
    public ResponseEntity<ContractResponse> createContract(@RequestBody ContractRequest contractRequest) {
        try {
            logger.info("[CREATE-CONTRACT]-[Controller] Starting contract creation for request: [{}]", contractRequest);

            ContractResponse contractResponse = contractService.create(contractBuilder.toContractEntity(contractRequest));
            logger.info("[CREATE-CONTRACT]-[Controller] Contract creation completed successfully for request: {}", contractRequest);

            return new ResponseEntity<>(contractResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("[CREATE-CONTRACT]-[Controller] Error occurred during contract creation for request: {}", contractRequest, e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
