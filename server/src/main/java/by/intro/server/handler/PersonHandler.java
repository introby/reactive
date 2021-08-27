package by.intro.server.handler;

import by.intro.server.model.Person;
import by.intro.server.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

@Component
@RequiredArgsConstructor
public class PersonHandler {

    private final PersonService personService;

    public Mono<ServerResponse> findById(ServerRequest request) {
        String id = request.pathVariable("id");
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(personService.getPersonById(id), Person.class);
    }

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(personService.getAllPersons(), Person.class);
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        final Mono<Person> person = request.bodyToMono(Person.class);
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromPublisher(person.flatMap(personService::addPerson), Person.class));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(personService.deletePerson(id), Void.class);
    }
}
