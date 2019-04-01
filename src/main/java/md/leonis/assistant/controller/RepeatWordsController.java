package md.leonis.assistant.controller;

import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import md.leonis.assistant.config.ConfigHolder;
import md.leonis.assistant.controller.template.LevelsSelectController;
import md.leonis.assistant.domain.LanguageLevel;
import md.leonis.assistant.domain.test.Dictionary;
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

    public VBox vBox;
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

    private List<Ar> ars;

    @FXML
    private void initialize() {
        //TODO from SourceFactory
        Set<LanguageLevel> levels = gseSourceFactory.getLanguageLevelsSet();
        levelsSelectController = new LevelsSelectController(stageManager, configHolder, levels);
        levelsSelectController.getSelectAllButton().setOnAction(event -> selectAllClick());
        selectedLevels = levelsSelectController.getSelectedLevels();
        selectedLevels.addListener((SetChangeListener<LanguageLevel>) observable -> onSelectedListChange());
        vBox.getChildren().add(levelsSelectController);

        initDictionary();
    }

    //TODO get user word bank
    private void initDictionary() {
        Dictionary dictionary = testService.getDictionaries().get(0);
        ars = testService.getDictionary(new File(dictionary.getPath())).getAr();
    }

    public void selectAllClick() {
        selectedLevels.removeListener((SetChangeListener<LanguageLevel>) observable -> onSelectedListChange());
        levelsSelectController.selectAllButtonClick();
        selectedLevels.addListener((SetChangeListener<LanguageLevel>) observable -> onSelectedListChange());
        refresh();
    }

    private void onSelectedListChange() {
        refresh();
    }

    private void refresh() {
        //TODO refresh all labels, filter words
    }

    public void studyNowButtonClick() {
        //TODO
        throw new UnsupportedOperationException();
    }
}
