package md.leonis.assistant.source.dsl.parser.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

import static md.leonis.assistant.source.dsl.parser.M1Parser.*;
import static md.leonis.assistant.source.dsl.parser.M1Parser.CBLUE;
import static md.leonis.assistant.source.dsl.parser.M1Parser.LINK_SEE_POST;

@Getter
@Setter
public class Link {

    private LinkType type;
    private String word;
    private String transcription;
    private Map<String, Map<String, List<String>>> linkAddress = new LinkedHashMap<>();
    private List<String> seq = new ArrayList<>();
    private String join = null;

    public Link(LinkType type, String word) {
        this.type = type;
        this.word = word;
    }

    /*public Link(String word) {
        this.type = LinkType.NONE;
        this.word = word;
    }*/

    public void addLinkGroups(List<String> chunks) {
        for (String chunk : chunks) {
            linkAddress.put(chunk, new LinkedHashMap<>());
        }
    }

    private Map<String, List<String>> getLastLinkGroup() {
        if (linkAddress.isEmpty()) {
            linkAddress.put("", new LinkedHashMap<>());
        }
        String lastElement = getLastElementId(linkAddress.keySet());
        return linkAddress.get(lastElement);
    }

    public void addLinkMeanings(List<String> chunks) {
        Map<String, List<String>> link1Group = getLastLinkGroup();
        for (String chunk : chunks) {
            link1Group.put(chunk, new ArrayList<>());
        }
    }

    private List<String> getLastLinkMeaning() {
        if (getLastLinkGroup().isEmpty()) {
            getLastLinkGroup().put("", new ArrayList<>());
        }
        String lastElement = getLastElementId(getLastLinkGroup().keySet());
        return getLastLinkGroup().get(lastElement);

    }

    public void addLinkNumbers(List<String> chunks) {
        getLastLinkMeaning().addAll(chunks);
    }

    private String getLastElementId(Set<String> keySet) {
        String lastElement = null;
        for (String s : keySet) {
            lastElement = s;
        }
        return lastElement;
    }

    @Override
    public String toString() {
        String result = type == LinkType.UNDEFINED ? "" : type + ", ";
        result = result + word;
        if (!linkAddress.isEmpty()) {
                result += ", " + linkAddress;
        }
        return result;
    }

    public static String renderLinks(List<Link> links) {
        StringBuilder result = new StringBuilder();
        if (!links.isEmpty()) {

            LinkType prevType = LinkType.UNDEFINED;

            for (Link link : links) {
                if (prevType != link.getType()) {
                    switch (link.getType()) {
                        case EQ_ONE:
                        case EQ_GREEN:
                            result.append(LINK_PRE);
                            break;
                        case FROM_TWO:
                            result.append(LINK2_PRE);
                            break;
                        case SEE_ALSO:
                            result.append(LINK_SEE_ALSO_PRE);
                            break;
                        case SEE:
                            result.append(" ").append(LINK_SEE_PRE);
                            break;
                        case ABBR_FROM:
                            result.append(ABBR).append(" ").append(FROM);
                            break;
                    }
                }

                switch (link.getType()) {
                    case EQ_ONE:
                    case FROM_TWO:
                    case SEE_ALSO:
                    case SEE:
                    case ABBR_FROM:
                    case SIMPLE:
                        result.append(String.format(" %s%s%s", LINKR.getKey(), link.getWord(), LINKR.getValue()));
                        break;
                    case EQ_GREEN:
                        result.append(String.format(" %s%s%s", LINK_GREENR.getKey(), link.getWord(), LINK_GREENR.getValue()));
                        break;
                    case CTEALTAG:
                        result.append(String.format(" %s%s%s", CTEALTAG.getKey(), link.getWord(), CTEALTAG.getValue()));
                        break;
                    case CMEDIUMVIOLET:
                        result.append(String.format(" %s%s%s", CMEDIUMVIOLET.getKey(), link.getWord(), CMEDIUMVIOLET.getValue()));
                        break;
                }

                if (link.getTranscription() != null) {
                    result.append(String.format(" %s%s%s", TRANSCRIPTION.getKey(), link.getTranscription(), TRANSCRIPTION.getValue()));
                }

                for (Map.Entry<String, Map<String, List<String>>> group : link.getLinkAddress().entrySet()) {
                    if (!group.getKey().isEmpty() && link.getSeq().contains(group.getKey())) {
                        String comma = group.getValue().isEmpty() ? "" : ",";
                        result.append(String.format(" %s%s%s%s", CBLUE.getKey(), group.getKey(), comma, CBLUE.getValue()));
                    }
                    String meanings = group.getValue().keySet().stream().filter(m -> !m.isEmpty() && link.getSeq().contains(m)).collect(Collectors.joining(", "));
                    if (!meanings.isEmpty()) {
                        String space = " ";
                        String space2 = "";
                        boolean isLastMeaning = group.getValue().entrySet().iterator().next().getValue().isEmpty();
                        boolean isLastLink = links.indexOf(link) == links.size() - 1;
                        String comma = isLastMeaning ? "" : ",";
                        result.append(space).append(String.format("%s%s%s%s%s", CBLUE.getKey(), space2, meanings, comma, CBLUE.getValue()));
                    }
                    for (Map.Entry<String, List<String>> meaning : group.getValue().entrySet()) {
                        for (String number : meaning.getValue()) {
                            if (!number.isEmpty() && link.getSeq().contains(number)) {
                                int index = link.getSeq().indexOf(number);
                                if (index + 1 != link.getSeq().size()) {
                                    if (link.getSeq().get(index + 1).startsWith("[")) { // no comma, append with tag
                                        result.append(String.format(" %s%s%s", CBLUE.getKey(), number, CBLUE.getValue()));
                                        result.append(" ").append(link.getSeq().get(index + 1));
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

                //if ((links.indexOf(link) < links.size() - 1)) {
                    if (link.getJoin() != null) {
                        if (!link.getJoin().equals("[c blue],[/c]") && !link.getJoin().equals(",") && link.getWord() != null) {
                            result.append(" ");
                        }
                        result.append(link.getJoin());
                    }
                //}

                if ((links.indexOf(link) == links.size() - 1)) {
                    switch (link.getType()) {
                        case SEE_ALSO:
                        case SEE:
                            result.append(LINK_SEE_POST);
                            break;
                    }
                }

                prevType = link.getType();
            }
        }
        return result.toString();
    }
}
