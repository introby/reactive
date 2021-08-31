package by.intro.server.security;

import org.springframework.context.annotation.Bean;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static by.intro.server.model.secure.AccountRole.ADMIN;
import static by.intro.server.model.secure.AccountRole.USER;

public class SecurityConfig {
        protected RSocketSecurity pattern(RSocketSecurity security) {
        return security.authorizePayload(authorize -> authorize
                .route("signin").permitAll()
                .route("refresh").permitAll()
                .route("shell-client").permitAll()
                .route("client-status").permitAll()
                .route("signout").authenticated()
                .route("channel").hasRole(ADMIN.name())
                .route("stream").hasRole(ADMIN.name())
                .route("request-response").hasAnyRole(USER.name(), ADMIN.name())
                .route("fire-and-forget").hasAnyRole(USER.name(), ADMIN.name())
                .anyRequest().authenticated()
                .anyExchange().permitAll()
        );
    }

    protected RSocketMessageHandler getMessageHandler(RSocketStrategies strategies) {
        RSocketMessageHandler mh = new RSocketMessageHandler();
        mh.getArgumentResolverConfigurer().addCustomResolver(
                new AuthenticationPrincipalArgumentResolver());
        mh.setRSocketStrategies(strategies);
        return mh;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf().disable().authorizeExchange().anyExchange().permitAll();
        return http.build();
    }
}
