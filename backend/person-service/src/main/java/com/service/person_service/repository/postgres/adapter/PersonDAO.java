package com.service.person_service.repository.postgres.adapter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.service.person_service.repository.postgres.imp.GeneratorIdImp;
import com.service.person_service.repository.postgres.interfaces.GeneratorId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
@Table(name = "tb_person")
@Entity
public class PersonDAO {

    @Id
    @Column(name = "person_id")
    private String personId = generatePersonId();
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String gender;
    private String cpf;
    @Column(name = "birthday")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private String birthdayAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt = null;

    @PrePersist
    public void prePersist() {
        this.personId = generatePersonId();
    }

    private String generatePersonId() {
        GeneratorId generator = new GeneratorIdImp();
        return generator.generatePersonId();
    }
}
