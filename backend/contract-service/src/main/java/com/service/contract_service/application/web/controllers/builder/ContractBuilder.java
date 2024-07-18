package com.service.contract_service.application.web.controllers.builder;

import com.service.contract_service.application.web.controllers.dto.requesties.ContractRequest;
import com.service.contract_service.application.web.controllers.dto.responses.ContractResponse;
import com.service.contract_service.domain.model.Contract;
import com.service.contract_service.domain.enums.ContractStatusEnum;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ContractBuilder {

    public Contract toContractEntity(ContractRequest contractRequest) {
        return Contract.builder()
                .personId(contractRequest.getPersonId())
                .productId(contractRequest.getProductId())
                .status(ContractStatusEnum.PENDING)
                .integrationPersonPending(false)
                .integrationProductPending(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public ContractResponse toContractResponse(Contract contract) {
        return ContractResponse.builder()
                .numberContract(contract.getId().toString())
                .message("Successfully recorded!")
                .build();
    }
}
