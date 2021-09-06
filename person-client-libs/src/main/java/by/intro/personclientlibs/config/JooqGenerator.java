package by.intro.personclientlibs.config;

import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class JooqGenerator {

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public void generate() {
        org.jooq.meta.jaxb.Configuration configuration = new org.jooq.meta.jaxb.Configuration()
                .withLogging(Logging.INFO)
                .withJdbc(new Jdbc()
                        .withDriver("org.postgresql.Driver")
                        .withUrl(url)
                        .withUser(username)
                        .withPassword(password)
                )
                .withGenerator(new Generator()
                        .withDatabase(new Database()
                                .withName("org.jooq.meta.postgres.PostgresDatabase")
                                .withIncludes(".*")
                                .withExcludes("flyway_schema_history")
                                .withInputSchema("public")
                        )
                        .withTarget(new Target()
                                .withPackageName("by.intro.server.model")
                                .withDirectory("server/target/generated-sources/jooq")
                        )
                );
        try {
            GenerationTool.generate(configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
