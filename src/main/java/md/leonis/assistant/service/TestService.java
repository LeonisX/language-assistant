package md.leonis.assistant.service;

import lombok.SneakyThrows;
import md.leonis.assistant.dao.test.*;
import md.leonis.assistant.dao.user.UserWordBankDAO;
import md.leonis.assistant.domain.LanguageLevel;
import md.leonis.assistant.domain.test.*;
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
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class TestService {

    private static final Logger log = LoggerFactory.getLogger(TestService.class);

    @Autowired
    private WordLevelDAO wordLevelDAO;

    @Autowired
    private WordFrequencyDAO wordFrequencyDAO;

    @Autowired
    private WordPlaceDAO wordPlaceDAO;

    @Autowired
    private UserWordBankDAO wordBankDAO;

    @Autowired
    private DictionaryDAO dictionaryDAO;

    @Autowired
    private VarianceDAO varianceDAO;

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

    public List<Variance> getVariances(String word) {
        return varianceDAO.findByVarianceEqualsIgnoreCase(word);
    }

    public Iterable<WordLevel> saveWordLevels(Iterable<WordLevel> wordLevels) {
        return wordLevelDAO.saveAll(wordLevels);
    }

    public long getWordLevelsCount() {
        return wordLevelDAO.count();
    }

    public List<WordFrequency> getWordFrequencies() {
        return wordFrequencyDAO.findAll();
    }

    public Iterable<WordFrequency> saveWordFrequencies(Iterable<WordFrequency> wordFrequencies) {
        return wordFrequencyDAO.saveAll(wordFrequencies);
    }

    public long getWordFrequenciesCount() {
        return wordFrequencyDAO.count();
    }

    public List<WordPlace> getWordPlaces() {
        return wordPlaceDAO.findAll();
    }

    public Iterable<WordPlace> saveWordPlaces(Iterable<WordPlace> wordPlaces) {
        return wordPlaceDAO.saveAll(wordPlaces);
    }

    public long getWordPlacesCount() {
        return wordPlaceDAO.count();
    }

    @SneakyThrows
    public void importWordFrequencies() {
        log.info("Importing");
        wordFrequencyDAO.deleteAll();

        ClassLoader classLoader = TestService.class.getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("banks/nor.txt")).getFile());
        List<String> list = Files.readAllLines(file.toPath(), Charset.defaultCharset());

        for (String nextLine : list) {
            String line = nextLine.trim();
            if (!line.isEmpty() && !line.startsWith("#")) {
                String[] chunks = line.split("\t");
                WordFrequency wordFrequency = new WordFrequency(chunks[0], Long.parseLong(chunks[1]));
                wordFrequencyDAO.save(wordFrequency);
            }
        }
    }

    @SneakyThrows
    public void importWordPlaces() {
        log.info("Importing");
        wordPlaceDAO.deleteAll();

        ClassLoader classLoader = TestService.class.getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("banks/nor.txt")).getFile());
        List<String> list = Files.readAllLines(file.toPath(), Charset.defaultCharset());

        long place = 1;
        for (String nextLine : list) {
            String line = nextLine.trim();
            if (!line.isEmpty() && !line.startsWith("#")) {
                String[] chunks = line.split("\t");
                WordPlace wordPlace = new WordPlace(chunks[0], place++);
                wordPlaceDAO.save(wordPlace);
            }
        }
    }

}
