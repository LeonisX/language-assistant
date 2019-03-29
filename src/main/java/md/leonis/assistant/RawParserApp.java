package md.leonis.assistant;

import com.fasterxml.jackson.databind.ObjectMapper;
import md.leonis.assistant.dao.bank.RawDAO;
import md.leonis.assistant.dao.test.ParsedRawDataDAO;
import md.leonis.assistant.dao.test.WordLevelDAO;
import md.leonis.assistant.domain.bank.raw.RawContainer;
import md.leonis.assistant.domain.bank.raw.RawData;
import md.leonis.assistant.domain.test.WordLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SpringBootApplication
public class RawParserApp {

    private static final Logger log = LoggerFactory.getLogger(RawParserApp.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext springContext = SpringApplication.run(RawParserApp.class, args);

        log.info("Parsing...");

        RawDAO rawDAO = springContext.getBean(RawDAO.class);

        ObjectMapper objectMapper = new ObjectMapper();

        if (3492 != rawDAO.count()) {
            throw new RuntimeException();
        }

        // 3492
        /*for (long i = 1; i <= rawDAO.count(); i++) {
            String json = rawDAO.findById(i).get().getRaw();

            RawContainer rawContainer = objectMapper.readValue(json, RawContainer.class);
            System.out.println(i);
            List<RawData> rawData = rawContainer.getData();
            if (34911 != rawContainer.getCount()) {
                throw new RuntimeException();
            }

        }*/

        ParsedRawDataDAO parsedRawDataDAO = springContext.getBean(ParsedRawDataDAO.class);

        List<WordLevel> wordLevels = StreamSupport.stream(rawDAO.findAll().spliterator(), false)
                .flatMap(r -> {
                    try {
                        return objectMapper.readValue(r.getRaw(), RawContainer.class).getData().stream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .peek(r -> parsedRawDataDAO.save(r.toParsedRawData()))
                .map(RawData::toWordLevel).collect(Collectors.toList());

        WordLevelDAO wordLevelDAO = springContext.getBean(WordLevelDAO.class);
        wordLevelDAO.saveAll(wordLevels);
    }
}
