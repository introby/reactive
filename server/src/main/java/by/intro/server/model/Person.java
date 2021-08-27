package by.intro.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "persons")
public class Person implements Comparable<Person> {

    @Id
    private String id;
    private String name;
    private int age;
    private String email;

    @Override
    public int compareTo(Person p) {

        return age == p.getAge() ? 0 : age > p.age ? 1 : -1;
    }
}
