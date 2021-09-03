package by.intro.server.controller;


import by.intro.personclientlibs.dto.PersonDto;
import by.intro.server.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/persons", produces = MediaType.APPLICATION_JSON_VALUE)
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/all")
    public Flux<PersonDto> getAllPersons() {
        return personService.getAllPersons().take(6);
    }

    @GetMapping("/groups")
    public Flux<Object> getGroups() {
        return personService.getGroupsByNameLength();
    }

    @GetMapping("/new-email")
    public Flux<PersonDto> getAllPersonsWithChangedEmail() {
        return personService.getAllPersonsWithChangedEmail();
    }

    @GetMapping("/oldest")
    public Mono<PersonDto> getOldestPerson() {
        return personService.getOldestPerson();
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PersonDto> addPerson(@RequestBody PersonDto person) {
        return personService.addPerson(person);
    }

    @PostMapping(value = "/add-all", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Flux<PersonDto> addAllPersons(@RequestBody Flux<PersonDto> persons) {
        return personService.addAllPersons(persons);
    }

}

