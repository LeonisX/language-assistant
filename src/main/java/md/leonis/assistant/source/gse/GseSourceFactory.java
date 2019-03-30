package md.leonis.assistant.source.gse;

import md.leonis.assistant.source.Crawler;
import md.leonis.assistant.source.Parser;
import md.leonis.assistant.source.Service;
import md.leonis.assistant.source.SourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}
