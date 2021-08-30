package by.intro.server.security.jwt;

import by.intro.server.model.secure.Account;
import by.intro.server.model.secure.AccountToken;
import by.intro.server.model.secure.Token;
import by.intro.server.repository.AccountRepository;
import by.intro.server.repository.TokenRepository;
import by.intro.server.security.TokenUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class JwtService {

    private final AccountRepository accountRepository;
    private final TokenRepository tokenRepository;
    @Setter
    private String tokenId;

    public JwtService(AccountRepository accountRepository, TokenRepository tokenRepository) {
        this.accountRepository = accountRepository;
        this.tokenRepository = tokenRepository;
    }

    public Account authenticate(String principal, String credential) {
        log.info("principal={},credential={}", principal, credential);
        try {
            Account user = accountRepository.retrieve(principal);
            if (user.getPassword().equals(credential)) {
                return user;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public Mono<Account> authenticate(String refreshToken) {
        ReactiveJwtDecoder reactiveJwtDecoder = TokenUtils.getRefreshTokenDecoder();
        return reactiveJwtDecoder.decode(refreshToken).map(jwt -> {
            try {
                Account account = Account.builder().login(jwt.getSubject()).role(jwt.getClaim("scope")).build();
                log.info("verify successfully. account:{}", account);
                Account auth = tokenRepository.getAuthFromRefreshToken(jwt.getId());
                if (account.equals(auth)) {
                    return account;
                }
            } catch (Exception e) {
                log.error("", e);
            }
            return new Account();
        });
    }

    public Token signToken(Account account) {
        Token token = new Token();
        AccountToken accessToken = TokenUtils.generateAccessToken(account);
        tokenRepository.storeAccessToken(accessToken.getTokenId(), accessToken.getAccount());
        AccountToken refreshToken = TokenUtils.generateRefreshToken(account);
        tokenRepository.storeRefreshToken(refreshToken.getTokenId(), refreshToken.getAccount());
        token.setAccessToken(accessToken.getToken());
        token.setRefreshToken(refreshToken.getToken());
        return token;
    }

    public void revokeAccessToken() {
        tokenRepository.deleteAccessToken(tokenId);
    }
}
