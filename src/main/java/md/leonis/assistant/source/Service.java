package md.leonis.assistant.source;

import md.leonis.assistant.domain.test.WordLevel;

import java.util.List;

public interface Service {

    Crawler getCrawler();

    boolean isCrawled();

    String getCrawlerStatus();

    Parser getParser();

    boolean isParsed();

    String getParserStatus();

    default String getStatus() {
        return String.format("Crawled: %s; Parsed: %s", getCrawlerStatus(), getParserStatus());
    }

    List<WordLevel> getWordLevels();
}
