package md.leonis.assistant.controller;

import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebView;
import md.leonis.assistant.config.ConfigHolder;
import md.leonis.assistant.utils.CssGenerator;
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
import java.io.StringWriter;

@Controller
public class WatchScriptController {

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private ConfigHolder configHolder;

    @FXML
    private WebView webView;

    @FXML
    public TextArea textArea;

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

    @FXML
    private void initialize() {

        webView.setCursor(Cursor.DEFAULT);

        webView.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                Document doc = webView.getEngine().getDocument();

                styleNode = doc.createElement("style");
                Text styleContent = generateCss();

                styleNode.appendChild(styleContent);
                doc.getDocumentElement().getElementsByTagName("head").item(0).appendChild(styleNode);

                //TODO add to unknown list
                EventListener clickEventListener = ev -> System.out.println(((Element) ev.getTarget()).getTextContent());
                EventListener overEventListener = ev -> webView.setCursor(Cursor.HAND);
                EventListener outEventListener = ev -> webView.setCursor(Cursor.MOVE);

                NodeList nodeList = doc.getElementsByTagName("span");

                // https://www.khanacademy.org/computing/computer-programming/html-css-js/html-js-dom-events/a/dom-event-types
                for (int i = 0; i < nodeList.getLength(); i++) {
                    EventTarget target = ((EventTarget) nodeList.item(i));
                    target.addEventListener("click", clickEventListener, false);
                    target.addEventListener("mousemove", overEventListener, false);
                    target.addEventListener("mouseover", overEventListener, false);
                    target.addEventListener("mouseout", outEventListener, false);
                }
            }
        });

        //TODO link with DB
        //TODO static class
        HtmlFormatter htmlFormatter = HtmlFormatter.builder().build();

        String text = htmlFormatter.toHtml("I don't see how this can be done without a loop. \n" +
                "A string is more or less an group of characters. \n" +
                "If it is a group (collection, array, etc) then no matter if it is internal or external to the native code, \n" +
                "I would expect that you would need a loop in order to find something within the \"group\". \n" +
                "I believe \"without using a loop?\" is more like \"without writing my own loop?\"");

        webView.getEngine().loadContent("<html><body> " + text + " </body></html>");
    }

    public void sourceCodeClick() {
        refreshWebView();
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
            String xmlString = result.getWriter().toString();


            textArea.setText(xmlString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Text generateCss() {
        Document doc = webView.getEngine().getDocument();
        return doc.createTextNode(CssGenerator.generate(colorsCheckBox.isSelected(), unknownWordsCheckBox.isSelected(), unkCheckBox.isSelected(), a1CheckBox.isSelected()
                , a2CheckBox.isSelected(), b1CheckBox.isSelected(), b2CheckBox.isSelected(), c1CheckBox.isSelected(), c2CheckBox.isSelected(), c2pCheckBox.isSelected()));
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
}
