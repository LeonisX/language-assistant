package md.leonis.assistant;

import md.leonis.assistant.service.SampleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MainAppTests {

    @Autowired
    private SampleService sampleService;

    @Test
    public void contextLoads() {
        sampleService.echo();
    }

}
