package by.intro.server.repository;

import by.intro.server.model.secure.Account;
import by.intro.server.security.TokenUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class TokenRepository {

    Cache<String, Account> accessTokenTable = CacheBuilder.
            newBuilder()
            .expireAfterWrite(TokenUtils.ACCESS_EXPIRE, TimeUnit.MINUTES)
            .build();
    Cache<String, Account> refreshTokenTable = CacheBuilder.
            newBuilder()
            .expireAfterWrite(TokenUtils.REFRESH_EXPIRE, TimeUnit.DAYS)
            .build();

    public void storeAccessToken(String tokenId, Account account) {
        accessTokenTable.put(tokenId, account);
        System.out.println();
    }

    public void storeRefreshToken(String tokenId, Account account) {
        refreshTokenTable.put(tokenId, account);
    }

    public Account getAuthFromAccessToken(String tokenId) {
        return accessTokenTable.getIfPresent(tokenId);
    }

    public Account getAuthFromRefreshToken(String tokenId) {
        return refreshTokenTable.getIfPresent(tokenId);
    }

    public void deleteAccessToken(String tokenId) {
        accessTokenTable.invalidate(tokenId);
    }

    public void deleteRefreshToken(String tokenId) {
        refreshTokenTable.invalidate(tokenId);
    }

    @Bean(name = "cache")
    public Cache<String, Account> getAccessTokenTable() {
        return accessTokenTable;
    }
}
