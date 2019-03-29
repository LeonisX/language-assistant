package md.leonis.assistant.source.gse;

import md.leonis.assistant.source.SourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GseSourceFactory implements SourceFactory {

    @Autowired
    private GseCrawler gseCrawler;

    @Autowired
    private GseParser gseParser;

    public GseCrawler getCrawler() {
        return new GseCrawler();
    }

    public GseParser getParser() {
        return new GseParser();
    }
}
