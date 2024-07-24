package com.service.person_service.repository.postgres.imp;

import com.service.person_service.repository.postgres.interfaces.GeneratorId;

import java.util.Random;

public class GeneratorIdImp implements GeneratorId {

    @Override
    public String generatePersonId() {
        Random random = new Random();
        int randomInt = 100 + random.nextInt(900);
        return "PERSON_" + randomInt;
    }
}
