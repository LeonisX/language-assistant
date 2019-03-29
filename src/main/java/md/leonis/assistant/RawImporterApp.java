package md.leonis.assistant;

import md.leonis.assistant.source.gse.GseSourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RawImporterApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext springContext = SpringApplication.run(RawImporterApp.class, args);

        springContext.getBean(GseSourceFactory.class).getCrawler().crawl();
    }

}
