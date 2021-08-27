package by.intro.server.controller;

import by.intro.server.handler.PersonHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class PersonRouter {

    @Bean
    public RouterFunction<ServerResponse> route(PersonHandler handler) {
        return RouterFunctions
                .route(GET("/api/v1/persons/getAllPersons").and(accept(MediaType.APPLICATION_JSON)), handler::findAll)
                .andRoute(GET("/api/v1/persons/getPerson/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::findById)
                .andRoute(POST("/api/v1/persons/createPerson").and(accept(MediaType.APPLICATION_JSON)), handler::save)
                .andRoute(DELETE("/api/v1/persons/deletePerson/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::delete);
    }
}
