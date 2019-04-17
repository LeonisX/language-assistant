package md.leonis.assistant.source.dsl.parser.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Translation {

    private String word;
    private boolean nearly;
    private List<Example> examples;
}
