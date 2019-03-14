package md.leonis.assistant.view;

import java.util.ResourceBundle;

public enum FxmlView {

    SPLASH("splash.title", "splash"),
    DASHBOARD("dashboard.title", "dashboard"),
    WORD_BANK("wordBank.title", "wordBank"),
    WATCH_VIDEO("watchVideo.title", "watchVideo"),

    LOGIN("login.title", "sample");

    private static String fxmlPath = "/fxml/%s.fxml";

    private String title;
    private String fxmlFileName;

    FxmlView(String title, String fxmlFileName) {
        this.title = title;
        this.fxmlFileName = fxmlFileName;
    }

    //TODO this bundle is cached?
    public String getTitle() {
        return ResourceBundle.getBundle("Bundle").getString(title);
    }

    public String getFxmlFile() {
        return String.format(fxmlPath, fxmlFileName);
    }

}