package md.leonis.assistant.source.dsl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import md.leonis.assistant.source.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class DslParser implements Parser {

    private static final Logger log = LoggerFactory.getLogger(DslParser.class);

    @Lazy
    @Autowired
    private DslService dslService;

    @Value("${dsl.total.count}")
    private int totalCount;

    @SneakyThrows
    public void parse() {
        log.info("Parsing...");

        ObjectMapper objectMapper = new ObjectMapper();

        /*if (pagesCount != dslService.getRawCount()) {
            throw new RuntimeException();
        }*/

        // verify
        /*for (long i = 1; i <= pagesCount; i++) {
            String json = dslService.findRawById(i).get().getRaw();
            RawContainer rawContainer = objectMapper.readValue(json, RawContainer.class);
            if (totalCount != rawContainer.getCount()) {
                throw new RuntimeException();
            }
        }

        StreamSupport.stream(dslService.findAllRaw().spliterator(), false)
                .flatMap(r -> {
                    try {
                        return objectMapper.readValue(r.getRaw(), RawContainer.class).getData().stream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .forEach(r -> dslService.saveParsedRawData(r.toParsedRawData()));*/
    }

    @Override
    public boolean isParsed() {
        return totalCount == dslService.getRawDataCount();
    }

    @Override
    public String getStatus() {
        if (isParsed()) {
            return "OK";
        } else {
            return String.format("%d/%d", dslService.getRawDataCount(), totalCount);
        }
    }
}
