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

        /*myWebView.getEngine().setOnAlert((WebEvent<String> wEvent) -> {
            System.out.println("JS alert() message: " + wEvent.getData() );
        });

        myWebView.getEngine().setOnError((WebErrorEvent wEvent) -> {
            System.out.println("JS alert() message: " + wEvent.getMessage() );
        });*/


        https://stackoverflow.com/questions/44905264/cannot-sign-in-to-google-in-javafx-webview
        https://stackoverflow.com/questions/51167203/android-webview-google-login-not-working
        https://stackoverflow.com/questions/44905264/cannot-sign-in-to-google-in-javafx-webview
        https://stackoverflow.com/questions/42721593/will-google-oauth-continue-to-work-with-javafx-webview

        com.sun.javafx.webkit.WebConsoleListener.setDefaultListener(
                (webView, message, lineNumber, sourceId) ->
                        System.out.println("Console: [" + sourceId + ":" + lineNumber + "] " + message)
        );

        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");

        myWebView.getEngine().load(configHolder.getCurrentVideo().getUrl());
    }

}
