package md.leonis.assistant.source;

public interface Crawler {

    void crawl();

    boolean isCrawled();

    String getStatus();
}
