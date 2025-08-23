package co.com.crediya.r2dbc.config;


import io.asyncer.r2dbc.mysql.MySqlConnectionConfiguration;
import io.asyncer.r2dbc.mysql.MySqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class R2DBCConfig {

    @Value("${database.host}")
    private String host;

    @Value("${database.port}")
    private Integer port;

    @Value("${database.username}")
    private String username;

    @Value("${database.password}")
    private String password;

    @Value("${database.name}")
    private String database;


    @Bean
    public ConnectionFactory connectionFactory() {
        return MySqlConnectionFactory.from(
                MySqlConnectionConfiguration.builder()
                        .host(host)
                        .port(port)
                        .user(username)
                        .password(password)
                        .database(database)
                        .build()
        );
    }

}