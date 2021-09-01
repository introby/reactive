package by.intro.server.repository;

import by.intro.server.model.Car;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CarRepository extends ReactiveCrudRepository<Car, Long> {

    Flux<Car> findByName(String name);
}
