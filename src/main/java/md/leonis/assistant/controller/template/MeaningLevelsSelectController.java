package md.leonis.assistant.controller.template;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import md.leonis.assistant.utils.ListenerHandles;
import md.leonis.assistant.view.StageManager;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MeaningLevelsSelectController extends HBox {

    @FXML
    private CheckBox l1CheckBox;
    @FXML
    private CheckBox l2CheckBox;
    @FXML
    private CheckBox l3CheckBox;
    @FXML
    private CheckBox l4CheckBox;
    @FXML
    private CheckBox l5CheckBox;

    @FXML
    private HBox containerHBox;

    @FXML
    private Button selectAllButton;

    private StageManager stageManager;

    private ObservableSet<Integer> selectedMeaningLevels;

    private Set<CheckBox> checkBoxes;

    private ListenerHandles selectedMeaningLevelsListenerHandles;

    private static final Set<Integer> ALL_SKILL_LEVELS = new HashSet<>(Arrays.asList(1,2,3,4,5));

    public MeaningLevelsSelectController(StageManager stageManager) {
        this.stageManager = stageManager;
        this.selectedMeaningLevels = FXCollections.observableSet(ALL_SKILL_LEVELS);
        this.selectedMeaningLevelsListenerHandles = new ListenerHandles(selectedMeaningLevels);

        stageManager.loadTemplate("meaningLevelsSelect", this, () ->
                checkBoxes = containerHBox.getChildren().stream()
                .filter(n -> n instanceof CheckBox)
                .map(n -> (CheckBox) n)
                .peek(c -> c.setSelected(true))
                .peek(c -> c.setOnAction(this::filterCheckBoxClick))
                .collect(Collectors.toSet()));
    }

    private void filterCheckBoxClick(ActionEvent actionEvent) {
        updateSelectedList((CheckBox) actionEvent.getSource());
    }

    public void selectAllButtonClick() {
        selectedMeaningLevelsListenerHandles.disableListeners();
        checkBoxes.forEach(c -> c.setSelected(true));
        selectedMeaningLevels.addAll(ALL_SKILL_LEVELS);
        selectedMeaningLevelsListenerHandles.enableAndNotifyListeners();
    }

    private void updateSelectedList(CheckBox checkBox) {
        Integer skillLevel = getSkillLevel(checkBox);
        if (selectedMeaningLevels.contains(skillLevel)) {
            selectedMeaningLevels.remove(skillLevel);
        } else {
            selectedMeaningLevels.add(skillLevel);
        }
    }

    private Integer getSkillLevel(CheckBox checkBox) {
        return Integer.valueOf(checkBox.getId().substring(1, 2));
    }

    public ObservableSet<Integer> getSelectedMeaningLevels() {
        return selectedMeaningLevels;
    }

    public ListenerHandles getSelectedMeaningLevelsListenerHandles() {
        return selectedMeaningLevelsListenerHandles;
    }

    public Button getSelectAllButton() {
        return selectAllButton;
    }
}
