package md.leonis.assistant.config;

import javafx.stage.Stage;
import md.leonis.assistant.exception.ExceptionWriter;
import md.leonis.assistant.view.SpringFXMLLoader;
import md.leonis.assistant.view.StageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import java.io.StringWriter;
import java.util.ResourceBundle;

@Configuration
public class AppJavaConfig {

    @Autowired
    private SpringFXMLLoader springFXMLLoader;

    /**
     * Useful when dumping stack trace to a string for logging.
     *
     * @return ExceptionWriter contains logging utility methods
     */
    @Bean
    @Scope("prototype")
    public ExceptionWriter exceptionWriter() {
        return new ExceptionWriter(new StringWriter());
    }

    @Bean
    public ResourceBundle resourceBundle() {
        return ResourceBundle.getBundle("Bundle");
    }

    @Bean
    @Lazy //Stage only created after Spring context bootstap
    public StageManager stageManager(Stage stage) {
        return new StageManager(springFXMLLoader, stage);
    }

    @Bean
    public ConfigHolder configHolder() {
        return new ConfigHolder();
    }
}
