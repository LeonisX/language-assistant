package md.leonis.assistant.domain;

import lombok.Getter;

@Getter
public class ScriptWord {

    private String title;
    private long frequency;
    private LanguageLevel level;
    private String tag;

    public ScriptWord(String title, LanguageLevel level, String tag) {
        this.title = title;
        this.frequency = 1;
        this.level = level;
        this.tag = tag;
    }

    public void increment() {
        ++frequency;
    }
}
