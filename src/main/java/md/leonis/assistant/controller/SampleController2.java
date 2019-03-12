package md.leonis.assistant.controller;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;
import md.leonis.assistant.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

@Controller
public class SampleController2 {

    @Autowired
    SampleService sampleService;

    @FXML
    private WebView myWebView;

    @Value("${my.url}")
    private String myUrl;

    @FXML
    private void initialize() {
        myWebView.getEngine().load(myUrl);
        sampleService.echo();
    }
}
