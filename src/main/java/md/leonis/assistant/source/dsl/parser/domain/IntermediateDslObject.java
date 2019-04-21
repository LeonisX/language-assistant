package md.leonis.assistant.source.dsl.parser.domain;

import javafx.util.Pair;
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

    private String notes = null; //TODO deep parse to chunks
    private String note = null;
    private List<Plural> plurals = new ArrayList<>();
    private List<Abbr> abbrFrom = new ArrayList<>();
    private List<Link> abbrLinks = new ArrayList<>();

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

    public Plural getCurrentPlural() {
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
    }

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

        boolean isNotes = notes != null || note != null || !plurals.isEmpty() || !abbrFrom.isEmpty() || !abbrLinks.isEmpty();

        if (isNotes) {
            result.append(" ").append(NOTES.getKey());
        }

        if (notes != null) {
            result.append(notes);
        }

        if (note != null) {
            result.append(NOTES_MAP.entrySet().stream().filter(e -> e.getValue().equals(note)).findFirst().get().getKey());
        }

        if (!plurals.isEmpty()) {
            for (int i = 0; i < plurals.size(); i++) {
                Plural plural = plurals.get(i);
                String words = String.join(", ", plural.getWords());
                Pair<String, String> pair = (i == 0) ? PLURAL_NOTE: PLURAL_NOTER;
                result.append(String.format("%s%s%s", pair.getKey(), words, pair.getValue()));
                if (plural.getTranscription() != null) {
                    result.append(String.format(" %s%s%s", TRANSCRIPTION.getKey(), plural.getTranscription(), TRANSCRIPTION.getValue()));
                }
                if (!plural.getJoin().isEmpty()) {
                    result.append(plural.getJoin()).append(" ");
                }
            }
        }

        if (!abbrFrom.isEmpty()) {
            if (!plurals.isEmpty()) {
                result.append("; ");
            }
            // ([p]сокр.[/p][i] от[/i] [p]лат.[/p] [c teal][lang id=1033]ante meridiem[/lang][/c])
            result.append(ABBR).append(" ").append(FROM).append(" ");
            for (Abbr abbr : abbrFrom) {
                result.append(String.format(" %s%s%s", PTAG.getKey(), abbr.getLang(), PTAG.getValue()));
                result.append(String.format(" %s%s%s", CTEALTAG.getKey(), abbr.getName(), CTEALTAG.getValue()));
            }
        }

        if (!abbrLinks.isEmpty()) {
            if (!plurals.isEmpty()) {
                result.append("; ");
            }
            renderLinks(result, abbrLinks);
        }

        if (isNotes) {
            result.append(" ").append(NOTES.getValue());
        }

        renderTags(result, 3);

        renderLinks(result, links);

        if (getCurrentTranslation().isNearly()) {
            result.append(String.format(" %s", NEARLY));
        }

        if (tail != null) {
            result.append(tail);
        }
        return result.toString();
    }

    private void renderLinks(StringBuilder result, List<Link> links) {
        if (!links.isEmpty()) {

            LinkType prevType = LinkType.UNDEFINED;

            for (Link link : links) {
                if (prevType != link.getType()) {
                    switch (link.getType()) {
                        case EQ_ONE:
                        case EQ_GREEN:
                            result.append(" " + LINK_PRE);
                            break;
                        case FROM_TWO:
                            result.append(" " + LINK2_PRE);
                            break;
                        case SEE_ALSO:
                            result.append(" " + LINK_SEE_ALSO_PRE);
                            break;
                        case SEE:
                            result.append(" " + LINK_SEE_PRE);
                            break;
                        case ABBR_FROM:
                            result.append(" ").append(ABBR).append(" ").append(FROM);
                            break;
                    }
                }

                switch (link.getType()) {
                    case EQ_ONE:
                    case FROM_TWO:
                    case SEE_ALSO:
                    case SEE:
                    case ABBR_FROM:
                        result.append(String.format(" %s%s%s", LINKR.getKey(), link.getName(), LINKR.getValue()));
                        break;
                    case EQ_GREEN:
                        result.append(String.format(" %s%s%s", LINK_GREENR.getKey(), link.getName(), LINK_GREENR.getValue()));
                        break;
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

                if ((links.indexOf(link) < links.size() - 1)) {
                    if (link.getJoin() != null) {
                        result.append(link.getJoin());
                    }
                }

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

    public String toM1CompactString() {
        return DslStringUtils.compact(toM1String());
    }

}
