package md.leonis.assistant.source.dsl.parser.domain;

import lombok.Getter;

//TODO add others
@Getter
public enum TagType {

    I("[i]", "[/i]"),
    P("[p]", "[/p]"),

    NONE("", "");

    private String opening;
    private String closing;

    TagType(String opening, String closing) {
        this.opening = opening;
        this.closing = closing;
    }
}
