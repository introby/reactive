package by.intro.server.service;

import by.intro.server.model.Person;
import by.intro.server.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public Flux<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Flux<Object> getGroupsByNameLength() {
        return personRepository.findAll()
                .groupBy(personDto -> personDto.getName().length())
                .concatMap(Flux::collectList);
    }

    public Flux<Person> getAllPersonsWithChangedEmail() {
        return personRepository.findAll()
                .map(personDto -> {
                    personDto.setEmail(personDto.getEmail().replaceAll("@.{2,255}\\.[a-z]{2,}$", "@company.com"));
                    return personDto;
                });
    }

    public Mono<Person> getOldestPerson() {
        return personRepository.findAll()
                .sort(Person::compareTo)
                .last();
    }

    public Mono<Person> addPerson(Person person) {
        return personRepository.save(person);
    }

    public Flux<Person> addAllPersons(Flux<Person> persons) {
        return personRepository.saveAll(persons);
    }

    public Mono<Person> getPersonById(String id) {
        return personRepository.findById(id);
    }

    public Mono<Person> deletePerson(String id) {
        Mono<Person> dbPerson = getPersonById(id);
        if (Objects.isNull(dbPerson)) {
            return Mono.empty();
        }
        return getPersonById(id).switchIfEmpty(Mono.empty()).filter(Objects::nonNull).flatMap(personToBeDeleted -> personRepository
                .delete(personToBeDeleted).then(Mono.just(personToBeDeleted)));
    }

}
