package md.leonis.assistant.controller;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
    private ObservableSet<LanguageLevel> selectedLevels;

    private ShowCardsController showCardsController;

    private ObservableList<UserWordBank> changedWords;

    private List<Ar> ars;

    private Set<LanguageLevel> levels;

    private ListChangeListener<UserWordBank> changeListener;

    private SetChangeListener<LanguageLevel> setChangeListener;

    @FXML
    private void initialize() {
        //TODO from SourceFactory
        setChangeListener = c -> onSelectedListChange();
        levels = gseSourceFactory.getLanguageLevelsSet();
        levelsSelectController = new LevelsSelectController(stageManager, configHolder, levels);
        levelsSelectController.getSelectAllButton().setOnAction(event -> selectAllClick());
        selectedLevels = levelsSelectController.getSelectedLevels();
        selectedLevels.addListener(setChangeListener);
        topVBox.getChildren().add(levelsSelectController);

        initDictionary();
    }

    public void selectAllClick() {
        selectedLevels.removeListener(setChangeListener);
        levelsSelectController.selectAllButtonClick();
        selectedLevels.addListener(setChangeListener);
        refresh();
    }

    private void onSelectedListChange() {
        refresh();
    }

    private void refresh() {
        //TODO refresh all labels, filter words
    }

    public void studyNowButtonClick() {
        //TODO in learn words
        //List<UserWordBank> userWordBank = userService.getWordsToLearn(20);
        //if (userWordBank.size() < 20) {
        //TODO here - get all. In learn words - only older than now()
        List<UserWordBank> userWordBank = userService.getWordsToRepeat(20);
        //}

        if (!userWordBank.isEmpty()) {
            showCardsController = new ShowCardsController(stageManager, configHolder, ars, userWordBank);
            //TODO
            //showCardsController.getSelectAllButton().setOnAction(event -> selectAllClick());
            changedWords = showCardsController.getChangedWords();

            changeListener = c -> onChangedWordsModify();
            changedWords.addListener(changeListener);
            topVBox.getChildren().clear();
            centerVBox.getChildren().clear();
            centerVBox.getChildren().add(showCardsController);
        } else {
            stageManager.showWarningAlert("Nothing to learn!",  "",  "");
        }
    }

    private void onChangedWordsModify() {
        log.info("1");
        changedWords.removeListener(changeListener);
        changedWords.forEach(word -> {
            userService.saveUserWordBank(word);
            log.info("Updated: {}", word);
        });
        changedWords.clear();
        changedWords.addListener(changeListener);
        log.info("2");
    }



    //TODO unify
    //TODO one dictionary for APP
    private void initDictionary() {
        //TODO select right dictionary, not Mueller only
        //TODO need to be sure that we have at once 1 dictionary
        Dictionary dictionary = testService.getDictionaries().get(0);
        //TODO new File in service
        //TODO may be convert all dictionaries to DB
        ars = testService.getDictionary(new File(dictionary.getPath())).getAr();
    }
}
