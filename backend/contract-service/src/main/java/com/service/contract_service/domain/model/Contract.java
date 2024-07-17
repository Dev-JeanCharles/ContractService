package com.service.contract_service.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.service.contract_service.domain.enums.ContractStatusEnum;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contract {

    private UUID id = null;

    private String personId;

    private String productId;

    private ContractStatusEnum status;

    private Boolean integrationPersonPending;

    private Boolean integrationProductPending;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt = null;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime cancelamentDat = null;
}
