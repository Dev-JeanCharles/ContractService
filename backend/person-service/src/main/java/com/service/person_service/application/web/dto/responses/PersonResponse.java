package com.service.person_service.application.web.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonResponse {
    @JsonProperty("person_id")
    private String personId;

    private String message;
}


