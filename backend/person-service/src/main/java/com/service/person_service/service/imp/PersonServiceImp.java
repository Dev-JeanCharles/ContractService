package com.service.person_service.service.imp;

import com.service.person_service.application.web.controllers.builder.PersonBuilder;
import com.service.person_service.application.web.dto.responses.PersonResponse;
import com.service.person_service.domain.model.Person;
import com.service.person_service.repository.postgres.adapter.PersonAdapter;
import com.service.person_service.repository.postgres.adapter.PersonDAO;
import com.service.person_service.repository.postgres.interfaces.PersonRepository;
import com.service.person_service.service.interfaces.PersonService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PersonServiceImp implements PersonService {

    private final PersonAdapter adapter;
    private final PersonRepository repository;
    private final PersonBuilder personBuilder;

    public PersonServiceImp(PersonAdapter adapter, PersonRepository repository, PersonBuilder personBuilder) {
        this.adapter = adapter;
        this.repository = repository;
        this.personBuilder = personBuilder;
    }

    @Override
    public PersonResponse create(@Valid Person person) {
        log.info("[CREATE-PERSON]-[Service] Creating a new person with personId: {}", person.getPersonId());

        PersonDAO personDAO = adapter.convertToPersonDAO(person);
        PersonDAO savedPersonDAO = repository.save(personDAO);
        log.info("[CREATE-PERSON]-[Postgres] Person saved in the database with id: {}", savedPersonDAO.getPersonId());

        return personBuilder.toPersonResponse(adapter.convertToPerson(savedPersonDAO));
    }
}
