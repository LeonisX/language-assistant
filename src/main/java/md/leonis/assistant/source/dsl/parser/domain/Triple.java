package md.leonis.assistant.source.dsl.parser.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Triple {

    private String key;
    private String body;
    private String value;

    @Override
    public String toString() {
        return key + body + value;
    }
}
