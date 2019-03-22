package md.leonis.assistant.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import md.leonis.assistant.domain.standard.Dictionary;
import md.leonis.assistant.domain.xdxf.lousy.Xdxf;
import md.leonis.assistant.service.SampleService;
import md.leonis.assistant.view.StageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class DictionaryImporterController {

    @FXML
    public TableView<Dictionary> dictionariesTable;

    @FXML
    public TableColumn<Dictionary, String> directionColumn;
    @FXML
    public TableColumn<Dictionary, String> titleColumn;

    public Button removeButton;
    public Button removeOrphanedButton;
    public TableColumn<Dictionary, Long> idColumn;
    public TableColumn<Dictionary, String> revisionColumn;
    public TableColumn<Dictionary, String> formatColumn;
    public TableColumn<Dictionary, String> sizeColumn;
    public TableColumn<Dictionary, Integer> recordsCountColumn;

    @Autowired
    private SampleService sampleService;

    @Lazy
    @Autowired
    private StageManager stageManager;

    private BooleanProperty isNotSelectedRow = new SimpleBooleanProperty(true);

    private static String lastVisitedDirectory = System.getProperty("user.home");

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        revisionColumn.setCellValueFactory(new PropertyValueFactory<>("revision"));
        formatColumn.setCellValueFactory(new PropertyValueFactory<>("format"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        sizeColumn.setCellValueFactory(d -> new SimpleStringProperty(Long.toString(d.getValue().getSize() / 1024)));
        recordsCountColumn.setCellValueFactory(new PropertyValueFactory<>("recordsCount"));


        directionColumn.setCellValueFactory(new PropertyValueFactory<>("k"));
        directionColumn.setComparator(String::compareToIgnoreCase);
        directionColumn.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getLangFrom() + " -> " + d.getValue().getLangTo()));

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        //titleColumn.setComparator(null);
        //xColumn.setCellValueFactory(new PropertyValueFactory<>("x"));

        titleColumn.setCellFactory(tc -> {
            TableCell<Dictionary, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(titleColumn.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });

        initData();

        dictionariesTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) ->
                isNotSelectedRow.setValue(dictionariesTable.getSelectionModel().getSelectedItem() == null));

        removeButton.disableProperty().bind(isNotSelectedRow);
    }

    private void initData() {
        ObservableList<Dictionary> observableList = FXCollections.observableArrayList(sampleService.getDictionaries());
        SortedList<Dictionary> dictionaries = new SortedList<>(observableList);
        dictionariesTable.setItems(dictionaries);
        dictionaries.comparatorProperty().bind(dictionariesTable.comparatorProperty());

        removeOrphanedButton.setDisable(dictionariesTable.getItems().isEmpty());
    }

    public void importClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(lastVisitedDirectory));
        fileChooser.setInitialFileName("dict.xdxf");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XDXF Dictionaries", "*.xdxf"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File file = fileChooser.showOpenDialog(dictionariesTable.getScene().getWindow());
        lastVisitedDirectory = (file != null) ? file.getParent() : System.getProperty("user.home");
        if (file != null) {
            if (dictionariesTable.getItems().stream().noneMatch(d -> d.getPath().equals(file.getAbsolutePath()))) {
                Xdxf xdxf = sampleService.getDictionary(file);
                Dictionary dictionary = xdxf.toDictionary(file);
                sampleService.saveDictionary(dictionary);
                initData();
            } else {
                stageManager.showWarningAlert("Already imported!", "", "");
            }
        }
    }

    public void removeClick() {
        if (!isNotSelectedRow.getValue()) {
            sampleService.deleteDictionary(dictionariesTable.getSelectionModel().getSelectedItem().getId());
            initData();
        }
    }

    public void onRemoveOrphaned() {
        List<Dictionary> orphaned = sampleService.getDictionaries().stream().filter(d -> !(new File(d.getPath()).exists())).collect(Collectors.toList());
        if (orphaned.isEmpty()) {
            stageManager.showWarningAlert("Nothing to delete", "All dictionaries are attached", "");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm to delete?");
        alert.setHeaderText("Delete these dictionaries?");
        alert.setContentText(orphaned.stream().map(Dictionary::getFullName).collect(Collectors.joining("\n")));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            sampleService.deleteAllDictionaries(orphaned);
        }
        initData();
    }
}
