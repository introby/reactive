package by.intro.server.config;

import io.r2dbc.spi.ConnectionFactory;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig {

    private final ConnectionFactory cfi;

    public JooqConfig(ConnectionFactory cfi) {
        this.cfi = cfi;
    }

    @Bean
    public DSLContext jooqDslContext() {
        return DSL.using(cfi).dsl();
    }
}
