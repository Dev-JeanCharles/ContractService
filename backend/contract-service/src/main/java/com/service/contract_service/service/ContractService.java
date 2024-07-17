package com.service.contract_service.service;

import com.service.contract_service.application.web.controllers.dto.responses.ContractResponse;
import com.service.contract_service.domain.model.Contract;

public interface ContractService {

    ContractResponse create(Contract contractEntity);
}
