package com.service.contract_service.service.interfaces;

import com.service.contract_service.infrastructure.gateways.person.dto.PersonResponseData;
import org.springframework.data.crossstore.ChangeSetPersister;

public interface PersonService {
    PersonResponseData getPersonById(String personId) throws ChangeSetPersister.NotFoundException;
}
