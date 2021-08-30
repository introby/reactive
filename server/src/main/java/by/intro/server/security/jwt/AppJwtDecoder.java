package by.intro.server.security.jwt;

import by.intro.server.model.secure.Account;
import com.google.common.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import reactor.core.publisher.Mono;

public class AppJwtDecoder implements ReactiveJwtDecoder {

    private final ReactiveJwtDecoder reactiveJwtDecoder;
//    private TokenRepository tokenRepository = BeanUtils.getBean(TokenRepository.class);
//    private JwtService jwtService = BeanUtils.getBean(JwtService.class);

    @Autowired
    private JwtService jwtService;
    @Autowired
    private Cache<String, Account> tokenRepo;

    public AppJwtDecoder(ReactiveJwtDecoder reactiveJwtDecoder) {
        this.reactiveJwtDecoder = reactiveJwtDecoder;
    }



    @Override
    public Mono<Jwt> decode(String token) throws JwtException {
        return reactiveJwtDecoder.decode(token).doOnNext(jwt -> {
            String id = jwt.getId();
//            Account auth = tokenRepository.getAuthFromAccessToken(id);
            Account auth = tokenRepo.getIfPresent(id);
            if (auth == null) {
                throw new JwtException("Invalid Account");
            }
            jwtService.setTokenId(id);
        });
    }
}
