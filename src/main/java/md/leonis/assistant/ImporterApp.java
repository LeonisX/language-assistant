package md.leonis.assistant;

import md.leonis.assistant.dao.standard.WordFrequencyDAO;
import md.leonis.assistant.dao.standard.WordLevelDAO;
import md.leonis.assistant.dao.standard.WordPlaceDAO;
import md.leonis.assistant.domain.standard.WordFrequency;
import md.leonis.assistant.domain.standard.WordPlace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

@SpringBootApplication
public class ImporterApp {

    private static final Logger log = LoggerFactory.getLogger(ImporterApp.class);

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext springContext = SpringApplication.run(ImporterApp.class, args);

        log.info("Importing");

        WordFrequencyDAO wordFrequencyDAO = springContext.getBean(WordFrequencyDAO.class);
        WordPlaceDAO wordPlaceDAO = springContext.getBean(WordPlaceDAO.class);
        WordLevelDAO wordLevelDAO = springContext.getBean(WordLevelDAO.class);

        wordFrequencyDAO.deleteAll();
        wordPlaceDAO.deleteAll();
        wordLevelDAO.deleteAll();

        ClassLoader classLoader = ImporterApp.class.getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("banks/nor.txt")).getFile());
        List<String> list = Files.readAllLines(file.toPath(), Charset.defaultCharset());

        long place = 1;
        for (String nextLine : list) {
            String line = nextLine.trim();
            if (!line.isEmpty() && !line.startsWith("#")) {
                String[] chunks = line.split("\t");
                WordFrequency wordFrequency = new WordFrequency(chunks[0], Long.parseLong(chunks[1]));
                WordPlace wordPlace = new WordPlace(chunks[0], place++);
                wordFrequencyDAO.save(wordFrequency);
                wordPlaceDAO.save(wordPlace);
            }
        }

        System.out.println(wordFrequencyDAO.count());
        System.out.println(wordPlaceDAO.count());
    }
}
