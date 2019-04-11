package md.leonis.assistant;

import md.leonis.assistant.service.TestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = {MainApp.class})
class MainAppTests {

    @Autowired
    private TestService testService;

    @Test
    void contextLoads() {
        testService.echo();
    }

}
