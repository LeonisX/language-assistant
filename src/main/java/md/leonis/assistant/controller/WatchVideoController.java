package md.leonis.assistant.controller;

import javafx.fxml.FXML;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import md.leonis.assistant.config.ConfigHolder;
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

    @Autowired
    private ConfigHolder configHolder;

    /*@Value("${my.url}")
    private String myUrl;*/

    @FXML
    private WebView myWebView;

    @FXML
    private void initialize() {

        myWebView.getEngine().setOnAlert((WebEvent<String> wEvent) -> {
            System.out.println("JS alert() message: " + wEvent.getData() );
        });

        myWebView.getEngine().setOnError((WebErrorEvent wEvent) -> {
            System.out.println("JS alert() message: " + wEvent.getMessage() );
        });


        myWebView.getEngine().load(configHolder.getCurrentVideo().getUrl());
    }

}
