package com.service.person_service.application.web.controllers;

import com.service.person_service.application.web.controllers.builder.PersonBuilder;
import com.service.person_service.application.web.dto.requesties.PersonRequest;
import com.service.person_service.application.web.dto.responses.PersonResponse;
import com.service.person_service.domain.model.Person;
import com.service.person_service.repository.postgres.adapter.PersonDAO;
import com.service.person_service.service.imp.PersonServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonBuilder personBuilder;
    private final PersonServiceImp personService;

    @Autowired
    public PersonController(PersonBuilder personBuilder, PersonServiceImp personService) {
        this.personBuilder = personBuilder;
        this.personService = personService;
    }

    @GetMapping("/{personId}")
    public ResponseEntity<PersonDAO> getPersonById(@PathVariable String personId) {
        PersonDAO person = personService.findPersonById(personId);
        if (person != null) {
            return ResponseEntity.ok(person);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<PersonResponse> createPerson(@RequestBody PersonRequest request) {
        log.info("[CREATE-PERSON]-[Controller] Starting person creation for request: [{}]", request);

        try {
            Person person = personBuilder.toBuilderEntity(request);
            PersonResponse response = personService.create(person);
            log.info("[CREATE-PERSON]-[Controller] Person creation completed successfully for request: {}", request);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (DataIntegrityViolationException e) {
            log.error("[CREATE-PERSON]-[Controller] DataIntegrityViolationException: ", e);
            throw e;
        }
    }
}
