package md.leonis.assistant.source.dsl.parser.domain;

import lombok.Getter;
import lombok.Setter;
import md.leonis.assistant.source.dsl.domain.parsed.DslGroup;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class IntermediateDslObject {

    private String word;
    private String transcription = null;
    private List<String> tags = new ArrayList<>();
    private String notes = null; //TODO deep parse to chunks
    private String link = null;
    private String link2 = null;
    private List<DslGroup> dslGroups = new ArrayList<>();
    private ParserState state = ParserState.M0;

    public IntermediateDslObject(String word) {
        this.word = word;
        addNewGroup();
    }

    public DslGroup getCurrentGroup() {
        return dslGroups.get(dslGroups.size() - 1);
    }

    public void addNewGroup() {
        dslGroups.add(new DslGroup());
    }

}