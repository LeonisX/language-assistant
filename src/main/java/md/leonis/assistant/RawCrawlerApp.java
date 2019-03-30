package md.leonis.assistant;

import md.leonis.assistant.source.gse.GseSourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RawCrawlerApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext springContext = SpringApplication.run(RawCrawlerApp.class, args);

        springContext.getBean(GseSourceFactory.class).getCrawler().crawl();
    }

}
