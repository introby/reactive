package by.intro.server.controller;


import by.intro.server.service.PersonService;
import by.intro.server.model.Person;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
//@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/persons", produces = MediaType.APPLICATION_JSON_VALUE)
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/all")
    public Flux<Person> getAllPersons() {
        return personService.getAllPersons().take(6);
    }

    @GetMapping("/groups")
    public Flux<Object> getGroups() {
        return personService.getGroupsByNameLength();
    }

    @GetMapping("/new-email")
    public Flux<Person> getAllPersonsWithChangedEmail() {
        return personService.getAllPersonsWithChangedEmail();
    }

    @GetMapping("/oldest")
    public Mono<Person> getOldestPerson() {
        return personService.getOldestPerson();
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Person> addPerson(@RequestBody Person person) {
        return personService.addPerson(person);
    }

    @PostMapping(value = "/add-all", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Flux<Person> addAllPersons(@RequestBody Flux<Person> persons) {
        return personService.addAllPersons(persons);
    }

}

