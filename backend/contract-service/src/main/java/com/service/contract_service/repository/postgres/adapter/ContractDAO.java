package com.service.contract_service.repository.postgres.adapter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.service.contract_service.domain.enums.ContractStatusEnum;
import com.service.contract_service.repository.postgres.imp.GeneratorIdImpl;
import com.service.contract_service.repository.postgres.interfaces.GeneratorId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
@Table(name = "tb_contract")
@Entity
public class ContractDAO {

    @Id
    @Column(name = "id")
    private String id = null;

    @NotNull(message = "person_id cannot be null")
    @Column(name = "person_id", nullable = false)
    private String personId;

    @Column(name = "full_name")
    private String fullNamePerson = null;

    @Column(name = "gender_person")
    private String genderPerson = null;

    @Column(name = "cpf")
    private String cpfPerson = null;

    @Column(name = "birthday")
    private String birthdayAtPerson = null;

    @NotNull(message = "product_id cannot be null")
    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "name_product")
    private String nameProduct = null;

    @Column(name = "description_product")
    private String descriptionProduct = null;

    @Column(name = "quantity_product")
    @NotNull(message = "product quantity cannot be null")
    private Integer quantityProduct = null;

    @Column(name = "origin_product")
    @NotNull(message = "product origin cannot be null")
    private String originProduct = null;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "status cannot be null")
    @Column(name = "status", nullable = false)
    private ContractStatusEnum status;

    @Column(name = "integration_person_pending")
    private Boolean integrationPersonPending;

    @Column(name = "integration_product_pending")
    private Boolean integrationProductPending;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = null;
    @Column(name = "canceled_at")
    private LocalDateTime cancelamentDat = null;

    @PrePersist
    public void prePersist() {
        this.id = generateContractId();
    }

    private String generateContractId() {
        GeneratorId generator = new GeneratorIdImpl();
        return generator.generatedContractId();
    }

}
