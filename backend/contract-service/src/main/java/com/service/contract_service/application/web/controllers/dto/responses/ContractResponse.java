package com.service.contract_service.application.web.controllers.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractResponse {
    @JsonProperty("number_contract")
    private String numberContract;

    private String message;

}
