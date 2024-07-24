package com.service.person_service.repository.postgres.interfaces;

import com.service.person_service.repository.postgres.adapter.PersonDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<PersonDAO, String> {
}
