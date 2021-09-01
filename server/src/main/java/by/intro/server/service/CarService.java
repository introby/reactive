package by.intro.server.service;

import by.intro.server.model.Car;
import by.intro.server.repository.CarRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Flux<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Flux<Car> findCarsByName(String name) {
        return (name != null) ? carRepository.findByName(name) : carRepository.findAll();
    }

    public Mono<Car> findCarById(long id) {
        return carRepository.findById(id);
    }

    public Mono<Car> addNewCar(Car car) {
        return carRepository.save(car);
    }

    public Mono<Car> updateCar(long id, Car car) {
        return carRepository.findById(id)
                .flatMap(c -> {
                    car.setId(c.getId());
                    return carRepository.save(car);
                });

    }

    public Mono<Void> deleteCar(Car student) {
        return carRepository.delete(student);
    }

}
