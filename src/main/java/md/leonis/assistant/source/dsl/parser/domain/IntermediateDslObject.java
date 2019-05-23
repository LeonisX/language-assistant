package md.leonis.assistant.source.dsl.parser.domain;

import lombok.Getter;
import lombok.Setter;
import md.leonis.assistant.source.dsl.domain.parsed.DslGroup;
import md.leonis.assistant.source.dsl.parser.DslStringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static md.leonis.assistant.source.dsl.parser.M1Parser.*;

@Getter
@Setter
public class IntermediateDslObject {

    private String word;
    private String newWord;

    private String transcription = null;

    private List<List<String>> tags = new ArrayList<>();
    private List<List<String>> tagsSeq = new ArrayList<>();

    private List<String> modification = new ArrayList<>();

    private List<DetailContainer> details = new ArrayList<>();

    //TODO delete all
    private String notes = null; //TODO deep parse to chunks
    private String note = null;
    /*private List<Plural> plurals = new ArrayList<>();
    private List<Abbr> abbrFrom = new ArrayList<>();
    private List<Link> abbrLinks = new ArrayList<>();*/

    private List<Link> links = new ArrayList<>();
    private String tail = null;
    private List<DslGroup> dslGroups = new ArrayList<>();

    private List<Translation> translations = new ArrayList<>();

    private ParserState state = ParserState.M0;

    public IntermediateDslObject(String word) {
        this.word = word;
        //addNewGroup();
        addNewTranslation();
        for (int i = 0; i < 3; i++) {
            tags.add(new ArrayList<>());
            tagsSeq.add(new ArrayList<>());
        }
    }

    public DetailContainer getCurrentDetailContainer() {
        return details.get(details.size() - 1);
    }

    public void addNewDetailContainer() {
        details.add(new DetailContainer());
    }

    public Detail getCurrentDetail() {
        return getCurrentDetailContainer().get(getCurrentDetailContainer().size() - 1);
    }

    public void addNewDetail() {
        getCurrentDetailContainer().add(new Detail());
    }

    public Link getCurrentDetailLink() {
        if (getCurrentDetail().getLinks().isEmpty()) {
            getCurrentDetail().getLinks().add(new Link(LinkType.UNDEFINED, null));
        }
        return getCurrentDetail().getLinks().get(getCurrentDetail().getLinks().size() - 1);
    }

    public void addNewDetailLink(String word) {
        //getCurrentDetail().getLinks().add(new Link(word));
    }

    /*public Plural getCurrentPlural() {
        return plurals.get(plurals.size() - 1);
    }

    public void addNewPlural(String word) {
        plurals.add(new Plural(word));
    }

    public Abbr getCurrentAbbrFrom() {
        return abbrFrom.get(abbrFrom.size() - 1);
    }

    public void addNewAbbrFrom(String lang) {
        abbrFrom.add(new Abbr(lang));
    }*/

    public Translation getCurrentTranslation() {
        return translations.get(translations.size() - 1);
    }

    public void addNewTranslation() {
        translations.add(new Translation());
    }

    public DslGroup getCurrentGroup() {
        return dslGroups.get(dslGroups.size() - 1);
    }

    public void addNewGroup() {
        dslGroups.add(new DslGroup());
    }

    public Link getCurrentLink(List<Link> links) {
        return links.get(links.size() - 1);
    }

    public void addLinkGroups(List<String> chunks, List<Link> links) {
        links.get(links.size() - 1).addLinkGroups(chunks);
        getCurrentLink(links).getSeq().addAll(chunks);
    }

    public void addLinkMeanings(List<String> chunks, List<Link> links) {
        links.get(links.size() - 1).addLinkMeanings(chunks);
        getCurrentLink(links).getSeq().addAll(chunks);
    }

    public void addLinkNumbers(List<String> chunks, List<Link> links) {
        links.get(links.size() - 1).addLinkNumbers(chunks);
        getCurrentLink(links).getSeq().addAll(chunks);
    }

    public String toM1String() {
        StringBuilder result = new StringBuilder(String.format("[m1]%s", newWord));

        renderTags(result, 1);

        if (transcription != null) {
            result.append(String.format(" %s%s%s", TRANSCRIPTION.getKey(), transcription, TRANSCRIPTION.getValue()));
        }

        renderTags(result, 2);

        if (!modification.isEmpty()) {
            result.append(" ").append(MODIFICATIONS.getKey()).append(String.join("; ", modification)).append(MODIFICATIONS.getValue());
        }

        if (notes != null) {
            result.append(NOTES.getKey()).append(notes).append(NOTES.getValue());
        }

        String dt = details.stream().map(d -> NOTES.getKey() + d.toString() + NOTES.getValue()).collect(Collectors.joining(" "));
        result.append(dt);

        renderTags(result, 3);

        result.append(" ").append(Link.renderLinks(links));

        if (getCurrentTranslation().isNearly()) {
            result.append(String.format(" %s", NEARLY));
        }

        if (tail != null) {
            result.append(tail);
        }
        return result.toString();
    }

    private void renderTags(StringBuilder result, int n) {
        for (String tag : tags.get(n - 1)) {
            result.append(String.format(" %s%s%s", PTAG.getKey(), tag, PTAG.getValue()));
            int index = tagsSeq.get(n - 1).indexOf(tag);
            if (index + 1 != tagsSeq.get(n - 1).size()) {
                if (tagsSeq.get(n - 1).get(index + 1).startsWith("[") || tagsSeq.get(n - 1).get(index + 1).startsWith(",")) { // [i]и[/i] ,
                    result.append(" ").append(tagsSeq.get(n - 1).get(index + 1));
                }
            }
        }
    }

}
