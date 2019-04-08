package md.leonis.assistant.controller.template;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import md.leonis.assistant.config.ConfigHolder;
import md.leonis.assistant.domain.user.MemorizationLevel;
import md.leonis.assistant.domain.user.UserWordBank;
import md.leonis.assistant.domain.xdxf.lousy.Ar;
import md.leonis.assistant.view.StageManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ShowCardsController extends BorderPane {

    @FXML private HBox questionHBox;
    @FXML private HBox answerHBox;

    @FXML private WebView questionWebView;
    @FXML private WebView answerWebView;

    private List<Ar> ars;

    private boolean questionMode;

    private List<UserWordBank> userWordBank;

    private UserWordBank currentWord;

    //TODO delete
    private StageManager stageManager;

    //TODO delete
    private ConfigHolder configHolder;

    private ObservableList<UserWordBank> changedWords;

    //TODO need answers
    public ShowCardsController(StageManager stageManager, ConfigHolder configHolder, List<Ar> ars, List<UserWordBank> userWordBank) {
        this.stageManager = stageManager;
        this.configHolder = configHolder;
        this.changedWords = FXCollections.observableArrayList();
        this.userWordBank = userWordBank;
        this.ars = ars;

        //TODO in stageManager
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/template/showCards.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }

        questionMode = false;
        currentWord = userWordBank.get(0);

        switchState();
    }

    private void switchState() {
        if (currentWord == null) {
            stageManager.showInformationAlert("Gata", "", "");
            Stage stage = (Stage) questionHBox.getScene().getWindow();
            // do what you have to do
            stage.close();
            return;
        }
        questionMode = !questionMode;
        //TODO show question
        /*showAnswerButton.setVisible(questionMode);
        dontRememberButton.setVisible(!questionMode);
        rememberButton.setVisible(!questionMode);
        knowButton.setVisible(!questionMode);
        veryEasyButton.setVisible(!questionMode);*/
        questionHBox.setVisible(questionMode);
        answerWebView.setVisible(!questionMode);
        answerHBox.setVisible(!questionMode);
        showQuestion(currentWord.getWord());
        showAnswer(currentWord.getWord());
    }

    public ObservableList<UserWordBank> getChangedWords() {
        return changedWords;
    }

    public void showAnswerButtonClick() {
        switchState();
    }

    public void dontRememberButtonClick() {
        UserWordBank firstWord = userWordBank.remove(0);
        firstWord.setRepeatTime(LocalDateTime.now().plusMinutes(0));
        firstWord.setStatus(MemorizationLevel.UNKNOWN);
        changedWords.clear();
        changedWords.add(firstWord);
        userWordBank.add(firstWord);
        currentWord = userWordBank.get(0);
        switchState();
    }

    public void rememberButtonClick() {
        UserWordBank firstWord = userWordBank.remove(0);
        firstWord.setRepeatTime(LocalDateTime.now().plusMinutes(1));
        firstWord.setStatus(MemorizationLevel.HARD_REMEMBERED);
        changedWords.clear();
        changedWords.add(firstWord);
        userWordBank.add(firstWord);
        currentWord = userWordBank.get(0);
        switchState();
    }

    public void knowButtonClick() {
        UserWordBank firstWord = userWordBank.remove(0);
        firstWord.setRepeatTime(LocalDateTime.now().plusDays(1));
        firstWord.setStatus(MemorizationLevel.KNOWN);
        changedWords.clear();
        changedWords.add(firstWord);
        if (userWordBank.isEmpty()) {
            currentWord = null;
        } else {
            currentWord = userWordBank.get(0);
        }
        switchState();
    }

    public void veryEasyButtonClick() {
        UserWordBank firstWord = userWordBank.remove(0);
        firstWord.setRepeatTime(LocalDateTime.now().plusDays(10));
        firstWord.setStatus(MemorizationLevel.NATIVE);
        changedWords.clear();
        changedWords.add(firstWord);
        if (userWordBank.isEmpty()) {
            currentWord = null;
        } else {
            currentWord = userWordBank.get(0);
        }
        switchState();
    }

    private void showQuestion(String word) {
        if (currentWord != null) {
            questionWebView.getEngine().loadContent(word);
        } else {
            questionWebView.getEngine().loadContent("");
        }
    }

    private void showAnswer(String word) {
        if (currentWord != null) {
            if (word.trim().isEmpty()) {
                answerWebView.getEngine().loadContent("");
                return;
            }
            String answer = findAnswer(word);

            //TODO need variances here???
        /*if (translation.isEmpty()) {
            translation = testService.getVariances(word).stream()
                    .map(v -> findTranslation(v.getWord())).filter(w -> !w.isEmpty()).collect(Collectors.joining("\n\n"));
        }*/
            //TODO if still empty - find online
            //TODO I need 100% working version of GoogleApi
        /*if (translation.isEmpty()) {
            translation = GoogleApi.translate(word);
            if (translation == null) {
                translation = "";
            }
        }*/
            answerWebView.getEngine().loadContent(answer);
        } else {
            answerWebView.getEngine().loadContent("");
        }
    }

    private String findAnswer(String word) {
        return ars.stream().filter(ar -> ar.getK().equalsIgnoreCase(word))
                //TODO checks in AR
                .map(ar -> ar.getTr() == null ? "" : ar.getTr() + "\n" + ar.getFullValue()).collect(Collectors.joining("\n\n"));
    }
}
