package by.intro.server.service;

import by.intro.server.model.Car;
import by.intro.server.model.CarDto;
import by.intro.server.model.tables.Audi;
import by.intro.server.model.tables.Seat;
import by.intro.server.model.tables.Vw;
import by.intro.server.repository.CarRepository;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final DSLContext context;

    public CarService(CarRepository carRepository, DSLContext context) {
        this.carRepository = carRepository;
        this.context = context;
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

    public Flux<CarDto> findAllCars() {

        return Flux.from(context.select(Audi.AUDI.NAME, Audi.AUDI.CREATION_DATE, Audi.AUDI.COUNT)
                .from(Audi.AUDI)
                .unionAll(context.select(Vw.VW.NAME, Vw.VW.CREATION_DATE, Vw.VW.COUNT)
                        .from(Vw.VW))
                .unionAll(context.select(Seat.SEAT.NAME, Seat.SEAT.CREATION_DATE, Seat.SEAT.COUNT)
                        .from(Seat.SEAT)))
                .take(8)
                .map(r -> r.into(CarDto.class));
    }

}
