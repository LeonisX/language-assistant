package md.leonis.assistant.source;

import md.leonis.assistant.domain.LanguageLevel;

import java.util.Set;

public interface SourceFactory {

    Crawler getCrawler();

    Parser getParser();

    Service getService();

    LanguageLevel[] getLanguageLevels();

    Set<LanguageLevel> getLanguageLevelsSet();

    //TODO matchers
}
