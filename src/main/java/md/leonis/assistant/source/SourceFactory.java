package md.leonis.assistant.source;

public interface SourceFactory {

    Crawler getCrawler();

    Parser getParser();

    //TODO matchers, levels
}
