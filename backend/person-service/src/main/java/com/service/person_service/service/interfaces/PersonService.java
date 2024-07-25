package com.service.person_service.service.interfaces;

import com.service.person_service.application.web.dto.responses.PersonResponse;
import com.service.person_service.domain.model.Person;
import com.service.person_service.repository.postgres.adapter.PersonDAO;

public interface PersonService {
    PersonResponse create(Person person);
    PersonDAO findPersonById(String personId);
}
