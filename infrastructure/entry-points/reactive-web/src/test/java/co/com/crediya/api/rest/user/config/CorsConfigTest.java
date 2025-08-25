package co.com.crediya.api.rest.user.config;

import co.com.crediya.api.config.CorsConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CorsConfigTest {

    @Test
    void corsWebFilterShouldBeCreated() {
        String origins = "http://localhost:4200,http://localhost:3000";
        CorsConfig config = new CorsConfig();
        CorsWebFilter corsWebFilter = config.corsWebFilter(origins);

        assertThat(corsWebFilter).isNotNull();
    }
}
