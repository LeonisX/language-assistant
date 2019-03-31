package md.leonis.assistant.controller.template;

import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import md.leonis.assistant.config.ConfigHolder;
import md.leonis.assistant.domain.LanguageLevel;
import md.leonis.assistant.view.StageManager;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class LevelsSelectController extends HBox {

    @FXML
    private HBox containerHBox;

    //TODO delete
    private StageManager stageManager;

    //TODO delete
    private ConfigHolder configHolder;

    private Set<LanguageLevel> levels;

    private Set<LanguageLevel> selectedLevels;

    private Set<CheckBox> checkBoxes;

    public LevelsSelectController(StageManager stageManager, ConfigHolder configHolder, Set<LanguageLevel> levels, ObservableSet<LanguageLevel> selectedLevels) {
        this.stageManager = stageManager;
        this.configHolder = configHolder;
        this.levels = levels;
        this.selectedLevels = selectedLevels;

        //TODO in stageManager
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/template/levelsSelectTemplate.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();

            checkBoxes = containerHBox.getChildren().stream()
                    .map(n -> (CheckBox) n)
                    .peek(c -> {
                        LanguageLevel languageLevel = getLevel(c);
                        c.setSelected(selectedLevels.contains(languageLevel));
                        c.setVisible(levels.contains(languageLevel));
                        c.managedProperty().bind(c.visibleProperty());
                    })
                    .filter(c -> levels.contains(getLevel(c)))
                    .peek(c -> c.setOnAction(this::filterCheckBoxClick))
                    .collect(Collectors.toSet());

        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    private void filterCheckBoxClick(ActionEvent actionEvent) {
        updateSelectedList((CheckBox) actionEvent.getSource());
    }

    public void showAllClick() {
        checkBoxes.forEach(c -> c.setSelected(true));
        selectedLevels.addAll(levels);
    }

    private void updateSelectedList(CheckBox checkBox) {
        LanguageLevel languageLevel = getLevel(checkBox);
        if (selectedLevels.contains(languageLevel)) {
            selectedLevels.remove(languageLevel);
        } else {
            selectedLevels.add(languageLevel);
        }
    }

    private LanguageLevel getLevel(CheckBox checkBox) {
        return LanguageLevel.valueOf(checkBox.getId().replace("CheckBox", "").toUpperCase());
    }

}
