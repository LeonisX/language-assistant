package md.leonis.assistant;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MainApp extends Application {

    private static final Logger log = LoggerFactory.getLogger(MainApp.class);

    private ConfigurableApplicationContext springContext;
    private Parent rootNode;
    private FXMLLoader fxmlLoader;

    public static void main(String[] args) {
        //SpringApplication.run(MainApp.class, args);
        launch(args);
    }

    @Override
    public void init() {
        springContext = SpringApplication.run(MainApp.class);
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(springContext::getBean);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        log.info("Starting {}!", "PROJECT_TITLE");
        fxmlLoader.setLocation(getClass().getResource("/fxml/sample.fxml"));
        rootNode = fxmlLoader.load();

        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(rootNode, 800, 600);
		/*primaryStage.centerOnScreen();
		primaryStage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});*/
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        springContext.stop();
    }

}
