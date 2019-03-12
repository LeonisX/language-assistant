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

    private static String[] savedArgs;

    public static void main(String[] args) {
        //SpringApplication.run(MainApp.class, args);
        savedArgs = args;
        launch(args);
    }

    /*А вот с точкой входа в приложение все гораздо интересней!
    Нам необходимо инициализировать Spring контекст и сделать это можно в двух разных местах:
    Если Вам потребуется создать экземпляры типов Scene, Stage, открыть popup, то делать это нужно в методе start(), т.к. он вызывается в UI потоке.
    В противном случае можете воспользоваться методом init() (как в примере ниже), который вызывается не в UI потоке перед вызовом метода start().*/
    @Override
    public void init() {
        springContext = SpringApplication.run(MainApp.class, savedArgs);

        // Именно на момент инициализации JavaFX мы запускаем инициализацию Spring контекста:
        //springContext = SpringApplication.run(getClass(), savedArgs);
        // Ну и следующей строкой заполняем текущий объект бинами:
        //springContext.getAutowireCapableBeanFactory().autowireBean(this);

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
        // Here probably close connections to DB
    }

}
