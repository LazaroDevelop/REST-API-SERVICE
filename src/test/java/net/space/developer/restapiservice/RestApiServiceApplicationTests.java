package net.space.developer.restapiservice;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest
@Import(TestcontainersConfiguration.class)
class RestApiServiceApplicationTests {

    @Test
    void contextLoads() {
        log.info("hola");
    }

}
