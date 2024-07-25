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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

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

        try {
            verifyExistingPersonEquals(person);

            PersonDAO personDAO = adapter.convertToPersonDAO(person);
            PersonDAO savedPersonDAO = repository.save(personDAO);
            log.info("[CREATE-PERSON]-[Postgres] Person saved in the database with id: {}", savedPersonDAO.getPersonId());

            return personBuilder.toPersonResponse(adapter.convertToPerson(savedPersonDAO));
        }catch (DataIntegrityViolationException e ) {
            log.error("[CREATE-PERSON]-[Service] ConstraintViolationException: ", e);
            throw e;
        }
    }

    @Override
    public PersonDAO findPersonById(String personId) {
        return repository.findById(personId).orElse(null);
    }

    private void verifyExistingPersonEquals(Person person) {
        List<PersonDAO> existingPersonId = repository.findByPersonId(person.getPersonId());
        List<PersonDAO> existingCpf = repository.findByCpf(person.getCpf());

        if (!existingPersonId.isEmpty()) {
            String errorMessage = String.format("Contract with personId '%s' already exists.",
                    person.getPersonId());
            log.error("[CREATE-PERSON]-[Service] {}", errorMessage);
            throw new DataIntegrityViolationException(errorMessage);
        }
        if (!existingCpf.isEmpty()) {
            String errorMessage = String.format("Contract with cpf '%s' already exists.",
                    person.getCpf());
            log.error("[CREATE-PERSON]-[Service] {}", errorMessage);
            throw new DataIntegrityViolationException(errorMessage);
        }
    }
}
