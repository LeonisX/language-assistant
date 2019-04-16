package md.leonis.assistant.source.dsl.parser.domain;

import lombok.Getter;
import lombok.Setter;
import md.leonis.assistant.source.dsl.domain.parsed.DslGroup;
import md.leonis.assistant.source.dsl.parser.StringUtils;

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
    private List<Link> links = new ArrayList<>();
    private List<String> linkSeq = new ArrayList<>();
    private String linksSep = null;
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

    public void addLinkGroups(List<String> chunks) {
        links.get(links.size() - 1).addLinkGroups(chunks);
        linkSeq.addAll(chunks);
    }

    public void addLinkMeanings(List<String> chunks) {
        links.get(links.size() - 1).addLinkMeanings(chunks);
        linkSeq.addAll(chunks);
    }

    public void addLinkNumbers(List<String> chunks) {
        links.get(links.size() - 1).addLinkNumbers(chunks);
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

        if (!links.isEmpty()) {
            switch (links.get(0).getType()) {
                case EQ_ONE:
                case EQ_GREEN:
                    result.append(" " + LINK_PRE);
                    break;
                case FROM_TWO:
                    result.append(" " + LINK2_PRE);
                    break;
            }

            for (Link link : links) {
                switch (links.get(0).getType()) {
                    case EQ_ONE:
                    case FROM_TWO:
                        result.append(String.format(" %s%s%s", LINKR.getKey(), link.getName(), LINKR.getValue()));
                        break;
                    case EQ_GREEN:
                        result.append(String.format(" %s%s%s", LINK_GREENR.getKey(), link.getName(), LINK_GREENR.getValue()));
                        break;
                }

                for (Map.Entry<String, Map<String, List<String>>> group : link.getLinkAddress().entrySet()) {
                    if (!group.getKey().isEmpty() && linkSeq.contains(group.getKey())) {
                        String comma = group.getValue().isEmpty() ? "" : ",";
                        result.append(String.format(" %s%s%s%s", CBLUE.getKey(), group.getKey(), comma, CBLUE.getValue()));
                    }
                    String meanings = group.getValue().keySet().stream().filter(m -> !m.isEmpty() && linkSeq.contains(m)).collect(Collectors.joining(", "));
                    if (!meanings.isEmpty()) {
                        /*String space = link.getType() == LinkType.FROM_TWO ? "" : " ";
                        String space2 = link.getType() == LinkType.FROM_TWO ? " " : "";*/
                        String space = " ";
                        String space2 = "";
                        boolean isLastMeaning = group.getValue().entrySet().iterator().next().getValue().isEmpty();
                        boolean isLastLink = links.indexOf(link) == links.size() - 1;
                        String comma = isLastMeaning ? "" : ",";
                        result.append(space).append(String.format("%s%s%s%s%s", CBLUE.getKey(), space2, meanings, comma, CBLUE.getValue()));
                    }
                    for (Map.Entry<String, List<String>> meaning : group.getValue().entrySet()) {
                        for (String number : meaning.getValue()) {
                            if (!number.isEmpty() && linkSeq.contains(number)) {
                                int index = linkSeq.indexOf(number);
                                if (index + 1 != linkSeq.size()) {
                                    if (linkSeq.get(index + 1).startsWith("[")) { // no comma, append with tag
                                        result.append(String.format(" %s%s%s", CBLUE.getKey(), number, CBLUE.getValue()));
                                        result.append(" ").append(linkSeq.get(index + 1));
                                    } else {
                                        //TODO if last for current link - no comma
                                        result.append(String.format(" %s%s,%s", CBLUE.getKey(), number, CBLUE.getValue()));
                                    }
                                } else {
                                    result.append(String.format(" %s%s%s", CBLUE.getKey(), number, CBLUE.getValue()));
                                }
                            }
                        }
                    }
                }

                if ((links.indexOf(link) < links.size() - 1)/* && (link.getType() == LinkType.EQ_ONE)*/) {
                    if (linksSep != null) {
                        result.append(linksSep);
                    } else {
                        //result.append("[c blue],[/c]");
                    }

                }
            }
        }

        if (tail != null) {
            result.append(tail);
        }
        return result.toString();
    }

    public String toM1CompactString() {
        return StringUtils.compact(toM1String());
    }
}
