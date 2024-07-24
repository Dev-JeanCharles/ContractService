package com.service.person_service.repository.postgres.adapter;

import com.service.person_service.domain.model.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonAdapter {


    public PersonDAO convertToPersonDAO(Person person) {
        PersonDAO personDAO = new PersonDAO();

        personDAO.setPersonId(person.getPersonId());
        personDAO.setFirstName(person.getFirstName());
        personDAO.setLastName(person.getLastName());
        personDAO.setGender(person.getGender());
        personDAO.setCpf(person.getCpf());
        personDAO.setBirthdayAt(person.getBirthdayAt());
        personDAO.setCreatedAt(person.getCreatedAt());
        personDAO.setUpdatedAt(person.getUpdatedAt());

        return personDAO;
    }

    public Person convertToPerson(PersonDAO savedPersonDAO) {
        Person person = new Person();

        person.setPersonId(savedPersonDAO.getPersonId());
        person.setFirstName(savedPersonDAO.getFirstName());
        person.setLastName(savedPersonDAO.getLastName());
        person.setGender(savedPersonDAO.getGender());
        person.setCpf(savedPersonDAO.getCpf());
        person.setBirthdayAt(savedPersonDAO.getBirthdayAt());
        person.setCreatedAt(savedPersonDAO.getCreatedAt());
        person.setUpdatedAt(savedPersonDAO.getUpdatedAt());

        return person;
    }
}
