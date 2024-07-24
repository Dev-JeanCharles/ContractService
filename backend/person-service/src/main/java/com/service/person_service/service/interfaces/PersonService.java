package com.service.person_service.service.interfaces;

import com.service.person_service.application.web.dto.responses.PersonResponse;
import com.service.person_service.domain.model.Person;

public interface PersonService {
    PersonResponse create(Person person);
}
