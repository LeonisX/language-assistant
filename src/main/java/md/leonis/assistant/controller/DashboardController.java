package md.leonis.assistant.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import md.leonis.assistant.config.ConfigHolder;
import md.leonis.assistant.domain.LanguageLevel;
import md.leonis.assistant.domain.User;
import md.leonis.assistant.view.FxmlView;
import md.leonis.assistant.view.StageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

@Controller
public class DashboardController {

    @FXML
    public TextArea infoTextArea;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private ConfigHolder configHolder;

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
}
