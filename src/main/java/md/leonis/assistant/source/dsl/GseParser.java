package md.leonis.assistant.source.dsl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import md.leonis.assistant.source.Parser;
import md.leonis.assistant.source.dsl.domain.parsed.RawContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.stream.StreamSupport;

@Component
public class GseParser implements Parser {

    private static final Logger log = LoggerFactory.getLogger(GseParser.class);

    @Lazy
    @Autowired
    private GseService gseService;

    @Value("${gse.pages.count}")
    private int pagesCount;

    @Value("${gse.total.count}")
    private int totalCount;

    @SneakyThrows
    public void parse() {
        log.info("Parsing...");

        ObjectMapper objectMapper = new ObjectMapper();

        if (pagesCount != gseService.getRawCount()) {
            throw new RuntimeException();
        }

        // verify
        for (long i = 1; i <= pagesCount; i++) {
            String json = gseService.findRawById(i).get().getRaw();
            RawContainer rawContainer = objectMapper.readValue(json, RawContainer.class);
            if (totalCount != rawContainer.getCount()) {
                throw new RuntimeException();
            }
        }

        StreamSupport.stream(gseService.findAllRaw().spliterator(), false)
                .flatMap(r -> {
                    try {
                        return objectMapper.readValue(r.getRaw(), RawContainer.class).getData().stream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .forEach(r -> gseService.saveParsedRawData(r.toParsedRawData()));
    }

    @Override
    public boolean isParsed() {
        return totalCount == gseService.getRawDataCount();
    }

    @Override
    public String getStatus() {
        if (isParsed()) {
            return "OK";
        } else {
            return String.format("%d/%d", gseService.getRawDataCount(), totalCount);
        }
    }
}
