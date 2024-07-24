package com.service.person_service.application.web.controllers.builder;

import com.service.person_service.application.web.dto.requesties.PersonRequest;
import com.service.person_service.application.web.dto.responses.PersonResponse;
import com.service.person_service.domain.model.Person;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PersonBuilder {

    public Person toBuilderEntity(PersonRequest request) {
        return Person.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastname())
                .gender(request.getGender())
                .cpf(request.getCpf())
                .birthdayAt(request.getBirthdayAt())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public PersonResponse toPersonResponse(Person person) {
        return PersonResponse.builder()
                .personId(person.getPersonId())
                .message("Successfully recorded!")
                .build();
    }
}
