package md.leonis.assistant.service;

import lombok.SneakyThrows;
import md.leonis.assistant.dao.test.*;
import md.leonis.assistant.dao.user.UserWordBankDAO;
import md.leonis.assistant.domain.LanguageLevel;
import md.leonis.assistant.domain.test.*;
import md.leonis.assistant.domain.test.Dictionary;
import md.leonis.assistant.domain.xdxf.lousy.Xdxf;
import org.jsoup.Jsoup;
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
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

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
            log.warn("Can't find level for `{}`", word);
            return new WordLevel(word, LanguageLevel.UNK);
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

    //TODO get word
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


//http://ucrel.lancs.ac.uk/bncfreq/flists.html
//http://ucrel.lancs.ac.uk/bncfreq/lists/1_1_all_fullalpha.txt.Z


/*http://ucrel.lancs.ac.uk/bncfreq/flists.html
        http://ucrel.lancs.ac.uk/bncfreq/lists/1_1_all_fullalpha.txt.Z

        Фактически, на базе этого надо сделать несколько списков.

        1. Полный, как есть (20 Мб) - необходимо замапить на наши объекты и сохранить в базу
        2. Для поиска вариантов (3-4 Мб) key:value - готово
        3. Какой-то ещё

        На самом деле, тут не полный список, следует свериться с https://en.wiktionary.org/wiki/appetitively
        И совместить два списка, т.е. если у слова 1	0.00 или что-то похожее, но есть статья в вики, то не удаляем


        Clean this list.
        1. v Combine to groups (normal -> sub)
        2. v Clear groups w/o children
        3. v Convert any &rehy;, &mdash; to symbols
        4. v a_bit -> a bit
        5. May be remove duplicated children
        6. Map type (see banks/screenshot) (PartOfSpeech enum is ready)
        7. v Generate text to check
        8. Smt else
        9. Export to DB (only for simply list)

        ### TODO

        1. Import to DB only headwords (word, ), no variants
        2. Investigate strange Parts of Speech
        3. May be optimize
        */

    public List<Variance> getVariances() {
        return varianceDAO.findAll();
    }

    public Iterable<Variance> saveVariances(Iterable<Variance> variances) {
        return varianceDAO.saveAll(variances);
    }

    public long getVariancesCount() {
        return varianceDAO.count();
    }

    @SneakyThrows
    public void importVariances() {
        log.info("Importing");

        //Word = Word type (headword followed by any variant forms) - see pp.4-5
        //PoS  = Part of speech (grammatical word class - see pp. 12-13)
        //Freq = Rounded frequency per million word tokens (down to a minimum of 10 occurrences of a lemma per million)- see pp. 5
        //Ra   = Range: number of sectors of the corpus (out of a maximum of 100) in which the word occurs
        //Disp = Dispersion value (Juilland's D) from a minimum of 0.00 to a maximum of 1.00.

        //  Word PoS Freq Ra Disp
        //	%	NoC	%	6	53	0.64

        //	book	NoC	%	374	100	0.95
        //	@	@	book	243	100	0.95
        //	@	@	books	131	100	0.94
        //	book	Uncl	:	0	1	0.00
        //	book	Verb	%	21	97	0.92
        //	@	@	book	5	83	0.88
        //	@	@	booked	12	97	0.92
        //	@	@	booking	4	78	0.88
        //	@	@	bookking	0	1	0.00
        //	@	@	books	0	25	0.81

        String home = System.getProperty("user.home");

        List<String> lines = Files.readAllLines(new File(home + File.separatorChar + "1_1_all_fullalpha.txt").toPath(), Charset.forName("utf8"));
        List<String[]> chunks = lines.stream().map(line -> line.trim().split("\t")).collect(Collectors.toList());

        Map<String[], List<String[]>> map = new LinkedHashMap<>();

        String[] currentRoot = null;
        for (String[] chunk : chunks) {
            if (chunk[0].equals("@")) {
                map.get(currentRoot).add(chunk);
            } else {
                currentRoot = chunk;
                map.put(currentRoot, new ArrayList<>());
            }
        }

        map.entrySet().removeIf(e -> e.getValue().isEmpty());

        FileWriter writer = new FileWriter(home + File.separatorChar + "1_1_all_fullalpha2.txt");
        for (Map.Entry<String[], List<String[]>> entry : map.entrySet()) {
            writer.write("\t" + format(String.join("\t", entry.getKey())) + "\n");
            for (String[] value : entry.getValue()) {
                writer.write("\t" + format(String.join("\t", value)) + "\n");
            }
        }
        writer.close();

        Map<String, String> keyValues = new LinkedHashMap<>();
        map.forEach((key, value) -> value.forEach(v -> {
            if (!v[2].equalsIgnoreCase(key[0])) {
                keyValues.put(v[2], key[0]);
            }
        }));

        FileWriter writer2 = new FileWriter(home + File.separatorChar + "1_1_all_fullalpha3.txt");
        for (Map.Entry<String, String> entry : keyValues.entrySet()) {
            writer2.write(format(entry.getKey()) + " \t" + format(entry.getValue()) + "\n");
        }
        writer2.close();

        for (Map.Entry<String, String> entry : keyValues.entrySet()) {
            varianceDAO.save(new Variance(null, entry.getKey(), entry.getValue()));
        }
    }

    private static String format(String text) {
        return Jsoup.parse(text).text().replace("_", " ");
    }
}
