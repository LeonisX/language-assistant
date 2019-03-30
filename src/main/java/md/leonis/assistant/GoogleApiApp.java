package md.leonis.assistant;

import md.leonis.assistant.utils.GoogleApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class GoogleApiApp {

    private static final Logger log = LoggerFactory.getLogger(GoogleApiApp.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext springContext = SpringApplication.run(GoogleApiApp.class, args);

        log.info("Translating");

        String translation = GoogleApi.translate("number");

        System.out.println(translation);
    }


}
