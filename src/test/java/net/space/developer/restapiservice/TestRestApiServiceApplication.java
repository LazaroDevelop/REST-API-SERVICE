package net.space.developer.restapiservice;

import net.space.developer.restapiservice.config.TestcontainersConfiguration;
import org.springframework.boot.SpringApplication;

public class TestRestApiServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(RestApiServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
