package md.leonis.assistant.source.dsl.parser.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Abbr {

    private String lang;
    private String name;

    public Abbr(String lang) {
        this.lang = lang;
    }

    @Override
    public String toString() {
        return "{" + "" + lang + ", " + name + '}';
    }
}
