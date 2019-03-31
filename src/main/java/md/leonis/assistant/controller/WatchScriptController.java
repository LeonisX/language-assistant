package md.leonis.assistant.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import md.leonis.assistant.config.ConfigHolder;
import md.leonis.assistant.controller.template.LevelsSelectController;
import md.leonis.assistant.domain.LanguageLevel;
import md.leonis.assistant.domain.test.Dictionary;
import md.leonis.assistant.domain.user.WordToLearn;
import md.leonis.assistant.domain.xdxf.lousy.Ar;
import md.leonis.assistant.service.TestService;
import md.leonis.assistant.service.UserService;
import md.leonis.assistant.source.gse.GseSourceFactory;
import md.leonis.assistant.utils.CssGenerator;
import md.leonis.assistant.utils.HtmlFormatter;
import md.leonis.assistant.view.StageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.w3c.dom.*;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

//TODO table tab: word || frequency || level + filter + sort
@Controller
public class WatchScriptController {

    private static final Logger log = LoggerFactory.getLogger(WatchScriptController.class);

    public ListView<String> listView;
    public Button removeButton;
    public VBox vBox;
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

    @FXML
    private WebView webView;

    @FXML
    public TextArea textArea;

    //TODO dynamically build
    //TODO dynamically show only relevant checkBoxes
    //TODO also another type of filter with range selection
    //TODO template + separate controller

    //TODO filters: detect endings, upper cases
    public CheckBox unknownWordsCheckBox;
    public CheckBox colorsCheckBox;

    private Node styleNode;

    private LevelsSelectController levelsSelectController;
    private ObservableSet<LanguageLevel> selectedLevels;

    private HtmlFormatter htmlFormatter;

    private List<Ar> ars;

    //TODO read from video || DB
    private String text = "I don't see how this can be done without a loop. \n" +
            "A string is more or less an group of characters. \n" +
            "If it is a group (collection, array, etc) then no matter if it is internal or external to the native code, \n" +
            "I would expect that you would need a loop in order to find something within the \"group\". \n" +
            "I believe \"without using a loop?\" is more like \"without writing my own loop?\" \n" +
            "Mortal mortal Kombat rare sponge, writings, stone, stones";

