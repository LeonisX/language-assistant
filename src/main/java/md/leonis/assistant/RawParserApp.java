package md.leonis.assistant;

import md.leonis.assistant.source.gse.GseSourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RawParserApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext springContext = SpringApplication.run(RawParserApp.class, args);

        springContext.getBean(GseSourceFactory.class).getParser().parse();
    }
}
