package by.intro.server.controller;

import by.intro.server.model.secure.Account;
import by.intro.server.model.secure.Token;
import by.intro.server.security.jwt.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class AccountController {

    private final JwtService jwtService;

    public AccountController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @MessageMapping("signin")
    Mono<Token> signin(Account account) {
        log.info("signin input: {}", account);
        String principal = account.getLogin();
        String credential = account.getPassword();
        Account user = jwtService.authenticate(principal, credential);
        if (user != null) {
            Token token = jwtService.signToken(user);
            return Mono.just(token);
        }
        return Mono.empty();
    }

    @MessageMapping("refresh")
    Mono<Token> refresh(String token) {
        String refreshToken = token.replace("\"", "");
        log.info("authenticate refreshToken: {}", refreshToken);
        Mono<Account> mono = jwtService.authenticate(refreshToken);
        return mono.map(jwtService::signToken);
    }

    @MessageMapping("signout")
    public Mono<Void> signout() {
        jwtService.revokeAccessToken();
        return Mono.empty();
    }
}
