package by.intro.client;

import by.intro.client.model.Message;
import io.rsocket.SocketAcceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@ShellComponent
public class RSocketShellClient {

    private final RSocketRequester rsocketRequester;
    private static Disposable disposable;

    private static final String CLIENT = "Client";
    private static final String REQUEST = "Request";
    private static final String FIRE_AND_FORGET = "Fire-And-Forget";
    private static final String STREAM = "Stream";

    public RSocketShellClient(RSocketRequester.Builder rsocketRequesterBuilder, RSocketStrategies strategies) {

        String client = UUID.randomUUID().toString();
        log.info("Connecting using client ID: {}", client);
        SocketAcceptor responder = RSocketMessageHandler.responder(strategies, new ClientHandler());

        this.rsocketRequester = rsocketRequesterBuilder
                .setupRoute("shell-client")
                .setupData(client)
                .rsocketStrategies(strategies)
                .rsocketConnector(connector -> connector.acceptor(responder))
                .tcp("localhost", 7001);

//        this.rsocketRequester.rsocket()
//                .onClose()
//                .doOnError(error -> log.warn("Connection CLOSED"))
//                .doFinally(consumer -> log.info("Client DISCONNECTED"))
//                .subscribe();
    }

    @ShellMethod("Send one request. One response will be printed.")
    public void requestResponse() throws InterruptedException {
        log.info("\nSending one request. Waiting for one response...");
        Message message = this.rsocketRequester
                .route("request-response")
                .data(new Message(CLIENT, REQUEST))
                .retrieveMono(Message.class)
                .block();
        log.info("\nResponse was: {}", message);
    }

    @ShellMethod("Send one request. No response will be returned.")
    public void fireAndForget() throws InterruptedException {
        log.info("\nFire-And-Forget. Sending one request. Expect no response (check server log)...");
        this.rsocketRequester
                .route("fire-and-forget")
                .data(new Message(CLIENT, FIRE_AND_FORGET))
                .send()
                .block();
    }

    @ShellMethod("Send one request. Many responses (stream) will be printed.")
    public void stream() {
        log.info("\nRequest-Stream. Sending one request. Waiting for unlimited responses (Stop process to quit)...");
        this.disposable = this.rsocketRequester
                .route("stream")
                .data(new Message(CLIENT, STREAM))
                .retrieveFlux(Message.class)
                .subscribe(er -> log.info("Response received: {}", er));
    }

    @ShellMethod("Stop streaming messages from the server.")
    public void s() {
        if (null != disposable) {
            disposable.dispose();
        }
    }

    @ShellMethod("Stream some settings to the server. Stream of responses will be printed.")
    public void channel() {
        Mono<Duration> setting1 = Mono.just(Duration.ofSeconds(1));
        Mono<Duration> setting2 = Mono.just(Duration.ofSeconds(3)).delayElement(Duration.ofSeconds(5));
        Mono<Duration> setting3 = Mono.just(Duration.ofSeconds(5)).delayElement(Duration.ofSeconds(15));

        Flux<Duration> settings = Flux.concat(setting1, setting2, setting3)
                .doOnNext(d -> log.info("\nSending setting for {}-second interval.\n", d.getSeconds()));

        disposable = this.rsocketRequester
                .route("channel")
                .data(settings)
                .retrieveFlux(Message.class)
                .subscribe(message -> log.info("Received: {} \n(Type 's' to stop.)", message));
    }
}

@Slf4j
class ClientHandler {

    @MessageMapping("client-status")
    public Flux<String> statusUpdate(String status) {
        log.info("Connection {}", status);
        return Flux.interval(Duration.ofSeconds(5)).map(index -> String.valueOf(Runtime.getRuntime().freeMemory()));
    }
}
