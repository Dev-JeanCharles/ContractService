package com.service.person_service.application.web.controllers;

import com.service.person_service.application.web.controllers.builder.PersonBuilder;
import com.service.person_service.application.web.dto.requesties.PersonRequest;
import com.service.person_service.application.web.dto.responses.PersonResponse;
import com.service.person_service.domain.model.Person;
import com.service.person_service.service.imp.PersonServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping
    public ResponseEntity<PersonResponse> createPerson(@RequestBody PersonRequest request) {
        log.info("[CREATE-PERSON]-[Controller] Starting person creation for request: [{}]", request);

        Person person = personBuilder.toBuilderEntity(request);
        PersonResponse response = personService.create(person);
        log.info("[CREATE-PERSON]-[Controller] Person creation completed successfully for request: {}", request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
