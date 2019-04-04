package md.leonis.assistant.controller.template;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import lombok.SneakyThrows;
import md.leonis.assistant.config.ConfigHolder;
import md.leonis.assistant.domain.LanguageLevel;
import md.leonis.assistant.domain.test.Dictionary;
import md.leonis.assistant.domain.xdxf.lousy.Ar;
import md.leonis.assistant.view.StageManager;

import java.io.IOException;
import java.util.Set;

public class ShowCardsController extends BorderPane {

    public HBox answerHBox;
    public HBox actionHBox;
    public WebView answerWebView;
    public WebView questionWebView;

    private ObservableList<Dictionary> dictionaries;

    private FilteredList<Ar> filteredData;

    //TODO delete
    private StageManager stageManager;

    //TODO delete
    private ConfigHolder configHolder;

    public ShowCardsController(StageManager stageManager, ConfigHolder configHolder, Set<LanguageLevel> levels) {
        this.stageManager = stageManager;
        this.configHolder = configHolder;
        /*this.levels = levels;
        this.selectedLevels = FXCollections.observableSet(new HashSet<>(levels));*/

        //TODO in stageManager
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/template/showCards.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();

            /*checkBoxes = containerHBox.getChildren().stream()
                    .filter(n -> n instanceof CheckBox)
                    .map(n -> (CheckBox) n)
                    .peek(c -> {
                        LanguageLevel languageLevel = getLevel(c);
                        c.setSelected(selectedLevels.contains(languageLevel));
                        c.setVisible(levels.contains(languageLevel));
                        c.managedProperty().bind(c.visibleProperty());
                    })
                    .filter(c -> levels.contains(getLevel(c)))
                    .peek(c -> c.setOnAction(this::filterCheckBoxClick))
                    .collect(Collectors.toSet());*/

        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    /*@FXML
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

        dictionaries = FXCollections.observableArrayList(testService.getDictionaries());
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


        //descrColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        descrColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFullValue()));

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
    }*/

    @SneakyThrows
    private void initData() {
        /*Dictionary dictionary = dictionariesComboBox.getSelectionModel().getSelectedItem();
        if (dictionary != null) {
            File file = new File(dictionary.getPath());
            //TODO loader, depends on format
            Xdxf xdxf = testService.getDictionary(file);
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
        }*/
    }

    /*public void searchWordFieldKeyReleased() {
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
    }*/
}
