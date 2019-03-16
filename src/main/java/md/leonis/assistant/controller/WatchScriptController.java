package md.leonis.assistant.controller;

import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebView;
import md.leonis.assistant.config.ConfigHolder;
import md.leonis.assistant.utils.CssGenerator;
import md.leonis.assistant.utils.HtmlUtils;
import md.leonis.assistant.view.StageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
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

    private static final String CSS =
            "body {"
                    + "    background-color: #00ff80; "
                    + "    font-family: Arial, Helvetica, san-serif;"
                    + "}"
                    + ".A1 {\n" +
                    "    background-color: #ccc;\n" +
                    "    color: #ffffff;\n" +
                    "    padding: 20px\n" +
                    "}"
                    + ".A2 {\n" +
                    "    background-color: #ff0000;\n" +
                    "    color: #ffffff;\n" +
                    "    padding: 20px\n" +
                    "}"
                    ;


    private Node styleNode;

    @FXML
    private void initialize() {

        webView.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                Document doc = webView.getEngine().getDocument() ;

                styleNode = doc.createElement("style");
                Text styleContent = doc.createTextNode(CSS);
                styleNode.appendChild(styleContent);
                doc.getDocumentElement().getElementsByTagName("head").item(0).appendChild(styleNode);

                System.out.println(webView.getEngine().executeScript("document.documentElement.outerHTML"));
                System.out.println("=======================");
                System.out.println(webView.getEngine().executeScript("document.documentElement.innerHTML"));
            }
        });

        String text = HtmlUtils.toHtml("I don't see how this can be done without a loop. \n" +
                "A string is more or less an group of characters. \n" +
                "If it is a group (collection, array, etc) then no matter if it is internal or external to the native code, \n" +
                "I would expect that you would need a loop in order to find something within the \"group\". \n" +
                "I believe \"without using a loop?\" is more like \"without writing my own loop?\"");

        webView.getEngine().loadContent("<html><body><h1>Hello!</h1>This is a <b>test</b> <span class='a1'>A1</span> <span class='a2 unknown'>A2</span> " + text + " </body></html>");
    }

    public void sourceCodeClick(ActionEvent actionEvent) throws TransformerException {
        Document doc = webView.getEngine().getDocument() ;
        styleNode.removeChild(styleNode.getFirstChild());
        Text styleContent = doc.createTextNode(CssGenerator.generate());
        styleNode.appendChild(styleContent);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "html");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        //initialize StreamResult with File object to save to file
        StreamResult result = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(webView.getEngine().getDocument());
        transformer.transform(source, result);
        String xmlString = result.getWriter().toString();


        textArea.setText(xmlString);
    }
}
