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
        Contract contract = new Contract();

        contract.setPersonId(contractRequest.getPersonId());
        contract.setProductId(contractRequest.getProductId());
        contract.setStatus(ContractStatusEnum.PENDING);
        contract.setIntegrationPersonPending(false);
        contract.setIntegrationProductPending(false);
        contract.setCreatedAt(LocalDateTime.now());

        return contract;
    }

    public ContractResponse toContractResponse(Contract contract) {
        ContractResponse contractResponse = new ContractResponse();

        contractResponse.setNumberContract(contract.getId().toString());
        contractResponse.setMessage("Successfully recorded!");

        return contractResponse;
    }
}
