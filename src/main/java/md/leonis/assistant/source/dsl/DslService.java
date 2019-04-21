package md.leonis.assistant.source.dsl;

import com.fasterxml.jackson.databind.ObjectMapper;
import md.leonis.assistant.domain.test.WordLevel;
import md.leonis.assistant.source.Crawler;
import md.leonis.assistant.source.Parser;
import md.leonis.assistant.source.dsl.dao.DslRawAbbrDAO;
import md.leonis.assistant.source.dsl.dao.DslRawAbbrParsedDAO;
import md.leonis.assistant.source.dsl.dao.DslRawDAO;
import md.leonis.assistant.source.dsl.dao.DslRawParsedDAO;
import md.leonis.assistant.source.dsl.domain.DslRaw;
import md.leonis.assistant.source.dsl.domain.DslRawAbbr;
import md.leonis.assistant.source.dsl.domain.parsed.DslRawAbbrParsed;
import md.leonis.assistant.source.dsl.domain.parsed.DslRawParsed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DslService implements md.leonis.assistant.source.Service {

    private static final Logger log = LoggerFactory.getLogger(DslService.class);

    @Autowired
    private DslRawDAO dslRawDAO;

    @Autowired
    private DslRawParsedDAO dslRawParsedDAO;

    @Autowired
    private DslRawAbbrDAO dslRawAbbrDAO;

    @Autowired
    private DslRawAbbrParsedDAO dslRawAbbrParsedDAO;

    @Autowired
    private DslParser parser;

    @Autowired
    private DslCrawler crawler;

    public long getRawCount() {
        return dslRawDAO.count();
    }

    public void saveRaw(DslRaw dslRaw) {
        dslRawDAO.save(dslRaw);
    }

    public Iterable<DslRaw> findAllRaw() {
        return dslRawDAO.findAll();
    }

    public Optional<DslRaw> findRawById(long id) {
        return dslRawDAO.findById(id);
    }

    public void clearRaw() {
        dslRawDAO.deleteAll();
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
                //TODO try to parse and return meanings count
                /*.flatMap(r -> {
                    try {
                        return objectMapper.readValue(r.getRaw(), RawContainer.class).getData().stream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })*/
                .map(DslRaw::toWordLevel).collect(Collectors.toList());
    }

    //TODO
    public long getRawDataCount() {
        return 0;
        //return parsedRawDataDAO.count();
    }

    public long getRawAbbrCount() {
        return dslRawAbbrDAO.count();
    }

    public void saveRawAbbr(DslRawAbbr dslRaw) {
        dslRawAbbrDAO.save(dslRaw);
    }

    public Iterable<DslRawAbbr> findAllRawAbbr() {
        return dslRawAbbrDAO.findAll();
    }

    public Optional<DslRawAbbr> findRawAbbrById(long id) {
        return dslRawAbbrDAO.findById(id);
    }

    public void clearRawAbbr() {
        dslRawAbbrDAO.deleteAll();
    }

    public void clearRawAbbrParsed() {
        dslRawAbbrParsedDAO.deleteAll();
    }

    public void saveRawAbbrParsed(DslRawAbbrParsed dslRawAbbrParsed) {
        dslRawAbbrParsedDAO.save(dslRawAbbrParsed);
    }

    public  Iterable<DslRawAbbrParsed> findAllRawParsedAbbr() {
        return dslRawAbbrParsedDAO.findAll();
    }

    public void clearRawParsed() {
        dslRawParsedDAO.deleteAll();
    }

    public void saveRawParsed(DslRawParsed dslRawParsed) {
        dslRawParsedDAO.save(dslRawParsed);
    }

}
