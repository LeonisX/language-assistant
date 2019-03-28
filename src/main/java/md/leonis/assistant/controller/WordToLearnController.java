package md.leonis.assistant.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import md.leonis.assistant.domain.ScriptWord;
import md.leonis.assistant.domain.standard.WordFrequency;
import md.leonis.assistant.domain.standard.WordLevel;
import md.leonis.assistant.domain.standard.WordToLearn;
import md.leonis.assistant.service.SampleService;
import md.leonis.assistant.view.FxmlView;
import md.leonis.assistant.view.StageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class WordToLearnController {

    @FXML
    public TableView<ScriptWord> wordsTable;

    @FXML
    public TableColumn<ScriptWord, String> wordColumn;
    @FXML
    public TableColumn<ScriptWord, String> levelColumn;
    @FXML
    public TableColumn<ScriptWord, String> frequencyColumn;
    @FXML
    public TableColumn<ScriptWord, String> xColumn;

    private ObservableList<ScriptWord> wordData = FXCollections.observableArrayList();

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private SampleService sampleService;

    @FXML
    private void initialize() {
        initData();

        wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        //transcrColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
        //levelColumn.setCellValueFactory(word -> new SimpleStringProperty(word.getValue().getLevel().getTitle()));
        levelColumn.setCellValueFactory(word -> new SimpleStringProperty(
                word.getValue().getLevel() == null ? "" : word.getValue().getLevel().name()));
        levelColumn.sortTypeProperty();
        frequencyColumn.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        xColumn.setCellValueFactory(new PropertyValueFactory<>("x"));

        wordsTable.setItems(wordData);
    }

    //TODO from DB
    private void initData() {
        List<WordToLearn> wordsToLearn = sampleService.getWordsToLearn();
        List<ScriptWord> scriptWords = wordsToLearn.stream().map(wb -> {
            String word = wb.getWord().toLowerCase();
            WordLevel wordLevel = sampleService.getWordLevel(word);
            WordFrequency wordFrequency = sampleService.getWordFrequency(word);
            return new ScriptWord(wb.getWord(), wordLevel.getLevel(), wordFrequency.getFrequency(), "");
        }).collect(Collectors.toList());
        wordData.addAll(scriptWords);
        /*wordData.add(new ScriptWord("select", LanguageLevel.A1, 4, ""));
        wordData.add(new ScriptWord("before", LanguageLevel.A2, 56, ""));
        wordData.add(new ScriptWord("daughter", LanguageLevel.C1, 66, ""));
        wordData.add(new ScriptWord("the", LanguageLevel.C2, 7, ""));
        wordData.add(new ScriptWord("add", LanguageLevel.B2, 1, ""));
        wordData.add(new ScriptWord("mirror", LanguageLevel.B1P, 99, ""));*/
    }

    public void startToLearnClick() {
        stageManager.showNewWindow(FxmlView.LEARN_WORDS);
    }
}
