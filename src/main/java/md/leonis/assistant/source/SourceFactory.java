package md.leonis.assistant.source;

import md.leonis.assistant.domain.LanguageLevel;

public interface SourceFactory {

    Crawler getCrawler();

    Parser getParser();

    Service getService();

    LanguageLevel[] getLanguageLevels();

    //TODO matchers
}
