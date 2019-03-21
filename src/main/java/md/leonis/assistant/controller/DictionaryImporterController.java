package md.leonis.assistant.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
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

@Controller
public class DictionaryImporterController {

    @FXML
    public TableView<Dictionary> dictionariesTable;

    @FXML
    public TableColumn<Dictionary, String> directionColumn;
    @FXML
    public TableColumn<Dictionary, String> titleColumn;
    @FXML
    public TableColumn<Dictionary, String> otherColumn;

    public Button removeButton;

    private SortedList<Dictionary> dictionaries;

    @Autowired
    private SampleService sampleService;

    @Lazy
    @Autowired
    private StageManager stageManager;

    private BooleanProperty isSelectedRow = new SimpleBooleanProperty(false);

    @FXML
    private void initialize() {
        initData();

        //TODO id, revision, format, size, recordsCount

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
            return cell ;
        });

        otherColumn.setCellValueFactory(new PropertyValueFactory<>("recordsCount"));
        otherColumn.setComparator(null);
        otherColumn.sortTypeProperty();

        dictionaries.comparatorProperty().bind(dictionariesTable.comparatorProperty());

        dictionariesTable.setItems(dictionaries);

        dictionariesTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) ->
                isSelectedRow.setValue(dictionariesTable.getSelectionModel().getSelectedItem() != null));

        removeButton.disableProperty().bind(isSelectedRow);
    }

    private void initData() {
        ObservableList<Dictionary> observableList = FXCollections.observableArrayList(sampleService.getDictionaries());
        dictionaries = new SortedList<>(observableList);
    }

    public void importClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("~/")); //TODO
        fileChooser.setInitialFileName("dict.xdxf");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XDXF Dictionaries", "*.xdxf"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        //TODO null???
        File file = fileChooser.showOpenDialog(dictionariesTable.getScene().getWindow());
        Xdxf xdxf = sampleService.getDictionary(file);
        //TODO xdxf -> dict
        //TODO to DB
        //TODO refresh
    }

    //TODO
    public void removeClick(ActionEvent actionEvent) {
    }
}
