package md.leonis.assistant.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import md.leonis.assistant.config.ConfigHolder;
import md.leonis.assistant.controller.template.LevelsSelectController;
import md.leonis.assistant.controller.template.ShowCardsController;
import md.leonis.assistant.domain.LanguageLevel;
import md.leonis.assistant.domain.test.Dictionary;
import md.leonis.assistant.domain.user.UserWordBank;
import md.leonis.assistant.domain.xdxf.lousy.Ar;
import md.leonis.assistant.service.TestService;
import md.leonis.assistant.service.UserService;
import md.leonis.assistant.source.gse.GseSourceFactory;
import md.leonis.assistant.view.StageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.util.List;
import java.util.Set;

@Controller
public class RepeatWordsController {

    private static final Logger log = LoggerFactory.getLogger(RepeatWordsController.class);

    public VBox topVBox;
    public VBox centerVBox;
    public HBox hBox;

    public Label wordBankLabel;
    public Label selectedWordsLabel;
    public Label learnCountLabel;

    public TableView<UserWordBank> wordsTableView;
    public TableColumn<UserWordBank, String> wordColumn;
    public TableColumn<UserWordBank, String> levelColumn;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private ConfigHolder configHolder;

    @Autowired
    private TestService testService;

    @Autowired
    private UserService userService;

    @Autowired
    //TODO generic
    private GseSourceFactory gseSourceFactory;

    private LevelsSelectController levelsSelectController;

    private List<Ar> ars;

    private List<UserWordBank> wordsToRepeat;

    @FXML
    private void initialize() {
        Set<LanguageLevel> levels = gseSourceFactory.getLanguageLevelsSet();
        levelsSelectController = new LevelsSelectController(stageManager, configHolder, levels);
        levelsSelectController.getSelectAllButton().setOnAction(event -> selectAllClick());
        levelsSelectController.getSelectedLevels().addListener(this::onSelectedListChange);
        topVBox.getChildren().add(levelsSelectController);

        initDictionary();

        //todo for learn getWordsToLearnCount
        wordBankLabel.setText(userService.getWordsToRepeatCount().toString());

        wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        levelColumn.setCellValueFactory(word -> new SimpleStringProperty(word.getValue().getWordLevel().name()));

        refresh();
    }

    //TODO unify; one dictionary for APP
    private void initDictionary() {
        Dictionary dictionary = testService.getDictionaries().get(0);
        ars = testService.getDictionary(new File(dictionary.getPath())).getAr();
    }

    private void selectAllClick() {
        levelsSelectController.getSelectedLevels().addListener(this::onSelectedListChange);
        levelsSelectController.selectAllButtonClick();
        levelsSelectController.getSelectedLevels().addListener(this::onSelectedListChange);
        refresh();
    }

    private void onSelectedListChange(SetChangeListener.Change<? extends LanguageLevel> change) {
        refresh();
    }

    private void refresh() {
        Set<LanguageLevel> selectedLevels = levelsSelectController.getSelectedLevels();
        //TODO in learn words
        //List<UserWordBank> userWordBank = userService.getWordsToLearn(20);
        //if (userWordBank.size() < 20) {
        //TODO here - get all. In learn words - only older than now()
        wordsToRepeat = userService.getWordsToRepeat(configHolder.getWordsToLearnCount(), selectedLevels);
        learnCountLabel.setText(Integer.toString(wordsToRepeat.size()));

        selectedWordsLabel.setText(Integer.toString(userService.getWordsToRepeat(selectedLevels).size()));

        wordsTableView.setItems(FXCollections.observableList(wordsToRepeat));
    }

    public void studyNowButtonClick() {
        if (!wordsToRepeat.isEmpty()) {
            ShowCardsController showCardsController =
                    new ShowCardsController(stageManager, configHolder, ars, wordsToRepeat, userService::saveUserWordBank);

            topVBox.getChildren().clear();
            centerVBox.getChildren().clear();
            centerVBox.getChildren().add(showCardsController);
        } else {
            stageManager.showWarningAlert("Nothing to learn!",  "",  "");
        }
    }
}

