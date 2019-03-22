package md.leonis.assistant.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import lombok.SneakyThrows;
import md.leonis.assistant.domain.standard.Dictionary;
import md.leonis.assistant.domain.xdxf.lousy.Ar;
import md.leonis.assistant.domain.xdxf.lousy.Xdxf;
import md.leonis.assistant.service.SampleService;
import md.leonis.assistant.view.StageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DictionaryController {

    @FXML
    public TableView<Ar> wordsTable;

    @FXML
    public TableColumn<Ar, String> wordColumn;
    @FXML
    public TableColumn<Ar, String> transcrColumn;
    @FXML
    public TableColumn<Ar, String> descrColumn;

    public TextField searchWordField;
    public TextArea textArea;
    public ComboBox<Dictionary> dictionariesComboBox;

    private ObservableList<Dictionary> dictionaries;

    private FilteredList<Ar> filteredData;

    @Autowired
    private SampleService sampleService;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @FXML
    private void initialize() {
        dictionariesComboBox.setConverter(new StringConverter<Dictionary>() {

            @Override
            public String toString(Dictionary dictionary) {
                return dictionary.getFullName();
            }

            @Override
            public Dictionary fromString(String string) {
                return dictionariesComboBox.getItems().stream().filter(d ->
                        d.getFullName().equals(string)).findFirst().orElse(null);
            }
        });

        dictionaries = FXCollections.observableArrayList(sampleService.getDictionaries());
        dictionariesComboBox.setItems(dictionaries);
        //if (!dictionaries.isEmpty()) {
        dictionariesComboBox.getSelectionModel().select(0);
        //}

        initData();

        wordColumn.setCellValueFactory(new PropertyValueFactory<>("k"));
        wordColumn.setComparator(String::compareToIgnoreCase);

        transcrColumn.setCellValueFactory(new PropertyValueFactory<>("tr"));
        transcrColumn.setComparator(null);
        //transcrColumn.setCellValueFactory(word -> new SimpleStringProperty(word.getValue().getLevel().getTitle()));
        transcrColumn.sortTypeProperty();
        descrColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        descrColumn.setComparator(null);
        //xColumn.setCellValueFactory(new PropertyValueFactory<>("x"));

        descrColumn.setCellFactory(tc -> {
            TableCell<Ar, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(descrColumn.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
    }

    @SneakyThrows
    private void initData() {
        Dictionary dictionary = dictionariesComboBox.getSelectionModel().getSelectedItem();
        if (dictionary != null) {
            File file = new File(dictionary.getPath());
            //TODO loader, depends on format
            Xdxf xdxf = sampleService.getDictionary(file);
            List<Ar> arList = xdxf.getAr();
            ObservableList<Ar> wordData = FXCollections.observableArrayList(arList);

            searchWordField.setText("");

            filteredData = new FilteredList<>(wordData);
            filteredData.setPredicate(p -> true); // Initial: show all rows

            //wordsTable.getSortOrder().add(wordColumn);
            SortedList<Ar> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(wordsTable.comparatorProperty());

            wordsTable.setItems(sortedData);
            textArea.setText(xdxf.toString());
        } else {
            filteredData = new FilteredList<>(FXCollections.observableArrayList(new ArrayList<>()));
        }
    }

    public void searchWordFieldKeyReleased() {
        updatePredicate();
    }

    private void updatePredicate() {
        filteredData.setPredicate((data) -> {
            boolean showItem = true;
            if (!searchWordField.getText().isEmpty()) {
                showItem = data.getK().toLowerCase().startsWith(searchWordField.getText().toLowerCase());
            }
            return showItem;
        });
    }

    public void onDictionariesComboBoxAction() {
        initData();
    }
}
