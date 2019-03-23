package md.leonis.assistant.controller;

import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import md.leonis.assistant.config.ConfigHolder;
import md.leonis.assistant.domain.standard.Dictionary;
import md.leonis.assistant.domain.standard.WordToLearn;
import md.leonis.assistant.domain.xdxf.lousy.Ar;
import md.leonis.assistant.service.SampleService;
import md.leonis.assistant.utils.CssGenerator;
import md.leonis.assistant.utils.GoogleApi;
import md.leonis.assistant.utils.HtmlFormatter;
import md.leonis.assistant.view.StageManager;
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

    public ListView<String> listView;
    public Button removeButton;
    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private ConfigHolder configHolder;

    @Autowired
    private SampleService sampleService;

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
    public CheckBox unkCheckBox;
    public CheckBox a1CheckBox;
    public CheckBox a2CheckBox;
    public CheckBox b1CheckBox;
    public CheckBox b2CheckBox;
    public CheckBox c1CheckBox;
    public CheckBox c2CheckBox;
    public CheckBox c2pCheckBox;

    private Node styleNode;

    private boolean allowRefresh = true;

    private HtmlFormatter htmlFormatter;

    List<Ar> ars;

    //TODO read from video || DB
    String text = "I don't see how this can be done without a loop. \n" +
            "A string is more or less an group of characters. \n" +
            "If it is a group (collection, array, etc) then no matter if it is internal or external to the native code, \n" +
            "I would expect that you would need a loop in order to find something within the \"group\". \n" +
            "I believe \"without using a loop?\" is more like \"without writing my own loop?\" \n" +
            "Mortal mortal Kombat rare sponge";

    @FXML
    private void initialize() {
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
                stageManager.showErrorAlert("Error loading text",  "Failed to display the text in the browser",
                        "WebView Worker Exception == " + webView.getEngine().getLoadWorker().getException());
                Stage stage = (Stage) webView.getScene().getWindow();
                stage.close();
            }
        });

        htmlFormatter = new HtmlFormatter(text, sampleService);

        webView.getEngine().loadContent("<html><body> " + htmlFormatter.getHtml() + " </body></html>");
    }

    //TODO one dictionary for APP
    private void initDictionary() {
        //TODO select right dictionary, not Mueller only
        Dictionary dictionary = sampleService.getDictionaries().get(0);
        //TODO File in service
        ars = sampleService.getDictionary(new File(dictionary.getPath())).getAr();
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
                .showUnknown(unkCheckBox.isSelected())
                .showA1(a1CheckBox.isSelected())
                .showA2(a2CheckBox.isSelected())
                .showB1(b1CheckBox.isSelected())
                .showB2(b2CheckBox.isSelected())
                .showC1(c1CheckBox.isSelected())
                .showC2(c2CheckBox.isSelected())
                .showC2p(c2pCheckBox.isSelected())
                .build();
        return webView.getEngine().getDocument().createTextNode(cssGenerator.generate());
    }

    public void showAllClick() {
        allowRefresh = false;
        unknownWordsCheckBox.setSelected(false);
        colorsCheckBox.setSelected(true);
        unkCheckBox.setSelected(true);
        a1CheckBox.setSelected(true);
        a2CheckBox.setSelected(true);
        b1CheckBox.setSelected(true);
        b2CheckBox.setSelected(true);
        c1CheckBox.setSelected(true);
        c2CheckBox.setSelected(true);
        c2pCheckBox.setSelected(true);
        allowRefresh = true;
        refreshWebView();
    }

    public void filterCheckBoxClick() {
        if (allowRefresh) {
            refreshWebView();
        }
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
            sampleService.saveWordToLearn(wordToLearn);
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
        String translation = ars.stream().filter(ar -> ar.getK().equalsIgnoreCase(word))
                //TODO checks in AR
                .map(ar -> ar.getTr() == null ? "" : ar.getTr() + "\n" + ar.getFullValue()).collect(Collectors.joining("\n\n"));
        //TODO I need 100% working version of GoogleApi
        /*if (translation.isEmpty()) {
            translation = GoogleApi.translate(word);
            if (translation == null) {
                translation = "";
            }
        }*/
        textArea.setText(translation);
    }

}
