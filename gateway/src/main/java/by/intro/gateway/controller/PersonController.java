package by.intro.gateway.controller;

import by.intro.gateway.model.Person;
import by.intro.gateway.service.PersonServiceApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactivefeign.webclient.WebReactiveFeign;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

    PersonServiceApi client = WebReactiveFeign
            .<PersonServiceApi>builder()
            .target(PersonServiceApi.class, "http://localhost:8080");

    @GetMapping
    public ResponseEntity<Flux<Person>> getAllPersons() {
        Flux<Person> allPersons = client.getAllPersons();
        return ResponseEntity.ok().body(allPersons);
    }

    @GetMapping("/oldest-person")
    public ResponseEntity<Mono<Person>> getOldestPerson() {
        Mono<Person> oldestPerson = client.getOldestPerson();
        return ResponseEntity.ok().body(oldestPerson);
    }

    @PostMapping
    public ResponseEntity<Mono<Person>> addPerson(@RequestBody Person person) {
        Mono<Person> addedPerson = client.addPerson(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedPerson);
    }

}
