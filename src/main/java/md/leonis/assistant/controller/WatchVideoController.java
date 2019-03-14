package md.leonis.assistant.controller;

import javafx.fxml.FXML;
import md.leonis.assistant.view.StageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

@Controller
public class WatchVideoController {

    @Lazy
    @Autowired
    private StageManager stageManager;

    @FXML
    private void initialize() {
    }

}
