package application.web.controllers;

import application.web.controllers.builder.Builders;
import application.web.controllers.dto.requesties.ContractRequest;
import application.web.controllers.dto.responses.ContractResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.ContractService;

@RestController
@RequestMapping(value = "/contract")
public class ContractController {

    @Autowired
    private ContractService contractService;

    private static final Logger logger = (Logger) LoggerFactory.getLogger(ContractController.class);

    Builders builders = new Builders();

    @PostMapping
    public ResponseEntity<ContractResponse> createContract(@RequestBody ContractRequest contractRequest) {
            logger.info("[CREATE-CONTRACT]-[Controller] Starting the contracted data: [{}]", contractRequest);

            ContractResponse response = contractService.create(builders.toContractEntity(contractRequest));
            logger.info("[CREATE-CONTRACT]-[Controller] The hiring process was successfully completed");

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
    }
