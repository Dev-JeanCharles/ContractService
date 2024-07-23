package com.service.contract_service.repository.imp;

import com.service.contract_service.repository.interfaces.GeneratorId;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class GeneratorIdImpl implements GeneratorId {

    @Override
    public String generatedContractId() {
        Random random = new Random();
        int randomInt = 100 + random.nextInt(900);
        return "CONTRACT_" + randomInt;
    }
}
