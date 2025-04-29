package net.space.developer.restapiservice;

import org.springframework.boot.SpringApplication;

public class TestRestApiServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(RestApiServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
