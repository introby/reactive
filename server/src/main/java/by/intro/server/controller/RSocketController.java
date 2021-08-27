package by.intro.server.controller;

import by.intro.server.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class RSocketController {

    static final String SERVER = "Server";
    static final String RESPONSE = "Response";
    static final String STREAM = "Stream";
    static final String CHANNEL = "Channel";

    private final List<RSocketRequester> CLIENTS = new ArrayList<>();

    @MessageMapping("request-response")
    Message requestResponse(Message request) {
        log.info("Request-Response: {}", request);
        return new Message(SERVER, RESPONSE);
    }

    @MessageMapping("fire-and-forget")
    public void fireAndForget(Message request) {
        log.info("Received fire-and-forget request: {}", request);
    }

    @MessageMapping("stream")
    Flux<Message> stream(Message request) {
        log.info("Received stream request: {}", request);
        return Flux
                .interval(Duration.ofSeconds(1))
                .map(index -> new Message(SERVER, STREAM, index))
                .log();
    }

    @MessageMapping("channel")
    Flux<Message> channel(final Flux<Duration> settings) {
        return settings
                .doOnNext(setting -> log.info("\nFrequency setting is {} second(s).\n", setting.getSeconds()))
                .switchMap(setting -> Flux.interval(setting)
                        .map(index -> new Message(SERVER, CHANNEL, index)))
                .log();
    }

    @ConnectMapping("shell-client")
    void connectShellClientAndAskForTelemetry(RSocketRequester requester, @Payload String client) {
        requester.rsocket()
                .onClose()
                .doFirst(() -> {
                    log.info("Client: {} CONNECTED.", client);
                    CLIENTS.add(requester);
                })
                .doOnError(error -> {
                    log.warn("Channel to client {} CLOSED", client);
                })
                .doFinally(consumer -> {
                    CLIENTS.remove(requester);
                    log.info("Client {} DISCONNECTED", client);
                })
                .subscribe();

        requester.route("client-status")
                .data("OPEN")
                .retrieveFlux(String.class)
                .doOnNext(s -> log.info("Client: {} Free Memory: {}.",client,s))
                .subscribe();
    }
}