    @FXML
    private void initialize() {
        //TODO from SourceFactory
        ObservableSet<LanguageLevel> levels = FXCollections.observableSet(gseSourceFactory.getLanguageLevels());
        selectedLevels = FXCollections.observableSet(levels);
        levelsSelectController = new LevelsSelectController(stageManager, configHolder, levels, selectedLevels);
        selectedLevels.addListener((SetChangeListener<LanguageLevel>) observable -> onSelectedListChange());
        vBox.getChildren().add(levelsSelectController);

        initDictionary();

        webView.setCursor(Cursor.DEFAULT);

        webView.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                Document doc = webView.getEngine().getDocument();

                styleNode = doc.createElement("style");
                Text styleContent = generateCss();

                styleNode.appendChild(styleContent);
                doc.getDocumentElement().getElementsByTagName("head").item(0).appendChild(styleNode);

                EventListener clickEventListener = ev -> {
                    String word = ((Element) ev.getTarget()).getTextContent();
                    if (!listView.getItems().contains(word)) {
                        listView.getItems().add(word);
                    }
                };

                EventListener overEventListener = ev -> {
                    webView.setCursor(Cursor.HAND);
                    showTranslation(((Element) ev.getTarget()).getTextContent());
                    String clazz = ((Element) ev.getTarget()).getAttribute("class");
                    if (!clazz.endsWith("decorated")) {
                        clazz = clazz + " " + "decorated";
                        ((Element) ev.getTarget()).setAttribute("class", clazz);
                    }
                };

                EventListener outEventListener = ev -> {
                    webView.setCursor(Cursor.MOVE);
                    showTranslation("");
                    String clazz = ((Element) ev.getTarget()).getAttribute("class");
                    if (clazz.endsWith("decorated")) {
                        clazz = clazz.replace("decorated", "").trim();
                        ((Element) ev.getTarget()).setAttribute("class", clazz);
                    }
                };

                NodeList nodeList = doc.getElementsByTagName("span");

                // https://www.khanacademy.org/computing/computer-programming/html-css-js/html-js-dom-events/a/dom-event-types
                for (int i = 0; i < nodeList.getLength(); i++) {
                    EventTarget target = ((EventTarget) nodeList.item(i));
                    target.addEventListener("click", clickEventListener, false);
                    target.addEventListener("mousemove", overEventListener, false);
                    target.addEventListener("mouseover", overEventListener, false);
                    target.addEventListener("mouseout", outEventListener, false);
                }
            } else if (newState == Worker.State.FAILED) {
                //TODO test
                stageManager.showErrorAlert("Error loading text", "Failed to display the text in the browser",
                        "WebView Worker Exception == " + webView.getEngine().getLoadWorker().getException());
                Stage stage = (Stage) webView.getScene().getWindow();
                stage.close();
            }
        });

        htmlFormatter = new HtmlFormatter(text, testService, levels);

        webView.getEngine().loadContent("<html><body> " + htmlFormatter.getHtml() + " </body></html>");
    }

    //TODO one dictionary for APP
    private void initDictionary() {
        //TODO select right dictionary, not Mueller only
        //TODO need to be sure that we have at once 1 dictionary
        Dictionary dictionary = testService.getDictionaries().get(0);
        //TODO new File in service
        //TODO may be convert all dictionaries to DB
        ars = testService.getDictionary(new File(dictionary.getPath())).getAr();
    }

    private void refreshWebView() {
        try {
            styleNode.removeChild(styleNode.getFirstChild());
            Text styleContent = generateCss();
            styleNode.appendChild(styleContent);

            //TODO this is debug code
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "html");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(webView.getEngine().getDocument());
            transformer.transform(source, result);
            textArea.setText(result.getWriter().toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Text generateCss() {
        CssGenerator cssGenerator = CssGenerator.builder()
                .hideKnownWords(unknownWordsCheckBox.isSelected())
                .showColors(colorsCheckBox.isSelected())
                .languageLevels(selectedLevels)
                .build();
        return webView.getEngine().getDocument().createTextNode(cssGenerator.generate());
    }

    public void showAllClick() {
        selectedLevels.removeListener((SetChangeListener<LanguageLevel>) observable -> onSelectedListChange());
        unknownWordsCheckBox.setSelected(false);
        colorsCheckBox.setSelected(true);
        levelsSelectController.showAllClick();
        selectedLevels.addListener((SetChangeListener<LanguageLevel>) observable -> onSelectedListChange());
        refreshWebView();
    }

    private void onSelectedListChange() {
        refreshWebView();
    }

    public void sourceCodeClick() {
        refreshWebView();
    }

    public void onListViewMouseClicked() {
        showTranslation(listView.getSelectionModel().getSelectedItem());
    }

    public void onNeedToLearnClick() {
        listView.getItems().forEach(word -> {
            WordToLearn wordToLearn = new WordToLearn();
            wordToLearn.setWord(word);
            userService.saveWordToLearn(wordToLearn);
        });
        listView.getItems().clear();
    }

    public void onRemoveButtonClick() {
        listView.getItems().removeAll(listView.getSelectionModel().getSelectedItems());
    }

    private void showTranslation(String word) {
        if (word.trim().isEmpty()) {
            textArea.setText("");
            return;
        }
        String translation = findTranslation(word);

        if (translation.isEmpty()) {
            translation = testService.getVariances(word).stream()
                    .map(v -> findTranslation(v.getWord())).filter(w -> !w.isEmpty()).collect(Collectors.joining("\n\n"));
        }
        //TODO if still empty - find online
        //TODO I need 100% working version of GoogleApi
        /*if (translation.isEmpty()) {
            translation = GoogleApi.translate(word);
            if (translation == null) {
                translation = "";
            }
        }*/
        textArea.setText(translation);
    }

    private String findTranslation(String word) {
        return ars.stream().filter(ar -> ar.getK().equalsIgnoreCase(word))
                //TODO checks in AR
                .map(ar -> ar.getTr() == null ? "" : ar.getTr() + "\n" + ar.getFullValue()).collect(Collectors.joining("\n\n"));
    }

}
