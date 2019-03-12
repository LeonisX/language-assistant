package md.leonis.assistant;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

@Controller
public class SampleController {

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
