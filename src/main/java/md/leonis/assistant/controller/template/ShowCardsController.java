package md.leonis.assistant.controller.template;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import md.leonis.assistant.config.ConfigHolder;
import md.leonis.assistant.domain.user.MemorizationLevel;
import md.leonis.assistant.domain.user.UserWordBank;
import md.leonis.assistant.domain.xdxf.lousy.Ar;
import md.leonis.assistant.view.StageManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ShowCardsController extends BorderPane {

    @FXML private HBox questionHBox;
    @FXML private HBox answerHBox;

    @FXML private WebView questionWebView;
    @FXML private WebView answerWebView;

    @FXML private Label memorizedLabel;
    @FXML private Label leftLabel;
    @FXML private Label totalLabel;

    private List<Ar> ars;

    private boolean questionMode;

    private List<UserWordBank> userWordBank;

    private UserWordBank currentWord;

    //TODO delete
    private StageManager stageManager;

    //TODO delete
    private ConfigHolder configHolder;

    private int memorizedCount;

    private Consumer<UserWordBank> saveWordConsumer;

    private int meaningLevelIncrement;

    public ShowCardsController(StageManager stageManager, ConfigHolder configHolder, List<Ar> ars, List<UserWordBank> userWordBank, Consumer<UserWordBank> consumer) {
        this(stageManager,configHolder, ars, userWordBank, consumer, 0);
    }

    //TODO need answers
    public ShowCardsController(StageManager stageManager, ConfigHolder configHolder, List<Ar> ars, List<UserWordBank> userWordBank, Consumer<UserWordBank> consumer, int meaningLevelIncrement) {
        this.stageManager = stageManager;
        this.configHolder = configHolder;
        this.userWordBank = userWordBank;
        this.ars = ars;
        this.saveWordConsumer = consumer;
        this.meaningLevelIncrement = meaningLevelIncrement;

        stageManager.loadTemplate("showCards", this, () -> {});

        questionHBox.managedProperty().bind(questionHBox.visibleProperty());
        answerHBox.managedProperty().bind(answerHBox.visibleProperty());

        questionMode = false;
        currentWord = userWordBank.get(0);

        memorizedCount = 0;
        totalLabel.setText(Integer.toString(userWordBank.size()));

        switchState();
    }

    private void switchState() {
        memorizedLabel.setText(Integer.toString(memorizedCount));
        leftLabel.setText(Integer.toString(userWordBank.size()));

        if (currentWord == null) {
            //TODO may be show statistics (time)
            stageManager.showInformationAlert("Well done", "You have finished repeating the words.", "");
            ((Stage) questionHBox.getScene().getWindow()).close();
            return;
        }
        questionMode = !questionMode;

        questionHBox.setVisible(questionMode);
        answerWebView.setVisible(!questionMode);
        answerHBox.setVisible(!questionMode);
        showQuestion();
        showAnswer();
    }

    public void showAnswerButtonClick() {
        switchState();
    }

    public void dontRememberButtonClick() {
        indicateWordKnowledge(MemorizationLevel.UNKNOWN);
    }

    public void rememberButtonClick() {
        indicateWordKnowledge(MemorizationLevel.HARD_REMEMBERED);
    }

    public void knowButtonClick() {
        indicateWordKnowledge(MemorizationLevel.KNOWN);
    }

    public void veryEasyButtonClick() {
        indicateWordKnowledge(MemorizationLevel.NATIVE);
    }

    private void indicateWordKnowledge(MemorizationLevel memorizationLevel) {
        UserWordBank firstWord = userWordBank.remove(0);
        firstWord.setStatus(memorizationLevel);
        //TODO more generic? timeUnit in MemorizationLevel?
        switch (memorizationLevel) {
            case UNKNOWN:
                firstWord.setRepeatTime(LocalDateTime.now().plusMinutes(0));
                break;
            case HARD_REMEMBERED:
                firstWord.setRepeatTime(LocalDateTime.now().plusMinutes(1));
                break;
            case KNOWN:
                firstWord.setRepeatTime(LocalDateTime.now().plusDays(1));
                break;
            case NATIVE:
                firstWord.setRepeatTime(LocalDateTime.now().plusDays(10));
                break;
        }
        if (memorizationLevel.ordinal() <= 1) { // repeat
            userWordBank.add(firstWord);
        } else {
            memorizedCount++;
            firstWord.setLevel(firstWord.getLevel() + meaningLevelIncrement);
            if (firstWord.getLevel() == 0) {
                firstWord.setLevel(1);
            }
        }
        saveWordConsumer.accept(firstWord);
        if (userWordBank.isEmpty()) {
            currentWord = null;
        } else {
            currentWord = userWordBank.get(0);
        }
        switchState();
    }

    private void showQuestion() {
        String content = "";
        if (currentWord != null) {
            content = String.format("%s (%s meaning(s))", currentWord.getWord(), currentWord.getLevel() + meaningLevelIncrement);
        }
        questionWebView.getEngine().loadContent(content);
    }

    private void showAnswer() {
        if (currentWord != null) {
            if (currentWord.getWord().trim().isEmpty()) {
                answerWebView.getEngine().loadContent("");
                return;
            }
            String answer = findAnswer(currentWord.getWord());

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
            answerWebView.getEngine().loadContent("<Sorry, there is no answer>");
        }
    }

    private String findAnswer(String word) {
        return ars.stream().filter(ar -> ar.getK().equalsIgnoreCase(word))
                //TODO checks in AR
                .map(ar -> ar.getTr() == null ? "" : ar.getTr() + "\n" + ar.getFullValue()).collect(Collectors.joining("\n\n"));
    }
}
