package by.intro.server.service;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;

public class ReactiveTest {

    @Test
    public void createFlux_just() {
        Flux<String> carFlux = Flux.just("BMW", "Seat", "VW", "Audi", "Skoda");
        carFlux.subscribe(c -> System.out.println("Here's some cars: " + c));

        StepVerifier.create(carFlux)
                .expectNext("BMW")
                .expectNext("Seat")
                .expectNext("VW")
                .expectNext("Audi")
                .expectNext("Skoda")
                .verifyComplete();
    }

    @Test
    public void createFlux_fromArray() {
        String[] cars = new String[] {
                "BMW", "Seat", "VW", "Audi", "Skoda"
        };
        Flux<String> carFlux = Flux.fromArray(cars);
        carFlux.subscribe(c -> System.out.println("Here's some cars: " + c));

        StepVerifier.create(carFlux)
                .expectNext("BMW")
                .expectNext("Seat")
                .expectNext("VW")
                .expectNext("Audi")
                .expectNext("Skoda")
                .verifyComplete();
    }

    @Test
    public void createFlux_range() {
        Flux<Integer> intervalFlux = Flux.range(1, 5);

        StepVerifier.create(intervalFlux)
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .expectNext(5)
                .verifyComplete();
    }

    @Test
    public void createFlux_interval() {
        Flux<Long> intervalFlux = Flux.interval(Duration.ofSeconds(2))
                .take(5);

        StepVerifier.create(intervalFlux)
                .expectNext(0L)
                .expectNext(1L)
                .expectNext(2L)
                .expectNext(3L)
                .expectNext(4L)
                .verifyComplete();
    }

    @Test
    public void mergeFluxes() {
        Flux<String> characterFlux = Flux
                .just("Potter", "Weasley", "Grainger")
                .delayElements(Duration.ofMillis(500));
        Flux<String> carFlux = Flux
                .just("BMW", "Seat", "Audi")
                .delaySubscription(Duration.ofMillis(250))
                .delayElements(Duration.ofMillis(500));

        Flux<String> mergedFlux = characterFlux.mergeWith(carFlux);

        StepVerifier.create(mergedFlux)
                .expectNext("Potter")
                .expectNext("BMW")
                .expectNext("Weasley")
                .expectNext("Seat")
                .expectNext("Grainger")
                .expectNext("Audi")
                .verifyComplete();
    }

    @Test
    public void zipFluxes() {

        Flux<String> characterFlux = Flux
                .just("Potter", "Weasley", "Grainger");
        Flux<String> carFlux = Flux
                .just("BMW", "Seat", "Audi");

        Flux<Tuple2<String, String>> zippedFlux = Flux.zip(characterFlux, carFlux);

        StepVerifier.create(zippedFlux)
                .expectNextMatches(p ->
                        p.getT1().equals("Potter") &&
                        p.getT2().equals("BMW"))
                .expectNextMatches(p ->
                        p.getT1().equals("Weasley") &&
                        p.getT2().equals("Seat"))
                .expectNextMatches(p ->
                        p.getT1().equals("Grainger") &&
                        p.getT2().equals("Audi"))
                .verifyComplete();
    }

    @Test
    public void firstFlux() {
        Flux<String> characterFlux = Flux
                .just("Potter", "Weasley", "Grainger")
                .delaySubscription(Duration.ofMillis(100));
        Flux<String> carFlux = Flux
                .just("BMW", "Seat", "Audi");

        Flux<String> firstFlux = Flux.firstWithSignal(characterFlux, carFlux);

        StepVerifier.create(firstFlux)
                .expectNext("BMW")
                .expectNext("Seat")
                .expectNext("Audi")
                .verifyComplete();
    }
}
