package by.intro.server.repository;

import by.intro.server.model.secure.Account;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static by.intro.server.model.secure.AccountRole.ADMIN;
import static by.intro.server.model.secure.AccountRole.USER;

@Repository
public class AccountRepository {

    private Map<String, Account> repo = new HashMap<>();

    @PostConstruct
    public void init() {
        Account admin = Account.builder().login("admin").password("admin").role(ADMIN).build();
        repo.put("admin", admin);
        Account user = Account.builder().login("user").password("user").role(USER).build();
        repo.put("user", user);
    }

    public Account retrieve(String login) {
        return repo.get(login);
    }
}
