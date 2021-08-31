package by.intro.server.security;

import by.intro.server.model.secure.Account;
import by.intro.server.model.secure.AccountToken;

import by.intro.server.security.jwt.AppJwtDecoder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class TokenUtils {

    // access token timeout, min
    public static final long ACCESS_EXPIRE = 30;
    // refresh token timeout, days
    public static final long REFRESH_EXPIRE = 7;

    private static final String ACCESS_SECRET_KEY = "and0X3Rva2VuLWJhc2VkX29wZW5hcGlfZm9yX3Jzb2NrZXRfYWNjZXNzX3Rva2Vu";
    private static final String REFRESH_SECRET_KEY = "and0X3Rva2VuLWJhc2VkX29wZW5hcGlfNF9yc29ja2V0X3JlZnJlc2hfdG9rZW4=";
    private static final MacAlgorithm MAC_ALGORITHM = MacAlgorithm.HS256;
    private static final String HMAC_SHA_256 = "HmacSHA256";

    public static AccountToken generateAccessToken(Account account) {
        Algorithm accessAlgorithm = Algorithm.HMAC256(ACCESS_SECRET_KEY);
        return generateToken(account, accessAlgorithm, ACCESS_EXPIRE, ChronoUnit.MINUTES);
    }

    public static ReactiveJwtDecoder getAccessTokenDecoder() {
        SecretKeySpec secretKey = new SecretKeySpec(ACCESS_SECRET_KEY.getBytes(), HMAC_SHA_256);
        return NimbusReactiveJwtDecoder.withSecretKey(secretKey)
                .macAlgorithm(MAC_ALGORITHM)
                .build();
    }

    public static AccountToken generateRefreshToken(Account account) {
        Algorithm refreshAlgorithm = Algorithm.HMAC256(REFRESH_SECRET_KEY);
        return generateToken(account, refreshAlgorithm, REFRESH_EXPIRE, ChronoUnit.DAYS);
    }

    public static ReactiveJwtDecoder getRefreshTokenDecoder() {
        SecretKeySpec secretKey = new SecretKeySpec(REFRESH_SECRET_KEY.getBytes(), HMAC_SHA_256);
        return NimbusReactiveJwtDecoder.withSecretKey(secretKey)
                .macAlgorithm(MAC_ALGORITHM)
                .build();
    }

    private static AccountToken generateToken(Account account, Algorithm algorithm, long expire, ChronoUnit unit) {
        String tokenId = UUID.randomUUID().toString();
        Instant instant;
        Instant now = Instant.now();
        if (now.isSupported(unit)) {
            instant = now.plus(expire, unit);
        } else {
            log.error("unit param is not supported");
            return null;
        }
        String token = JWT.create()
                .withJWTId(tokenId)
                .withSubject(account.getLogin())
                .withClaim("scope", account.getRole().name())
                .withExpiresAt(Date.from(instant))
                .sign(algorithm);

        return AccountToken.builder().tokenId(tokenId).token(token).account(account).build();
    }

}
