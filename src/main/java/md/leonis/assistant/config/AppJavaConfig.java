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

    //    @Bean
//    public FxmlScanner fxmlScanner() {
//        FxmlScanner fxmlScanner = new FxmlScanner("classpath*:/fxml/**/*.fxml");
//        Set<String> scannedFxmlFiles = null;
//        try {
//            scannedFxmlFiles = fxmlScanner.scan();
//            //TODO: have to get part of pathname in there
//            //i.e: LoggerTab.fxml --> /fxml/tabs/LoggerTab.fxml
//            // Main.fxml -> /fxml/Main.fxml (could hardcode fxml in both cases since convention)
//            //but need to get hold of sub-directories
//            scannedFxmlFiles.forEach((file) -> {
//                System.out.println("file = " + file);
//            });
//        } catch (IOException ex) {
//            System.out.println("error in fxmlScanner.scan() ");
//        }
//        generateDynamicFxmlEnum(scannedFxmlFiles);
//        return fxmlScanner;
//    }
//
//    private void generateDynamicFxmlEnum(Set<String> scannedFxmlFiles) {
//        /*
//        The build process is divided into steps, and during one of the steps,
//        generate-sources, a maven plugin has the opportunity to generate source
//        that will then be compiled during the compile step.
//        */
//        TypeSpec typeSpecForEnum = TypeSpec.enumBuilder("FxmlEnum")
//                .addModifiers(Modifier.PUBLIC)
//                .addEnumConstant("MAIN")
//                .addEnumConstant("LOGIN")
//                .build();
//
//        JavaFile javaFile = JavaFile.builder("com.mvp.java.spring.config", typeSpecForEnum)
//                .addFileComment("AUTO_GENERATED")
//                .build();
//
//        try {
//            javaFile.writeTo(Paths.get("./src/main/java"));
//        } catch (IOException ex) {
//            System.out.println("error in javaFile.writeTo() ");
//        }
//
//    }
}
