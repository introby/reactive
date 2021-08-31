package by.intro.server.security.jwt;

import by.intro.server.model.secure.Account;
import by.intro.server.repository.TokenRepository;
import by.intro.server.security.TokenUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Primary
public class AppJwtDecoder implements ReactiveJwtDecoder {

    private final ReactiveJwtDecoder reactiveJwtDecoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    public AppJwtDecoder(JwtService jwtService, TokenRepository tokenRepository) {
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.reactiveJwtDecoder = TokenUtils.getAccessTokenDecoder();
    }

    @Override
    public Mono<Jwt> decode(String token) throws JwtException {
        return reactiveJwtDecoder.decode(token).doOnNext(jwt -> {
            String id = jwt.getId();
            Account auth = tokenRepository.getAuthFromAccessToken(id);
            if (auth == null) {
                throw new JwtException("Invalid Account");
            }
            jwtService.setTokenId(id);
        });
    }
}
