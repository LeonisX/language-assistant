package md.leonis.assistant.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.web.WebView;
import md.leonis.assistant.service.SampleService;
import md.leonis.assistant.view.FxmlView;
import md.leonis.assistant.view.StageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

@Controller
public class SampleController {

    @Autowired
    private SampleService sampleService;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Value("${my.url}")
    private String myUrl;

    @FXML
    private WebView myWebView;

    @FXML
    private Button nextWindowButton;

    @FXML
    private void initialize() {
        myWebView.getEngine().load(myUrl);
        sampleService.echo();
    }

    @FXML
    private void handleNextWindowButtonAction(ActionEvent event) {
        // Button was clicked, show next window
        stageManager.switchScene(FxmlView.USER);

        stageManager.showNewWindow(FxmlView.USER);

    }
}
