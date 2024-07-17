package service;

import application.web.controllers.dto.responses.ContractResponse;
import domain.model.Contract;

public interface ContractService {

    ContractResponse create(Contract contractEntity);
}
