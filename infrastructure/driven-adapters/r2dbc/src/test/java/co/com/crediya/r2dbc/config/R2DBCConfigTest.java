package co.com.crediya.r2dbc.config;


import io.asyncer.r2dbc.mysql.MySqlConnectionConfiguration;
import io.asyncer.r2dbc.mysql.MySqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = R2DBCConfig.class)
@TestPropertySource(properties = {
        "database.host=localhost",
        "database.port=3306",
        "database.username=test_user",
        "database.password=test_pass",
        "database.name=test_db"
})
class R2DBCConfigTest {

    @Autowired
    private R2DBCConfig r2dbcConfig;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Test
    void shouldLoadPropertiesAndCreateConnectionFactory() {
        assertThat(r2dbcConfig).isNotNull();

        assertThat(r2dbcConfig).extracting(
                "host", "port", "username", "password", "database"
        ).doesNotContainNull();

        assertThat(connectionFactory).isNotNull();
    }
}