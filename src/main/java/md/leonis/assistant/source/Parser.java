package md.leonis.assistant.source;

public interface Parser {

    void parse();

    boolean isParsed();

    String getStatus();
}
