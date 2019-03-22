package md.leonis.assistant.service;

import md.leonis.assistant.dao.standard.DictionaryDAO;
import md.leonis.assistant.dao.standard.UserWordBankDAO;
import md.leonis.assistant.dao.standard.WordFrequencyDAO;
import md.leonis.assistant.dao.standard.WordLevelDAO;
import md.leonis.assistant.domain.LanguageLevel;
import md.leonis.assistant.domain.standard.Dictionary;
import md.leonis.assistant.domain.standard.UserWordBank;
import md.leonis.assistant.domain.standard.WordFrequency;
import md.leonis.assistant.domain.standard.WordLevel;
import md.leonis.assistant.domain.xdxf.lousy.Xdxf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Comparator;
import java.util.List;

@Service
public class SampleService {

    private static final Logger log = LoggerFactory.getLogger(SampleService.class);

    @Autowired
    private WordLevelDAO wordLevelDAO;

    @Autowired
    private WordFrequencyDAO wordFrequencyDAO;

    @Autowired
    private UserWordBankDAO wordBankDAO;

    @Autowired
    private DictionaryDAO dictionaryDAO;

    @Autowired
    private UserWordBankDAO userWordBankDAO;

    public void echo() {
        System.out.println("ECHO");
    }

    public LanguageLevel getLevel(String word) {
        List<WordLevel> wordLevelList = wordLevelDAO.findByWord(word);
        if (wordLevelList.isEmpty()) {
            return LanguageLevel.UNK;
        }
        if (wordLevelList.size() > 1) {
            log.info(wordLevelList.toString());
        }
        return wordLevelList.stream().min(Comparator.comparing(WordLevel::getWord)).get().getLevel();
    }

    public WordLevel getWordLevel(String word) {
        List<WordLevel> wordLevelList = wordLevelDAO.findByWord(word);
        if (wordLevelList.isEmpty()) {
            return new WordLevel();
        }
        if (wordLevelList.size() > 1) {
            log.info(wordLevelList.toString());
        }
        return wordLevelList.stream().min(Comparator.comparing(WordLevel::getWord)).get();
    }

    public WordFrequency getWordFrequency(String word) {
        List<WordFrequency> wordFrequencies = wordFrequencyDAO.findByWord(word);
        if (wordFrequencies.isEmpty()) {
            return new WordFrequency();
        }
        if (wordFrequencies.size() > 1) {
            log.info(wordFrequencies.toString());
        }
        return wordFrequencies.stream().min(Comparator.comparing(WordFrequency::getWord)).get();
    }

    public boolean getKnownStatus(String word) {
        return wordBankDAO.findById(word).isPresent();
    }

    //TODO get name
    public Xdxf getDictionary(File file) {
        try {
            JAXBContext jc = JAXBContext.newInstance(Xdxf.class);

            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            spf.setFeature("http://xml.org/sax/features/validation", false);
            spf.setValidating(false);

            XMLReader xmlReader = spf.newSAXParser().getXMLReader();
            InputSource inputSource = new InputSource(new FileReader(file));
            SAXSource source = new SAXSource(xmlReader, inputSource);

            Unmarshaller unmarshaller = jc.createUnmarshaller();

            return (Xdxf) unmarshaller.unmarshal(source);

        } catch (JAXBException | SAXException | ParserConfigurationException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Dictionary> getDictionaries() {
        /*List<Dictionary> result = new ArrayList<>();
        result.add(new Dictionary(1L, "RUS", "ENG", "format", "revision", "fullName", 10L, 120000, ""));
        result.add(new Dictionary(2L, "ENG", "RUS", "format2", "revision2", "Tro lo lo", 20000L, 1000000, ""));
        return result;*/
        return dictionaryDAO.findAll();
    }

    public void saveDictionary(Dictionary dictionary) {
        dictionaryDAO.save(dictionary);
    }

    public void deleteDictionary(Long id) {
        dictionaryDAO.deleteById(id);
    }

    public void deleteAllDictionaries(List<Dictionary> dictionaries) {
        dictionaryDAO.deleteAll(dictionaries);
    }

    public List<UserWordBank> getUserWordBank() {
        return userWordBankDAO.findAll();
    }
}
