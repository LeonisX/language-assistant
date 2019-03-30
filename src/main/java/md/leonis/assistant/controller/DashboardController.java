package md.leonis.assistant.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import md.leonis.assistant.config.ConfigHolder;
import md.leonis.assistant.domain.LanguageLevel;
import md.leonis.assistant.domain.User;
import md.leonis.assistant.domain.test.WordLevel;
import md.leonis.assistant.service.TestService;
import md.leonis.assistant.service.UserService;
import md.leonis.assistant.source.gse.GseService;
import md.leonis.assistant.view.FxmlView;
import md.leonis.assistant.view.StageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class DashboardController {

    @FXML
    public TextArea infoTextArea;

    public HBox gseHBox;
    public Label gseLabel;
    public Button gseCrawlButton;
    public Button gseParseButton;

    public HBox wordListHBox;
    public Label wordListLabel;
    public Button wordListImportButton;

    public HBox userWordBankHBox;
    public Label userWordBankLabel;
    public Button userWordBankGenerateButton;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private ConfigHolder configHolder;

    @Autowired
    private GseService gseService;

    @Autowired
    private UserService userService;

    @Autowired
    private TestService testService;

    @FXML
    private void initialize() {
        User user = configHolder.getUser();

        //TODO link to page with description
        //TODO user.getWords() ???
        String level = LanguageLevel.getTitle(user.getWords());

        String text = String.format("%s\n" +
                "\n" +
                "\n" +
                "Уровень: %s\n" +
                "\n" +
                "Словарный запас: %d\n" +
                "\n" +
                "Подтвержденные слова:\n" +
                "- чтение: %d\n" +
                "- написание: %d\n" +
                "- на слух: %d\n" +
                "\n" +
                "\n" +
                "Специализация: программист\n" +
                "\n" +
                "Счёт: %d\n" +
                "\n" +
                "Тут роза \n" +
                "(ядро + специальности)", user.getName(), level, user.getWords(), user.getReadWords(), user.getWriteWords(), user.getTestWords(), user.getScore());

        infoTextArea.setText(text);

        refreshDiagnosticControls();
    }

    public void wordBankShow() {
        stageManager.showNewWindow(FxmlView.WORD_BANK);
    }

    public void watchVideoShow() {
        stageManager.showNewWindow(FxmlView.VIDEO_LIST);
    }

    public void onDictionaryClick() {
        stageManager.showNewWindow(FxmlView.DICTIONARY);
    }

    public void onImportDictionaryClick() {
        stageManager.showNewWindow(FxmlView.DICTIONARY_IMPORTER);
    }

    public void wordToLearnShow() {
        stageManager.showNewWindow(FxmlView.WORD_TO_LEARN);
    }

    public void learnWordsClick() {
        stageManager.showNewWindow(FxmlView.LEARN_WORDS);
    }

    private void refreshDiagnosticControls() {
        //gseHBox.setVisible(!gseService.isCrawled() || !gseService.isParsed());
        gseLabel.setText(String.format("Data Source Status: %s", gseService.getStatus()));
        gseCrawlButton.setVisible(!gseService.isCrawled());
        gseParseButton.setVisible(!gseService.isParsed());

        long wordLevelCount = testService.getWordLevelCount();
        wordListLabel.setText(String.format("Word Level Count: %d", wordLevelCount));
        wordListImportButton.setVisible(wordLevelCount == 0);

        //TODO method for count
        long userWordBankCount = userService.getUserWordBank().size();
        userWordBankLabel.setText(String.format("User Word Bank Count: %d", userWordBankCount));
        userWordBankGenerateButton.setVisible(userWordBankCount == 0);
    }

    public void gseCrawlButtonClick() {
        try {
            gseService.getCrawler().crawl();
            stageManager.showInformationAlert("Crawled", String.format("Raw Count: %d", gseService.getRawCount()), "");
        } catch (Exception e) {
            stageManager.showErrorAlert("Crawler Error", e.getMessage(), "");
        }
        refreshDiagnosticControls();
    }

    public void gseParseButtonClick() {
        try {
            gseService.getParser().parse();
            stageManager.showInformationAlert("Parsed", String.format("Parsed Raw Data Count: %d", gseService.getRawDataCount()), "");
        } catch (Exception e) {
            stageManager.showErrorAlert("Parser Error", e.getMessage(), "");
        }
        refreshDiagnosticControls();
    }

    public void wordListImportButtonClick() {
        try {
            List<WordLevel> wordLevels = gseService.getWordLevels();
            testService.saveWordLevels(wordLevels);
            stageManager.showInformationAlert("Word Levels Imported", String.format("Word Levels Count: %d", wordLevels.size()), "");
        } catch (Exception e) {
            stageManager.showErrorAlert("Word Levels Import Error", e.getMessage(), "");
        }
        refreshDiagnosticControls();
    }

    public void userWordBankGenerateClick() {
        try {
            userService.generateUserWordBank(gseService.findAllWords());
            //TODO method for count
            stageManager.showInformationAlert("User Word Bank Generated", String.format("User Word Bank: %d", userService.getUserWordBank().size()), "");
        } catch (Exception e) {
            stageManager.showErrorAlert("User Word Bank Generation Error", e.getMessage(), "");
        }
        refreshDiagnosticControls();
    }
}
