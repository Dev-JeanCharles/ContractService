package com.service.contract_service.repository.postgres.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.service.contract_service.domain.enums.ContractStatusEnum;
import com.service.contract_service.repository.imp.GeneratorIdImpl;
import com.service.contract_service.repository.interfaces.GeneratorId;
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

    @NotNull(message = "product_id cannot be null")
    @Column(name = "product_id", nullable = false)
    private String productId;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "status cannot be null")
    @Column(name = "status", nullable = false)
    private ContractStatusEnum status;

    @Column(name = "integration_person_pending")
    private Boolean integrationPersonPending;

    @Column(name = "integration_product_pending")
    private Boolean integrationProductPending;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = null;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
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
