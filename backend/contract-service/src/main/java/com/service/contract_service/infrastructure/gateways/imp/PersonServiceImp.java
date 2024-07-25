package com.service.contract_service.infrastructure.gateways.imp;

import com.service.contract_service.domain.commons.PersonServiceIntegrationException;
import com.service.contract_service.infrastructure.gateways.person.PersonGateway;
import com.service.contract_service.infrastructure.gateways.person.dto.PersonResponseData;
import com.service.contract_service.service.interfaces.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PersonServiceImp implements PersonService {

    private final PersonGateway gateway;

    public PersonServiceImp(PersonGateway gateway) {
        this.gateway = gateway;
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 3000))
    @Override
    public PersonResponseData getPersonById(String personId) throws ChangeSetPersister.NotFoundException {
        try {
            log.info("[CREATE-CONTRACT]-[Gateway] starting integration with service-person PersonId: {}", personId);

            System.out.println("Execute Gateway Person");

            PersonResponseData personResponse = gateway.getPersonById(personId);

            log.info("Received response from Person Service: {}", personResponse);

            if (personResponse == null) {
                log.error("[CREATE-CONTRACT]-[Gateway] No client exists with this personId: {}", personId);
                throw new ChangeSetPersister.NotFoundException();
            }

            return personResponse;
        } catch (ChangeSetPersister.NotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("[CREATE-CONTRACT]-[Gateway] Integration Failure with PersonService", e);
            throw new PersonServiceIntegrationException("Integration Failure with PersonService");
        }
    }
}


