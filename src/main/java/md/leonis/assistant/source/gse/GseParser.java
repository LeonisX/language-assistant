package md.leonis.assistant.source.gse;

import com.fasterxml.jackson.databind.ObjectMapper;
import md.leonis.assistant.dao.bank.RawDAO;
import md.leonis.assistant.dao.test.ParsedRawDataDAO;
import md.leonis.assistant.dao.test.WordLevelDAO;
import md.leonis.assistant.domain.test.WordLevel;
import md.leonis.assistant.source.Parser;
import md.leonis.assistant.source.gse.domain.parsed.RawContainer;
import md.leonis.assistant.source.gse.domain.parsed.RawData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class GseParser implements Parser {

    private static final Logger log = LoggerFactory.getLogger(GseParser.class);

    @Autowired
    private RawDAO rawDAO;

    @Autowired
    private ParsedRawDataDAO parsedRawDataDAO;

    @Autowired
    private WordLevelDAO wordLevelDAO;

    public void parse() {

        log.info("Parsing...");

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

        wordLevelDAO.saveAll(wordLevels);
    }
}
