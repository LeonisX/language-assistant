package md.leonis.assistant.source.dsl.parser.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class Link {

    private LinkType type;
    private String name;
    private Map<String, Map<String, List<String>>> linkAddress = new LinkedHashMap<>();
    private List<String> seq = new ArrayList<>();
    private String join = null;

    public Link(LinkType type, String name) {
        this.type = type;
        this.name = name;
    }

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
        return "{" + type + ", " + name + ", " + linkAddress + '}';
    }
}
