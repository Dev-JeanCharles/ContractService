package com.service.person_service.application.web.controllers;

import com.service.person_service.application.web.dto.responses.PersonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/person")
public class PersonController {

    @GetMapping("/{person_id}")
    public PersonResponse findProductByProductId(@PathVariable("person_id") String personId) {
        PersonResponse person = PersonResponse.builder()
                .personId("123e4567-e89b-12d3-a456-426614174000")
                .firstName("João")
                .lastname("Silva")
                .gender("Masculino")
                .cpf("123.456.789-00")
                .birthdayAt(LocalDate.of(1990, 5, 15))
                .createdAt(LocalDateTime.now())
                .build();

        try {
            System.out.print("Executou product-services");
            return person;
        } catch (Exception e) {
            throw new RuntimeException("FALHA", e);
        }
    }
}
