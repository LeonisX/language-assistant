package md.leonis.assistant.source.dsl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import md.leonis.assistant.source.Crawler;
import md.leonis.assistant.source.dsl.domain.DslRaw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DslCrawler implements Crawler {

    private static final Logger log = LoggerFactory.getLogger(DslCrawler.class);

    private static final Random random = new Random();

    @Value("${dsl.path}")
    private String dictPath;

    @Value("${dsl.abbr.path}")
    private String abbrPath;

    @Value("${dsl.dictionary.file}")
    private String dictFileName;

    //TODO do not parse now. info about dictionary (ex. lang, records count,...), XML
    //TODO in future get info and use somehow
    @Value("${dsl.description.file}")
    private String descrFileName;

    //TODO get from descrFileName <entry key="articleCount">48872</entry>
    @Value("${dsl.total.count}")
    private int totalCount;

    @Lazy
    @Autowired
    private DslService dslService;

    @Override
    @SneakyThrows
    public void crawl() {

        log.info("Crawling");

        //TODO unify
        String home = System.getProperty("user.home");

        File file = new File(home + File.separatorChar + dictPath + File.separatorChar + dictFileName);
        List<String> allLines = Files.readAllLines(file.toPath(), Charset.forName("utf8"));

        String word = null;
        List<String> lines = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();

        dslService.clearRaw();

        for (String line : allLines) {
            if (isWord(line)) {
                if (null != word) {
                    //TODO save to db
                    DslRaw dslRaw = new DslRaw(null, word, objectMapper.writeValueAsString(lines));
                    dslService.saveRaw(dslRaw);
                    lines.clear();
                }
                word = line;
            }
            if (isData(line)) {
                lines.add(line);
            }
        }

        DslRaw dslRaw = new DslRaw(null, word, objectMapper.writeValueAsString(lines));
        dslService.saveRaw(dslRaw);

        for (DslRaw raw : dslService.findAllRaw()) {
            System.out.println(raw.getWord());
            List<String> jsonLines = objectMapper.readValue(raw.getRaw(), new TypeReference<List<String>>() {});
            jsonLines.forEach(l -> System.out.println("\t" + l));
        }
    }

    private boolean isWord(String line) {
        return !line.trim().isEmpty() && !line.startsWith("\t");
    }

    private boolean isData(String line) {
        return !line.trim().isEmpty() && line.startsWith("\t");
    }

    @Override
    public boolean isCrawled() {
        return dslService.getRawCount() == totalCount;
    }

    @Override
    public String getStatus() {
        if (isCrawled()) {
            return "OK";
        } else {
            return String.format("%d/%d", dslService.getRawCount(), totalCount);
        }
    }
}
