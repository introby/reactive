package by.intro.server.mapper;

import by.intro.personclientlibs.dto.PersonDto;
import by.intro.server.model.Person;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    PersonDto personToPersonDto(Person person);
    Person dtoToPerson(PersonDto dto);
}
