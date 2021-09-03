package by.intro.server.controller;

import by.intro.server.model.Car;
import by.intro.server.model.dto.CarDto;
import by.intro.server.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public Flux<Car> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Car>> getCar(@PathVariable long id) {
        return carService.findCarById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<Car> addNewCar(@RequestBody Car car) {
        return carService.addNewCar(car);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Car>> updateCar(@PathVariable long id, @RequestBody Car car) {
        return carService.updateCar(id, car)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteStudent(@PathVariable long id) {
        return carService.findCarById(id)
                .flatMap(c ->
                        carService.deleteCar(c)
                                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/car-list")
    public Flux<CarDto> findAllCars() {
        return carService.findAllCars();
    }
}
