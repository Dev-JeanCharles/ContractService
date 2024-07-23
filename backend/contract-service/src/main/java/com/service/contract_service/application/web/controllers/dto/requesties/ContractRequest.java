package com.service.contract_service.application.web.controllers.dto.requesties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractRequest {

    @JsonProperty("person_id")
    private String personId;

    @JsonProperty("product_id")
    private String productId;
}
