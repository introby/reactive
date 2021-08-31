package by.intro.gateway.service;

import by.intro.gateway.model.Person;
import feign.Headers;
import feign.RequestLine;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Headers({ "Accept: application/json" })
public interface PersonServiceApi {

    @RequestLine("GET /api/v1/persons/all")
    Flux<Person> getAllPersons();

    @RequestLine("GET /api/v1/persons/oldest")
    Mono<Person> getOldestPerson();

    @RequestLine("POST /api/v1/persons/add")
    Mono<Person> addPerson(Person person);
}
