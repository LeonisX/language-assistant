package md.leonis.assistant.service;

import md.leonis.assistant.dao.standard.DictionaryDAO;
import md.leonis.assistant.dao.standard.UserWordBankDAO;
import md.leonis.assistant.dao.standard.WordLevelDAO;
import md.leonis.assistant.domain.LanguageLevel;
import md.leonis.assistant.domain.standard.Dictionary;
import md.leonis.assistant.domain.standard.WordLevel;
import md.leonis.assistant.domain.xdxf.lousy.Xdxf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.*;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class SampleService {

    private static final Logger log = LoggerFactory.getLogger(SampleService.class);

    private static final String DICT_NAME = "_resources/files/dictionaries/mueller24/dict.xdxf";

    @Autowired
    private WordLevelDAO wordLevelDAO;

    @Autowired
    private UserWordBankDAO wordBankDAO;

    @Autowired
    private DictionaryDAO dictionaryDAO;

    public void echo() {
        System.out.println("ECHO");
    }

    public LanguageLevel getLevel(String word) {
        List<WordLevel> wordLevelList = wordLevelDAO.findByWord(word);
        System.out.println(wordLevelList.size());
        if (wordLevelList.isEmpty()) {
            return LanguageLevel.UNK;
        }
        if (wordLevelList.size() > 1) {
            log.info(wordLevelList.toString());
        }
        return wordLevelList.stream().min(Comparator.comparing(WordLevel::getWord)).get().getLevel();
    }

    public boolean getKnownStatus(String word) {
        return wordBankDAO.findById(word).isPresent();
    }

    //TODO get name
    public Xdxf getDictionary() {
        try {
            JAXBContext jc = JAXBContext.newInstance(Xdxf.class);

            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            spf.setFeature("http://xml.org/sax/features/validation", false);
            spf.setValidating(false);

            XMLReader xmlReader = spf.newSAXParser().getXMLReader();
            URL url = SampleService.class.getClassLoader().getResource(DICT_NAME);
            File file = new File(url.toURI());
            InputSource inputSource = new InputSource(new FileReader(file));
            SAXSource source = new SAXSource(xmlReader, inputSource);

            Unmarshaller unmarshaller = jc.createUnmarshaller();

            return (Xdxf) unmarshaller.unmarshal(source);

        } catch (JAXBException | SAXException | ParserConfigurationException | FileNotFoundException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Dictionary> getDictionaries() {
        List<Dictionary> result = new ArrayList<>();
        result.add(new Dictionary(1L, "RUS", "ENG", "format", "revision", "fullName", 10L, 120000L));
        result.add(new Dictionary(2L, "ENG", "RUS", "format2", "revision2", "Tro lo lo", 20000L, 1000000L));
        return result;
        //TODO enable
        //return dictionaryDAO.findAll();
    }
}
