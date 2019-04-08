package md.leonis.assistant.controller.template;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import md.leonis.assistant.config.ConfigHolder;
import md.leonis.assistant.domain.Video;
import md.leonis.assistant.utils.VideoUtils;
import md.leonis.assistant.view.FxmlView;
import md.leonis.assistant.view.StageManager;

import java.io.IOException;

public class VideoTemplateController extends HBox {

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

    private StageManager stageManager;

    private ConfigHolder configHolder;

    private Video video;

    public VideoTemplateController(StageManager stageManager, ConfigHolder configHolder, Video video) {
        this.stageManager = stageManager;
        this.configHolder = configHolder;
        this.video = video;

        stageManager.loadTemplate("videoTemplate", this, () -> {
            title.setText(video.getTitle());
            author.setText(video.getAuthor());
            categories.setText(video.getCategories().toString());
            tags.setText(video.getAverageWordLevel() + video.getSoundQuality() + video.getSpeechSpeed() + video.getAccent());
            rating.setText(video.getRating().toString());

            String youtubeId = VideoUtils.getYoutubeVideoId(video.getUrl());
            Image image = new Image(VideoUtils.getYouTubeThumbnail(youtubeId));
            previewImage.setImage(image);
        });
    }

    public void studyScriptClick(ActionEvent actionEvent) {
        //stageManager.showWarningAlert("Attention please", "This functionality is not ready yet", "Please be patient");
        stageManager.showNewWindow(FxmlView.WATCH_SCRIPT);
    }

    public void watchClick(ActionEvent actionEvent) {
        configHolder.setCurrentVideo(video);
        stageManager.showNewWindow(FxmlView.WATCH_VIDEO);
    }
}
