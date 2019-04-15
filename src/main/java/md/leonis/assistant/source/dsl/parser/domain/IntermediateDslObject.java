package md.leonis.assistant.source.dsl.parser.domain;

import lombok.Getter;
import lombok.Setter;
import md.leonis.assistant.source.dsl.domain.parsed.DslGroup;

import java.util.*;
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
    private List<String> tags2Seq = new ArrayList<>();
    private String notes = null; //TODO deep parse to chunks
    private List<String> vars = new ArrayList<>();
    private List<String> link1 = new ArrayList<>();
    private String link1Green = null;
    private String link2 = null;
    private Map<String, Map<String, List<String>>> linkAddress = new LinkedHashMap<>();
    private List<String> linkSeq = new ArrayList<>();
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

    //  Link1
    
    public void addLink1Groups(List<String> chunks) {
        for (String chunk : chunks) {
            //linkAddress.put(RomanToNumber.romanToDecimal(chunk), new LinkedHashMap<>());
            linkAddress.put(chunk, new LinkedHashMap<>());
        }
        linkSeq.addAll(chunks);
    }

    private Map<String, List<String>> getLastLink1Group() {
        if (linkAddress.isEmpty()) {
            linkAddress.put("", new LinkedHashMap<>());
        }
        String lastElement = null;
        for (String s : linkAddress.keySet()) {
            lastElement = s;
        }
        return linkAddress.get(lastElement);
    }

    public void addLink1Meanings(List<String> chunks) {
        Map<String, List<String>> link1Group = getLastLink1Group();
        for (String chunk : chunks) {
            //link1Group.put(Integer.valueOf(chunk), new ArrayList<>());
            link1Group.put(chunk, new ArrayList<>());
        }
        linkSeq.addAll(chunks);
    }

    private List<String> getLastLink1Meaning() {
        if (getLastLink1Group().isEmpty()) {
            getLastLink1Group().put("", new ArrayList<>());
        }
        String lastElement = null;
        for (String s : getLastLink1Group().keySet()) {
            lastElement = s;
        }
        return getLastLink1Group().get(lastElement);

    }

    public void addLink1Numbers(List<String> chunks) {
        List<String> link1Meaning = getLastLink1Meaning();
        for (String chunk : chunks) {
            //Integer n = Integer.valueOf(chunk.replace(")", ""));
            //link1Meaning.add(chunk.replace(")", ""));
            link1Meaning.add(chunk);
        }
        linkSeq.addAll(chunks);
    }

    public String toM1String() {
        StringBuilder result = new StringBuilder(String.format("[m1]%s", newWord));
        if (!tags1.isEmpty()) {
            result.append(" ").append(tags1.stream().map(p -> String.format("%s%s%s", PTAG.getKey(), p, PTAG.getValue())).collect(Collectors.joining(" ")));
        }
        if (transcription != null) {
            result.append(String.format(" %s%s%s", TRANSCRIPTION.getKey(), transcription, TRANSCRIPTION.getValue()));
        }
        /*if (!tags2.isEmpty()) {
            result.append(" ").append(tags2.stream().map(p -> String.format("%s%s%s", PTAG.getKey(), p, PTAG.getValue())).collect(Collectors.joining(" ")));
        }*/

        for (String tag2 : tags2) {
            result.append(String.format(" %s%s%s", PTAG.getKey(), tag2, PTAG.getValue()));
            int index = tags2Seq.indexOf(tag2);
            if (index + 1 != tags2Seq.size()) {
                if (tags2Seq.get(index + 1).startsWith("[")) { // [i]и[/i]
                    result.append(" ").append(tags2Seq.get(index + 1));
                }
            }
        }

        if (!vars.isEmpty()) {
            result.append(" ").append(VARS.getKey()).append(String.join("; ", vars)).append(VARS.getValue());
        }
        if (notes != null) {
            result.append(String.format(" %s%s%s", NOTES.getKey(), notes, NOTES.getValue()));
        }

        if (link1Green != null) {
            result.append(" " + LINK_PRE).append(String.format(" %s%s%s", LINK_GREEN.getKey(), link1Green, LINK_GREEN.getValue()));
        }

        if (!link1.isEmpty()) {
            result.append(" " + LINK_PRE).append(String.format(" %s%s%s", LINK.getKey(), link1.get(0), LINK.getValue()));
            if (link1.size() > 1) {
                result.append(LINK_U).append(String.format(" %s%s%s", LINK.getKey(), link1.get(1), LINK.getValue()));
            }
        }

        if (link2 != null) {
            result.append(String.format(" %s%s%s", LINK2.getKey(), link2, LINK2.getValue()));
        }


        for (Map.Entry<String, Map<String, List<String>>> group : linkAddress.entrySet()) {
            if (!group.getKey().isEmpty() && linkSeq.contains(group.getKey())) {
                String comma = group.getValue().isEmpty() ? "": ",";
                result.append(String.format(" %s%s%s%s", CBLUE.getKey(), group.getKey(), comma, CBLUE.getValue()));
            }
            String meanings = group.getValue().keySet().stream().filter(m -> !m.isEmpty() && linkSeq.contains(m)).collect(Collectors.joining(", "));
            if (!meanings.isEmpty()) {
                String comma = group.getValue().entrySet().iterator().next().getValue().isEmpty() ? "": ",";
                result.append(String.format(" %s%s%s%s", CBLUE.getKey(), meanings, comma, CBLUE.getValue()));
            }
            for (Map.Entry<String, List<String>> meaning : group.getValue().entrySet()) {
                /*if (!meaning.getKey().isEmpty() && linkSeq.contains(meaning.getKey())) {
                    result.append(String.format(" %s%s%s", CBLUE.getKey(), meaning.getKey(), CBLUE.getValue()));
                }*/
                /*result.append(meaning.getValue().stream().filter(m -> !m.isEmpty() && linkSeq.contains(m))
                        .map(n -> String.format(" %s%s%s", CBLUE.getKey(), n, CBLUE.getValue())).collect(Collectors.joining(" [i]и[/i]"))
                );*/
                /*if (!numbers.isEmpty()) {
                    result.append(String.format(" %s%s%s", CBLUE.getKey(), meanings, CBLUE.getValue()));
                }*/
                for (String number : meaning.getValue()) {
                    if (!number.isEmpty() && linkSeq.contains(number)) {
                        int index = linkSeq.indexOf(number);
                        if (index + 1 != linkSeq.size()) {
                            if (linkSeq.get(index + 1).startsWith("[")) { // no comma, append with tag
                                result.append(String.format(" %s%s%s", CBLUE.getKey(), number, CBLUE.getValue()));
                                result.append(" ").append(linkSeq.get(index + 1));
                            } else {
                                result.append(String.format(" %s%s,%s", CBLUE.getKey(), number, CBLUE.getValue()));
                            }
                        } else {
                            result.append(String.format(" %s%s%s", CBLUE.getKey(), number, CBLUE.getValue()));
                        }
                    }
                }
            }
        }

        if (tail != null) {
            result.append(tail);
        }
        return result.toString();
    }
}
