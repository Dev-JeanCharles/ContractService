package application.web.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    private static final Logger logger = (Logger) LoggerFactory.getLogger(ContractController.class);

    @PostMapping
    public ResponseEntity<ContractResponse> createContract(@RequestBody ContractRequest contractRequest) {
        try {
            logger.info("[CREATE-CONTRACT]-[Controller] Starting the contracted data: [{}]", contractRequest);

            ContractResponse response = contractService.create(buildContractEntity(contractRequest));
            logger.info("[CREATE-CONTRACT]-[Controller] The hiring process was successfully completed");

            return new ResponseEntity<ContractResponse>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("[CREATE-CONTRACT]-[Controller] failed", e);
            return buildResponse("123456", "Failed");
        }
    }
}
