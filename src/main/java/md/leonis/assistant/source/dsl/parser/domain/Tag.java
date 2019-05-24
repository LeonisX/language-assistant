package md.leonis.assistant.source.dsl.parser.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Tag {

    private TagType type;
    private String word;

    public Tag(String word) {
        this.type = TagType.NONE;
        this.word = word;
    }

    @Override
    public String toString() {
        return type.getOpening() + word + type.getClosing();
    }
}
