package application.web.controllers.builder;

import application.web.controllers.dto.requesties.ContractRequest;
import application.web.controllers.dto.responses.ContractResponse;
import domain.model.Contract;
import domain.enums.ContractStatusEnum;

import java.time.LocalDateTime;

public class Builders {

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
