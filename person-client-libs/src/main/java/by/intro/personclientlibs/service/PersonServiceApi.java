package by.intro.personclientlibs.service;

import by.intro.personclientlibs.model.dto.PersonDto;
import feign.Headers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import reactivefeign.spring.config.EnableReactiveFeignClients;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@EnableReactiveFeignClients
@ConditionalOnProperty(value = "gateway.person.url")
@ReactiveFeignClient(
        name = "PersonServiceApi",
        url = "${gateway.person.url}",
        path = "/api/v1/persons")
@Headers({"Accept: application/json"})
public interface PersonServiceApi {

    @GetMapping("/all")
    Flux<PersonDto> getAllPersons();

    @GetMapping("/oldest")
    Mono<PersonDto> getOldestPerson();

    @PostMapping("/add")
    Mono<PersonDto> addPerson(PersonDto personDto);
}
