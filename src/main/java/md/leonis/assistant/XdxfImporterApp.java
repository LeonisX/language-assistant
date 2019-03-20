package md.leonis.assistant;

import md.leonis.assistant.domain.xdxf.lousy.Xdxf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import java.io.FileNotFoundException;
import java.io.FileReader;

@SpringBootApplication
public class XdxfImporterApp {

    private static final Logger log = LoggerFactory.getLogger(XdxfImporterApp.class);

    private static final String DICT_NAME = "/home/leonidstavila/Downloads/dict/mueller24/dict.files";

    public static void main(String[] args) throws SAXException, ParserConfigurationException, FileNotFoundException {
        //ConfigurableApplicationContext springContext = SpringApplication.run(XdxfImporterApp.class, args);

        log.info("Importing");

        try {
            JAXBContext jc = JAXBContext.newInstance(Xdxf.class);

            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            spf.setFeature("http://xml.org/sax/features/validation", false);
            spf.setValidating(false);

            XMLReader xmlReader = spf.newSAXParser().getXMLReader();
            InputSource inputSource = new InputSource(new FileReader(DICT_NAME));
            SAXSource source = new SAXSource(xmlReader, inputSource);

            Unmarshaller unmarshaller = jc.createUnmarshaller();
            Xdxf xdxf = (Xdxf) unmarshaller.unmarshal(source);

            System.out.println(xdxf);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
