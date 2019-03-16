package md.leonis.assistant.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.util.Duration;
import md.leonis.assistant.view.FxmlView;
import md.leonis.assistant.view.StageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

@Controller
public class SplashController {

    @Lazy
    @Autowired
    private StageManager stageManager;

    @FXML
    private void initialize() {
        //TODO switch on
        //PauseTransition delay = new PauseTransition(Duration.seconds(1));
        PauseTransition delay = new PauseTransition(Duration.millis(1));
        //TODO boolean - need to wait all processes
        delay.setOnFinished(event -> stageManager.switchScene(FxmlView.DASHBOARD));
        delay.play();
    }
}
