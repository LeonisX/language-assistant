package md.leonis.assistant.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import md.leonis.assistant.config.ConfigHolder;
import md.leonis.assistant.controller.template.VideoTemplateController;
import md.leonis.assistant.view.StageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

@Controller
public class VideoListController {

    @FXML
    public Label title;

    @FXML
    public Label author;

    @FXML
    public Label categories;

    @FXML
    public Label tags;

    @FXML
    public Label rating;

    @FXML
    public ImageView previewImage;

    @FXML
    public HBox container;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private ConfigHolder configHolder;

    @Value("${my.url}")
    private String myUrl;

    @FXML
    private WebView myWebView;

    @FXML
    private void initialize() {

        configHolder.getVideos().forEach(video ->
                container.getChildren().add(new VideoTemplateController(stageManager, configHolder, video)));


        /*Video video = configHolder.getVideos().get(0);

        title.setText(video.getTitle());
        author.setText(video.getAuthor());
        categories.setText(video.getCategories().toString());
        tags.setText(video.getAverageWordLevel() + video.getSoundQuality() + video.getSpeechSpeed() + video.getAccent());
        rating.setText(video.getRating().toString());

        String youtubeId = VideoUtils.getYoutubeVideoId(video.getUrl());
        Image image = new Image(VideoUtils.getYouTubeThumbnail(youtubeId));
        previewImage.setImage(image);*/
    }

    /*public void studyScriptClick(ActionEvent actionEvent) {
        stageManager.showWarningAlert("Attention please", "This functionality is not ready yet", "Please be patient");
    }

    public void watchClick(ActionEvent actionEvent) {
        configHolder.setCurrentVideo(configHolder.getVideos().get(0));
        stageManager.showNewWindow(FxmlView.WATCH_VIDEO);
    }*/
}
