package md.leonis.assistant.view;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;

import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Manages switching Scenes on the Primary Stage
 */
public class StageManager {

    private static final Logger log = getLogger(StageManager.class);
    private final Stage primaryStage;
    private final SpringFXMLLoader springFXMLLoader;

    public StageManager(SpringFXMLLoader springFXMLLoader, Stage stage) {
        this.springFXMLLoader = springFXMLLoader;
        this.primaryStage = stage;
    }

    public void switchScene(final FxmlView view) {
        switchScene(view, StageStyle.DECORATED);
    }

    public void switchScene(final FxmlView view, StageStyle stageStyle) {
        Parent viewRootNodeHierarchy = loadViewNodeHierarchy(view.getFxmlFile());
        show(viewRootNodeHierarchy, view.getTitle(), stageStyle);
    }

    private void show(final Parent rootnode, String title, StageStyle stageStyle) {
        Scene scene = prepareScene(rootnode);
        //scene.getStylesheets().add("/styles/Styles.css");

        //primaryStage.initStyle(stageStyle);
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();

        try {
            primaryStage.show();
        } catch (Exception exception) {
            logAndExit ("Unable to show scene for title" + title,  exception);
        }
    }

    public void showNewWindow(final FxmlView view) {
        Parent viewRootNodeHierarchy = loadViewNodeHierarchy(view.getFxmlFile());

        Label secondLabel = new Label("I'm a Label on new Window");

        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(secondLabel);

        Scene secondScene = new Scene(viewRootNodeHierarchy);
        //Scene secondScene = new Scene(viewRootNodeHierarchy, 230, 100);

        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle(view.getTitle());
        newWindow.setScene(secondScene);

        // Set position of second window, related to primary window.
        newWindow.setX(primaryStage.getX() + 200);
        newWindow.setY(primaryStage.getY() + 100);

        newWindow.show();
    }

    private Scene prepareScene(Parent rootNode) {
        Scene scene = primaryStage.getScene();

        if (scene == null) {
            return new Scene(rootNode);
        }

        scene.setRoot(rootNode);
        return scene;
    }

    /**
     * Loads the object hierarchy from a FXML document and returns to root node
     * of that hierarchy.
     *
     * @return Parent root node of the FXML document hierarchy
     */
    private Parent loadViewNodeHierarchy(String fxmlFilePath) {
        Parent rootNode = null;
        try {
            rootNode = springFXMLLoader.load(fxmlFilePath);
            Objects.requireNonNull(rootNode, "A Root FXML node must not be null");
        } catch (Exception exception) {
            logAndExit("Unable to load FXML view " + fxmlFilePath, exception);
        }
        return rootNode;
    }

    private void logAndExit(String errorMsg, Exception exception) {
        log.error(errorMsg, exception, exception.getCause());
        Platform.exit();
    }
}
