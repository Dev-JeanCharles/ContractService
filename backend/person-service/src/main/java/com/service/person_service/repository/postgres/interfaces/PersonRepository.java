package com.service.person_service.repository.postgres.interfaces;

import com.service.person_service.repository.postgres.adapter.PersonDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<PersonDAO, String> {

    List<PersonDAO> findByPersonId(String personId);
    List<PersonDAO> findByCpf(String cpf);
}
