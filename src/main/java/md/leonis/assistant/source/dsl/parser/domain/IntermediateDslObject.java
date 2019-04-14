package md.leonis.assistant.source.dsl.parser.domain;

import lombok.Getter;
import lombok.Setter;
import md.leonis.assistant.source.dsl.domain.parsed.DslGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static md.leonis.assistant.source.dsl.parser.M1Parser.*;

@Getter
@Setter
public class IntermediateDslObject {

    private String word;
    private String newWord;
    private String transcription = null;
    private List<String> tags1 = new ArrayList<>();
    private List<String> tags2 = new ArrayList<>();
    private String notes = null; //TODO deep parse to chunks
    private List<String> vars = new ArrayList<>();
    private List<String> link1 = new ArrayList<>();
    private String link1Group = null;
    private String link1Meaning = null;
    private String link1Number = null;
    private String link2 = null;
    private String tail = null;
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

    @Override
    public String toString() {
        String result = String.format("[m1]%s", newWord);
        if (!tags1.isEmpty()) {
            result += " " + tags1.stream().map(p -> String.format("%s%s%s", PTAG.getKey(), p, PTAG.getValue())).collect(Collectors.joining(" "));
        }
        if (transcription != null) {
            result += String.format(" %s%s%s", TRANSCRIPTION.getKey(), transcription, TRANSCRIPTION.getValue());
        }
        if (!tags2.isEmpty()) {
            result += " " + tags2.stream().map(p -> String.format("%s%s%s", PTAG.getKey(), p, PTAG.getValue())).collect(Collectors.joining(" "));
        }
        if (!vars.isEmpty()) {
            result += " " + VARS.getKey() + String.join("; ", vars) + VARS.getValue();
        }
        if (notes != null) {
            result += String.format(" %s%s%s", NOTES.getKey(), notes, NOTES.getValue());
        }
        if (!link1.isEmpty()) {
            result += " " + LINK_PRE + String.format(" %s%s%s", LINK.getKey(), link1.get(0), LINK.getValue());
            if (link1.size() > 1) {
                result += LINK_U + String.format(" %s%s%s", LINK.getKey(), link1.get(1), LINK.getValue());
            }
        }
        if (link1Group != null) {
            result += String.format(" %s%s%s", LINK_GROUP.getKey(), link1Group, LINK_GROUP.getValue());
        }
        if (link2 != null) {
            result += String.format(" %s%s%s", LINK2.getKey(), link2, LINK2.getValue());
        }
        if (tail != null) {
            result += tail;
        }
        return result;
    }
}
