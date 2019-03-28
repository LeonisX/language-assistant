package md.leonis.assistant.view;

import java.util.ResourceBundle;

public enum FxmlView {

    SPLASH("splash.title", "splash"),
    DASHBOARD("dashboard.title", "dashboard"),
    WORD_BANK("wordBank.title", "wordBank"),
    VIDEO_LIST("videoList.title", "videoList"),
    WATCH_SCRIPT("watchScript.title", "watchScript"),
    WATCH_VIDEO("watchVideo.title", "watchVideo"),
    DICTIONARY("dictionary.title", "dictionary"),
    DICTIONARY_IMPORTER("dictionaryImporter.title", "dictionaryImporter"),
    WORD_TO_LEARN("wordToLearn.title", "wordToLearn"),
    LEARN_WORDS("learnWords.title", "learnWords");

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
