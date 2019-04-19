package md.leonis.assistant.source.dsl.parser.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class Plural {

    private List<String> words;
    private String transcription = null;

    public Plural(String notes) {
        this.words = Arrays.stream(notes.split(",")).map(String::trim).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "{" + words + ", " + transcription + '}';
    }
}
