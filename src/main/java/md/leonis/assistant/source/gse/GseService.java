package md.leonis.assistant.source.gse;

import com.fasterxml.jackson.databind.ObjectMapper;
import md.leonis.assistant.source.gse.domain.ParsedRawData;
import md.leonis.assistant.domain.test.WordLevel;
import md.leonis.assistant.source.Crawler;
import md.leonis.assistant.source.Parser;
import md.leonis.assistant.source.gse.dao.ParsedRawDataDAO;
import md.leonis.assistant.source.gse.dao.RawDAO;
import md.leonis.assistant.source.gse.domain.Raw;
import md.leonis.assistant.source.gse.domain.parsed.RawContainer;
import md.leonis.assistant.source.gse.domain.parsed.RawData;
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
public class GseService implements md.leonis.assistant.source.Service {

    private static final Logger log = LoggerFactory.getLogger(GseService.class);

    @Autowired
    private RawDAO rawDAO;

    @Autowired
    private ParsedRawDataDAO parsedRawDataDAO;

    @Autowired
    private GseParser parser;

    @Autowired
    private GseCrawler crawler;

    public long getRawCount() {
        return rawDAO.count();
    }

    public void saveRaw(Raw raw) {
        rawDAO.save(raw);
    }

    public Iterable<Raw> findAllRaw() {
        return rawDAO.findAll();
    }

    public Optional<Raw> findRawById(long id) {
        return rawDAO.findById(id);
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
        return parsedRawDataDAO.save(parsedRawData);
    }

    public long getRawDataCount() {
        return parsedRawDataDAO.count();
    }

    public List<ParsedRawData> getParsedRawData() {
        return parsedRawDataDAO.findAll();
    }

    public List<String> findAllWords() {
        return parsedRawDataDAO.findAllWords();
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
