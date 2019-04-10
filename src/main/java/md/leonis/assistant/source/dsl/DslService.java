package md.leonis.assistant.source.dsl;

import com.fasterxml.jackson.databind.ObjectMapper;
import md.leonis.assistant.domain.test.WordLevel;
import md.leonis.assistant.source.Crawler;
import md.leonis.assistant.source.Parser;
import md.leonis.assistant.source.dsl.dao.aParsedRawDataDAO;
import md.leonis.assistant.source.dsl.dao.aRawDAO;
import md.leonis.assistant.source.dsl.domain.ParsedRawData;
import md.leonis.assistant.source.dsl.domain.Raw;
import md.leonis.assistant.source.dsl.domain.parsed.RawContainer;
import md.leonis.assistant.source.dsl.domain.parsed.RawData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DslService implements md.leonis.assistant.source.Service {

    private static final Logger log = LoggerFactory.getLogger(DslService.class);

    @Autowired
    private aRawDAO aRawDAO;

    @Autowired
    private aParsedRawDataDAO aParsedRawDataDAO;

    @Autowired
    private DslParser parser;

    @Autowired
    private DslCrawler crawler;

    public long getRawCount() {
        return aRawDAO.count();
    }

    public void saveRaw(Raw raw) {
        aRawDAO.save(raw);
    }

    public Iterable<Raw> findAllRaw() {
        return aRawDAO.findAll();
    }

    public Optional<Raw> findRawById(long id) {
        return aRawDAO.findById(id);
    }

    @Override
    public Crawler getCrawler() {
        return crawler;
    }

    @Override
    public boolean isCrawled() {
        return crawler.isCrawled();
    }

    @Override
    public String getCrawlerStatus() {
        return crawler.getStatus();
    }

    @Override
    public Parser getParser() {
        return parser;
    }

    public ParsedRawData saveParsedRawData(ParsedRawData parsedRawData) {
        return aParsedRawDataDAO.save(parsedRawData);
    }

    public long getRawDataCount() {
        return aParsedRawDataDAO.count();
    }

    public List<ParsedRawData> getParsedRawData() {
        return aParsedRawDataDAO.findAll();
    }

    public List<String> findAllWords() {
        return aParsedRawDataDAO.findAllWords();
    }

    @Override
    public boolean isParsed() {
        return parser.isParsed();
    }

    @Override
    public String getParserStatus() {
        return parser.getStatus();
    }

    @Override
    public List<WordLevel> getWordLevels() {
        ObjectMapper objectMapper = new ObjectMapper();
        return StreamSupport.stream(findAllRaw().spliterator(), false)
                .flatMap(r -> {
                    try {
                        return objectMapper.readValue(r.getRaw(), RawContainer.class).getData().stream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).map(RawData::toWordLevel).collect(Collectors.toList());
    }

}
