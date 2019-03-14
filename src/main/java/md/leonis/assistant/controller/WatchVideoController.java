package md.leonis.assistant.controller;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;
import md.leonis.assistant.view.StageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

@Controller
public class WatchVideoController {

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Value("${my.url}")
    private String myUrl;

    @FXML
    private WebView myWebView;

    @FXML
    private void initialize() {
        myWebView.getEngine().load(myUrl);
    }

}
