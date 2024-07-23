package com.service.contract_service.infrastructure.gateways.person;

import com.service.contract_service.infrastructure.gateways.person.dto.PersonResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "person-service",
        url = "${gateway.person.services.url}"
)
public interface PersonGateway {

    @GetMapping(value = "/person/{id}")
    PersonResponseData getPersonById(@PathVariable("id") String personId);
}
