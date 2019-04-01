package md.leonis.assistant.source.gse;

import md.leonis.assistant.domain.LanguageLevel;
import md.leonis.assistant.source.Crawler;
import md.leonis.assistant.source.Parser;
import md.leonis.assistant.source.Service;
import md.leonis.assistant.source.SourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class GseSourceFactory implements SourceFactory {

    @Autowired
    private GseService service;

    @Override
    public Crawler getCrawler() {
        return service.getCrawler();
    }

    @Override
    public Parser getParser() {
        return service.getParser();
    }

    @Override
    public Service getService() {
        return service;
    }

    @Override
    public LanguageLevel[] getLanguageLevels() {
        return new LanguageLevel[]{
                LanguageLevel.UNK,
                LanguageLevel.A0,
                LanguageLevel.A1,
                LanguageLevel.A2,
                LanguageLevel.A2P,
                LanguageLevel.B1,
                LanguageLevel.B1P,
                LanguageLevel.B2,
                LanguageLevel.B2P,
                LanguageLevel.C1,
                LanguageLevel.C2
                //LanguageLevel.C2P,
        };
    }

    @Override
    public Set<LanguageLevel> getLanguageLevelsSet() {
        return new HashSet<>(Arrays.asList(getLanguageLevels()));
    }
}
