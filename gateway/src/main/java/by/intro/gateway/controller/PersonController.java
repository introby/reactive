package by.intro.gateway.controller;

import by.intro.gateway.service.PersonServiceApi;
import by.intro.personclientlibs.dto.PersonDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name="Person Controller", description="Operations with person")
@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

    private final PersonServiceApi client;

    public PersonController(PersonServiceApi client) {
        this.client = client;
    }

    @Operation(
            summary = "Get all persons",
            description = "Get all persons from Server module"
    )
    @GetMapping
    public ResponseEntity<Flux<PersonDto>> getAllPersons() {
        Flux<PersonDto> allPersons = client.getAllPersons();
        return ResponseEntity.ok().body(allPersons);
    }

    @Operation(
            summary = "Get the oldest person",
            description = "Get the oldest person from Server module"
    )
    @GetMapping("/oldest-person")
    public ResponseEntity<Mono<PersonDto>> getOldestPerson() {
        Mono<PersonDto> oldestPerson = client.getOldestPerson();
        return ResponseEntity.ok().body(oldestPerson);
    }

    @Operation(
            summary = "Add person",
            description = "Add person to DB"
    )
    @PostMapping
    public ResponseEntity<Mono<PersonDto>> addPerson(@RequestBody @Parameter(description = "Person to save") PersonDto personDto) {
        Mono<PersonDto> addedPerson = client.addPerson(personDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedPerson);
    }
}
