package com.service.contract_service.repository.imp;

import com.service.contract_service.repository.interfaces.ContractIdGenerator;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class ContractIdGeneratorImpl implements ContractIdGenerator {

    @Override
    public String generatedContractId() {
        Random random = new Random();
        int randomInt = 100 + random.nextInt(900);
        return "CONTRACT_" + randomInt;
    }
}
