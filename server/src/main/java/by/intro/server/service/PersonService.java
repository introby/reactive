package by.intro.server.service;

import by.intro.personclientlibs.dto.PersonDto;
import by.intro.server.mapper.PersonMapper;
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
    private final PersonMapper personMapper;

    public Flux<PersonDto> getAllPersons() {
        Flux<Person> persons = personRepository.findAll();
        return persons.map(personMapper::personToPersonDto);
    }

    public Flux<Object> getGroupsByNameLength() {
        return personRepository.findAll()
                .groupBy(personDto -> personDto.getName().length())
                .concatMap(Flux::collectList);
    }

    public Flux<PersonDto> getAllPersonsWithChangedEmail() {
        return personRepository.findAll()
                .map(personDto -> {
                    personDto.setEmail(personDto.getEmail().replaceAll("@.{2,255}\\.[a-z]{2,}$", "@company.com"));
                    return personDto;
                })
                .map(personMapper::personToPersonDto);
    }

    public Mono<PersonDto> getOldestPerson() {
        return personRepository.findAll()
                .sort(Person::compareTo)
                .last()
                .map(personMapper::personToPersonDto);
    }

    public Mono<PersonDto> addPerson(PersonDto dto) {
        return personRepository.save(personMapper.dtoToPerson(dto))
                .map(personMapper::personToPersonDto);
    }

    public Flux<PersonDto> addAllPersons(Flux<PersonDto> dtoFlux) {
        Flux<Person> personFlux = dtoFlux.map(personMapper::dtoToPerson);
        return personRepository.saveAll(personFlux)
                .map(personMapper::personToPersonDto);
    }

    public Mono<Person> getPersonById(String id) {
        return personRepository.findById(id);
    }

    public Mono<PersonDto> deletePerson(String id) {
        Mono<Person> dbPerson = getPersonById(id);
        if (Objects.isNull(dbPerson)) {
            return Mono.empty();
        }
        return getPersonById(id).switchIfEmpty(Mono.empty()).filter(Objects::nonNull).flatMap(personToBeDeleted -> personRepository
                .delete(personToBeDeleted).then(Mono.just(personToBeDeleted))).map(personMapper::personToPersonDto);
    }

}
