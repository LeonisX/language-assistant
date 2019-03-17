package md.leonis.assistant.domain;

import lombok.Getter;

@Getter
public class ScriptWord {

    private String word;
    private long frequency;
    private LanguageLevel level;
    private String tag;

    public ScriptWord(String word, LanguageLevel level, String tag) {
        this.word = word;
        this.frequency = 1;
        this.level = level;
        this.tag = tag;
    }

    public void increment() {
        ++frequency;
    }
}
