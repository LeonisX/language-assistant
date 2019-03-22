package md.leonis.assistant.domain.xdxf.lousy;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class RawTextXmlAdapter extends XmlAdapter<Element, String> {

    @Override
    public Element marshal(String v) throws Exception {
        // TODO NYI Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    @Override
    public String unmarshal(Element node) throws Exception {
        //return node.getTextContent();
        //return innerXml(node);
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer = transFactory.newTransformer();
        StringWriter buffer = new StringWriter();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.transform(new DOMSource(node), new StreamResult(buffer));
        return buffer.toString();
    }

    private static String innerXml(Node node) {
        DOMImplementationLS lsImpl = (DOMImplementationLS) node.getOwnerDocument().getImplementation().getFeature("LS", "3.0");
        LSSerializer lsSerializer = lsImpl.createLSSerializer();
        if (lsSerializer.getDomConfig().canSetParameter("xml-declaration", Boolean.TRUE))
            lsSerializer.getDomConfig().setParameter("xml-declaration", Boolean.FALSE);
        NodeList childNodes = node.getChildNodes();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < childNodes.getLength(); i++) {
            sb.append(lsSerializer.writeToString(childNodes.item(i)));
        }
        return sb.toString();
    }
}