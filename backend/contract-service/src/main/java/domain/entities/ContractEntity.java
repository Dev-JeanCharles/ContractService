package domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import domain.enums.ContractStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_contract")
public class ContractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @JsonProperty("person_id")
    @Column(name = "person_id", nullable = false)
    private String personID;

    @JsonProperty("product_id")
    @Column(name = "product_id", nullable = false)
    private String productID;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ContractStatusEnum status;

    @JsonProperty("integration_person_pending")
    @Column(name = "integration_person_pending")
    private Boolean integrationPersonPending;

    @JsonProperty("integration_product_pending")
    @Column(name = "integration_product_pending")
    private Boolean integrationProductPending;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonProperty("created_at")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonProperty("updated_at")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("canceled_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "canceled_at")
    private LocalDateTime cancelamentDat;
}
