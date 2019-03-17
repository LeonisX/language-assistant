package md.leonis.assistant;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import md.leonis.assistant.dao.PersonDAO;
import md.leonis.assistant.domain.Person;
import md.leonis.assistant.view.FxmlView;
import md.leonis.assistant.view.StageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class MainApp extends Application {

    private static final Logger log = LoggerFactory.getLogger(MainApp.class);

    private ConfigurableApplicationContext springContext;
    private StageManager stageManager;
    //private Parent rootNode;
    //private FXMLLoader fxmlLoader;

    //private static String[] savedArgs;

    public static void main(String[] args) {
        //SpringApplication.run(MainApp.class, args);
        //savedArgs = args;
        Application.launch(args);
    }

    /*А вот с точкой входа в приложение все гораздо интересней!
    Нам необходимо инициализировать Spring контекст и сделать это можно в двух разных местах:
    Если Вам потребуется создать экземпляры типов Scene, Stage, открыть popup, то делать это нужно в методе start(), т.к. он вызывается в UI потоке.
    В противном случае можете воспользоваться методом init() (как в примере ниже), который вызывается не в UI потоке перед вызовом метода start().*/
    @Override
    public void init() {
        String[] args = this.getParameters().getRaw().toArray(new String[0]);
        // Именно на момент инициализации JavaFX мы запускаем инициализацию Spring контекста:
        springContext = SpringApplication.run(MainApp.class, args);

        //springContext = bootstrapSpringApplicationContext();
    }

    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void start(Stage primaryStage) throws ParseException {
        //TODO load title
        log.info("Starting {}!", "Language Assistant");
        stageManager = springContext.getBean(StageManager.class, primaryStage);
        displayInitialScene();

        /*fxmlLoader.setLocation(getClass().getResource("/fxml/sample.fxml"));
        rootNode = fxmlLoader.load();

        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(rootNode, 800, 600);*/
		/*primaryStage.centerOnScreen();
		primaryStage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});*/
        /*primaryStage.setScene(scene);
        primaryStage.show();*/


        //TODO test; delete
        PersonDAO personDAO = springContext.getBean(PersonDAO.class);

        long count = personDAO.count();

        if (count == 0) {
            Person p1 = new Person();

            p1.setFullName("John");

            Date d1 = df.parse("1980-12-20");
            p1.setDateOfBirth(d1);
            //
            Person p2 = new Person();

            p2.setFullName("Smith");
            Date d2 = df.parse("1985-11-11");
            p2.setDateOfBirth(d2);

            personDAO.save(p1);
            personDAO.save(p2);
        }

        Iterable<Person> all = personDAO.findAll();

        StringBuilder sb = new StringBuilder();

        all.forEach(p -> sb.append(p.getFullName()).append("<br>"));

        System.out.println(sb.toString());

    }

    @Override
    public void stop() {
        springContext.stop();
        // Close or Stop???
        //springContext.close();
        // Here probably close connections to DB
    }

    /**
     * Useful to override this method by sub-classes wishing to change the first
     * Scene to be displayed on startup. Example: Functional tests on main
     * window.
     */
    protected void displayInitialScene() {
        //TODO w/o borders https://stackoverflow.com/questions/14972199/how-to-create-splash-screen-with-transparent-background-in-javafx
        // Caused by: java.lang.IllegalStateException: Cannot set style once stage has been set visible
        stageManager.switchScene(FxmlView.SPLASH, StageStyle.UNDECORATED);
    }

    private ConfigurableApplicationContext bootstrapSpringApplicationContext() {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(MainApp.class);
        String[] args = getParameters().getRaw().toArray(new String[0]);
        builder.headless(false); //needed for TestFX integration testing or eles will get a java.awt.HeadlessException during tests
        return builder.run(args);
    }

}
