package md.leonis.assistant.source.dsl.parser;

import javafx.util.Pair;
import md.leonis.assistant.source.dsl.parser.domain.*;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class M1Parser {

    public static final Pair<String, String> TRANSCRIPTION = new Pair<>("[c lightslategray]{{t}}", "{{/t}}[/c]");

    public static final String LINK_PRE = "[c mediumblue] [b]=[/b] [/c]";
    public static final Triple LINK_U = new Triple("[c blue]", ",", "[/c]");

    public static final String LINK2_PRE = "[i]от[/i]";
    public static final String LINK_SEE_ALSO_PRE = "\\[[p]см. тж.[/p]";
    public static final String LINK_SEE_PRE = "\\[[p]см.[/p]";
    public static final String LINK_SEE_POST = "\\]";

    public static final String ITAG_OR = "[i]или[/i]";

    public static final String NEARLY = "[c darkred]≅[/c]";

    public static final Pair<String, String> LINK = new Pair<>("[c mediumblue] [b]=[/b] [/c] <<", ">>");
    public static final Pair<String, String> LINKR = new Pair<>("<<", ">>");

    public static final Pair<String, String> LINK_GREEN = new Pair<>("[c mediumblue] [b]=[/b] [/c] [c lightseagreen] [lang id=1033]", "[/lang] [/c]");
    public static final Pair<String, String> LINK_GREENR = new Pair<>("[c lightseagreen] [lang id=1033]", "[/lang] [/c]");

    public static final Pair<String, String> LINK_SEE_ALSO = new Pair<>("\\[[p]см. тж.[/p] <<", LINK_SEE_POST);
    public static final Pair<String, String> LINK_SEE = new Pair<>("\\[[p]см.[/p] <<", LINK_SEE_POST);

    public static final Pair<String, String> CBLUE = new Pair<>("[c blue]", "[/c]");
    public static final Pair<String, String> CMEDIUMBLUE = new Pair<>("[c mediumblue]", "[/c]");

    public static final Pair<String, String> LINK2 = new Pair<>("[i]от[/i] <<", ">>");

    public static final Pair<String, String> NOTES = new Pair<>("(", ")");

    public static final Pair<String, String> PTAG = new Pair<>("[p]", "[/p]");

    public static final Pair<String, String> MODIFICATIONS = new Pair<>("[c mediumvioletred] (", ") [/c]");

    public static final Pair<String, String> ITAG = new Pair<>("[i]", "[/i]");

    public static final Pair<String, String> CTEALTAG = new Pair<>("[c teal] [lang id=1033]", "[/lang] [/c]");

    public static final Triple ABBR = new Triple("[p]", "сокр.", "[/p]");
    public static final Triple FROM = new Triple("[i]", "от", "[/i]");

    private final IntermediateDslObject dslObject;
    private final BidiMap<String, String> abbrs;

    public M1Parser(IntermediateDslObject dslObject, BidiMap<String, String> abbrs) {
        this.dslObject = dslObject;
        this.abbrs = abbrs;
    }

    // m1 (not in [trn])
    // [m1]bootblack [c lightslategray]{{t}}\[ˊbu:tblæk\]{{/t}}[/c] [p]n[/p] ([p]преим.[/p] [p]амер.[/p])
    // [m1]boogie [c lightslategray]{{t}}\[ˊbu:gɪ\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<boogie-woogie>>
    public void parse(String line) {
        line = Preprocessor.normalize(line);

        String unchangedLine = line;

        if (!line.startsWith("[m1]")) {
            throw new IllegalStateException(line + " isn't [m1]");
        }

        line = line.replace("[m1]", "");

        // abatis, abattis
        /*if (!line.toUpperCase().startsWith(dslObject.getWord().toUpperCase())) {
            throw new IllegalStateException(line + " w/o " + dslObject.getWord());
        }*/

        int ts = line.indexOf("[");
        if (ts >= 0) {
            dslObject.setNewWord(line.substring(0, ts - 1));
        } else {
            dslObject.setNewWord(line.trim());
        }
        line = line.substring(dslObject.getNewWord().length()).trim();

        // [p]v[/p]
        line = tryReadTags(line, 1);

        // [c lightslategray]{{t}}\[ˊbu:tblæk\]{{/t}}[/c]
        Optional<String> body = DslStringUtils.tryGetBody(line, TRANSCRIPTION);
        if (body.isPresent()) {
            //TODO unescape []
            dslObject.setTranscription(body.get().trim());
            line = DslStringUtils.trimOuterBody(line, TRANSCRIPTION).trim();
        }

        // [p]v[/p]
        line = tryReadTags(line, 2);

        body = DslStringUtils.tryGetBody(line, MODIFICATIONS);
        if (body.isPresent()) {
            dslObject.setModification(Arrays.stream(body.get().trim().split(";")).map(String::trim).collect(Collectors.toList()));
            line = DslStringUtils.trimOuterBody(line, MODIFICATIONS).trim();
        }

        // ([p]преим.[/p] [p]амер.[/p])
        body = DslStringUtils.tryGetBody(line, NOTES);
        if (body.isPresent()) {
            dslObject.setNotes(processNotes(body.get()));
            line = DslStringUtils.trimOuterBody(line, NOTES).trim();
        }

        // [p]v[/p]
        line = tryReadTags(line, 3);

        // [c mediumblue][b]=[/b][/c]
        body = DslStringUtils.tryGetBody(line, LINK);
        if (body.isPresent()) {
            line = DslStringUtils.trimOuterBody(line, LINK);
            line = DslStringUtils.formatOuterBody(body.get(), LINKR) + line;
            line = tryReadLink(line, LinkType.EQ_ONE, dslObject.getLinks());
        }
        // [c mediumblue][b]=[/b][/c]
        body = DslStringUtils.tryGetBody(line, LINK_GREEN);
        if (body.isPresent()) {
            line = DslStringUtils.trimOuterBody(line, LINK_GREEN);
            line = DslStringUtils.formatOuterBody(body.get(), LINKR) + line;
            line = tryReadLink(line, LinkType.EQ_GREEN, dslObject.getLinks());
        }
        // [i]от[/i] <<abacus>>
        body = DslStringUtils.tryGetBody(line, LINK2);
        if (body.isPresent()) {
            line = DslStringUtils.trimOuterBody(line, LINK2);
            line = DslStringUtils.formatOuterBody(body.get(), LINKR) + line;
            line = tryReadLink(line, LinkType.FROM_TWO, dslObject.getLinks());
        }
        // \[[p]см. тж.[/p] <<kerb>> [i]и[/i] <<curb>> [c blue]1,[/c] [c blue]4)[/c]\]
        body = DslStringUtils.tryGetBody(line, LINK_SEE_ALSO);
        if (body.isPresent()) {
            line = LINKR.getKey() + body.get();
            line = tryReadLink(line, LinkType.SEE_ALSO, dslObject.getLinks());
        }
        // \[[p]см.[/p] <<dare>> [c blue]1,[/c] [c blue]1)[/c]\]
        body = DslStringUtils.tryGetBody(line, LINK_SEE);
        if (body.isPresent()) {
            line = LINKR.getKey() + body.get();
            line = tryReadLink(line, LinkType.SEE, dslObject.getLinks());
        }

        if (line.equals(NEARLY)) {
            dslObject.getCurrentTranslation().setNearly(true);
            line = "";
        }

        //TODO investigate
        if (!line.isEmpty()) {
            //throw new RuntimeException(line);
            //System.out.println("---------------------------------" + line);
            dslObject.setTail(line);
        }

        String result = dslObject.toM1String();
        //if (!DslStringUtils.compact(result).equals(DslStringUtils.compact(unchangedLine))) {
        if (!result.equals(unchangedLine)) {
            System.out.println(unchangedLine);
            System.out.println(result);
            System.out.println();
        }

        //TODO switch to notes; finally retest this
        /*if (dslObject.getTail() != null && !dslObject.getTail().equals(":") && !dslObject.getTail().equals("[i]:[/i]")) {
            System.out.println(unchangedLine + " ( " + dslObject.getTail() + " )");
        }*/
        if (dslObject.getNotes() != null) {
            System.out.println(unchangedLine + " ( " + dslObject.getNotes() + " )");
        }

        //TODO probably split newWord A, a -> A; a

        dslObject.setState(ParserState.TRN);
    }

    private String processNotes(String notes) {
        dslObject.addNewDetail();

        boolean readNext = true;
        while (readNext) {
            Optional<String> body = DslStringUtils.tryGetBody(notes, PTAG);
            if (body.isPresent()) {
                dslObject.getCurrentDetail().getTags().add(new Tag(TagType.P, body.get()));
                notes = DslStringUtils.trimOuterBody(notes, PTAG).trim();
                readNext = true;
                continue;
            }
            body = DslStringUtils.tryGetBody(notes, ITAG);
            if (body.isPresent()) {
                dslObject.getCurrentDetail().getTags().add(new Tag(TagType.I, body.get()));
                notes = DslStringUtils.trimOuterBody(notes, ITAG).trim();
                readNext = true;
                continue;
            }
            body = DslStringUtils.tryGetBody(notes, CTEALTAG);
            if (body.isPresent()) {
                dslObject.getCurrentDetail().getLinks().add(new Link(body.get()));
                notes = DslStringUtils.trimOuterBody(notes, CTEALTAG).trim();

                body = DslStringUtils.tryGetBody(notes, TRANSCRIPTION);
                if (body.isPresent()) {
                    //TODO unescape []
                    dslObject.getCurrentDetailLink().setTranscription(body.get().trim());
                    notes = DslStringUtils.trimOuterBody(notes, TRANSCRIPTION).trim();
                }

                readNext = true;
                continue;
            }
            readNext = false;


            /*int plurals = this.dslObject.getPlurals().size();
            notes = tryReadPlurals(notes);
            readNext = plurals < this.dslObject.getPlurals().size();*/
        }

        //fix for tests
        if (notes.isEmpty()) {
            notes = null;
        }

        // Ignore ;
        /*if (notes.startsWith(";")) {
            notes = notes.substring(1).trim();
        }

        readNext = true;
        while (readNext) {
            int plurals = this.dslObject.getAbbrFrom().size();
            notes = tryReadAbbr(notes);
            readNext = plurals < this.dslObject.getAbbrFrom().size();
        }*/

        return notes;
    }

    /*private String tryReadAbbr(String notes) {
        // [p]сокр.[/p][i] от[/i] [p]лат.[/p] [c teal][lang id=1033]ante meridiem[/lang][/c]
        Optional<Pair<Integer, Integer>> pair = DslStringUtils.tryGetBody(notes, ABBR, FROM);
        if (pair.isPresent()) {
            notes = DslStringUtils.trim(notes, pair.get()).trim();
            // Either abbr from or abbr link
            Optional<String> body = DslStringUtils.tryGetBody(notes, PTAG);
            if (body.isPresent()) { // abbr from
                dslObject.addNewAbbrFrom(body.get());
                notes = DslStringUtils.trimOuterBody(notes, PTAG).trim();
                return tryReadAbbrFrom(notes);
            } else {
                notes = tryReadLink(notes, LinkType.ABBR_FROM, dslObject.getAbbrLinks()).trim();
            }
        }
        return notes;
    }

    private String tryReadAbbrFrom(String notes) {
        Optional<String> body = DslStringUtils.tryGetBody(notes, CTEALTAG);
        if (body.isPresent()) {
            dslObject.getCurrentAbbrFrom().setWord(body.get());
            notes = DslStringUtils.trimOuterBody(notes, CTEALTAG).trim();
        }
        return notes;
    }

    private String tryReadPlurals(String notes) {
        // ([p]pl[/p] [c teal][lang id=1033]As, A's[/lang][/c] [c lightslategray]{{t}}\[eɪz\]{{/t}}[/c])
        Pair<String, String> pair = this.dslObject.getPlurals().isEmpty() ? PLURAL_NOTE : PLURAL_NOTER;
        Optional<String> body = DslStringUtils.tryGetBody(notes, pair);
        if (body.isPresent()) {
            dslObject.addNewPlural(body.get());
            notes = DslStringUtils.trimOuterBody(notes, pair).trim();
        }

        body = DslStringUtils.tryGetBody(notes, TRANSCRIPTION);
        if (body.isPresent()) {
            //TODO unescape []
            dslObject.getCurrentPlural().setTranscription(body.get().trim());
            notes = DslStringUtils.trimOuterBody(notes, TRANSCRIPTION).trim();
        }

        if (notes.startsWith(",")) {
            notes = StringUtils.removeStart(notes, ",").trim();
            dslObject.getCurrentPlural().setJoin(",");
        }
        if (notes.startsWith(ITAG_OR)) {
            notes = StringUtils.removeStart(notes, ITAG_OR).trim();
            dslObject.getCurrentPlural().setJoin(" " + ITAG_OR);
        }
        return notes;
    }*/

    private String tryReadTags(String line, int number) {
        // [p]v[/p]
        boolean readNext = true;

        while (readNext) {
            //hack
            Optional<String> body = DslStringUtils.tryGetBody(line, ITAG);
            if (body.isPresent() && (body.get().trim().equals("a") || body.get().trim().equals("n") || body.get().trim().equals("v"))) {
                dslObject.getTags().get(number - 1).add(body.get().trim());
                dslObject.getTagsSeq().get(number - 1).add(body.get());
                line = DslStringUtils.trimOuterBody(line, ITAG).trim();
            }
            //hack
            //TODO may be words????
            body = DslStringUtils.tryGetBody(line, CTEALTAG);
            if (body.isPresent() && (body.get().trim().equals("a") || body.get().trim().equals("n") || body.get().trim().equals("v"))) {
                dslObject.getTags().get(number - 1).add(body.get().trim());
                dslObject.getTagsSeq().get(number - 1).add(body.get());
                line = DslStringUtils.trimOuterBody(line, CTEALTAG).trim();
            }

            body = DslStringUtils.tryGetBody(line, PTAG);
            if (body.isPresent()) {
                dslObject.getTags().get(number - 1).add(body.get().trim());
                dslObject.getTagsSeq().get(number - 1).add(body.get());
                line = DslStringUtils.trimOuterBody(line, PTAG).trim();

                line = tryIgnore(line, number, "[i]и[/i]");
                line = tryIgnore(line, number, "[i],[/i]");
                line = tryIgnore(line, number, ",");
            } else {
                readNext = false;
            }
        }
        return line;
    }

    private String tryIgnore(String line, int number, String tag) {
        if (StringUtils.startsWith(line, tag)) {
            line = StringUtils.removeStart(line, tag).trim();
            dslObject.getTagsSeq().get(number - 1).add(tag);
        }
        return line;
    }

    private String tryReadLink(String line, LinkType linkType, List<Link> links) {
        boolean readNext = true;
        while (readNext) {
            String oldLine = line;
            line = tryReadLink1(line, linkType, links);
            readNext = !oldLine.equals(line);
        }
        return line;
    }

    private String tryReadLink1(String line, LinkType linkType, List<Link> links) {
        Optional<String> body = DslStringUtils.tryGetBody(line, LINKR);
        Link link = null;
        if (body.isPresent()) {
            link = new Link(linkType, body.get().trim());
            links.add(link);
            line = DslStringUtils.trimOuterBody(line, LINKR).trim();
        }

        if (link != null) {
            // [c blue],[/c]

            Optional<Pair<Integer, Integer>> pair = DslStringUtils.tryGetBody(line, LINK_U);
            if (pair.isPresent()) {
                line = DslStringUtils.trim(line, pair.get()).trim();
                link.setJoin(LINK_U.toString());
                //TODO unify
                body = DslStringUtils.tryGetBody(line, LINKR);
                if (body.isPresent()) {
                    link = new Link(linkType, body.get().trim());
                    links.add(link);
                    line = DslStringUtils.trimOuterBody(line, LINKR).trim();
                }
            }

            boolean readNext = true;
            // [c blue]2[/c]{
            while (readNext) {
                body = DslStringUtils.tryGetBody(line, CBLUE);
                if (body.isPresent()) {
                    //identify: split by `,`, trim, remove ""
                    List<String> chunks = Arrays.stream(body.get().split(",")).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
                    if (!chunks.isEmpty()) {
                        if (RomanToNumber.isValidRomanNumeral(chunks.get(0))) {
                            this.dslObject.addLinkGroups(chunks, links);
                        } else if (!chunks.get(0).endsWith(")")) {
                            this.dslObject.addLinkMeanings(chunks, links);
                        } else {
                            this.dslObject.addLinkNumbers(chunks, links);
                        }
                    }

                    line = DslStringUtils.trimOuterBody(line, CBLUE).trim();

                    // ignore [i]и[/i]
                    body = DslStringUtils.tryGetBody(line, ITAG);
                    if (body.isPresent()) {
                        line = DslStringUtils.trimOuterBody(line, ITAG).trim();
                        link.getSeq().add(DslStringUtils.formatOuterBody(body.get(), ITAG));
                        link.setJoin(DslStringUtils.formatOuterBody(body.get(), ITAG));
                    }
                    // ,
                    if (line.startsWith(",")) {
                        line = line.substring(1).trim();
                        link.setJoin(",");
                    }
                } else {
                    readNext = false;
                    // ,
                    if (line.startsWith(",")) {
                        line = line.substring(1).trim();
                        link.setJoin(",");
                    }
                    // ignore [i]и[/i]
                    body = DslStringUtils.tryGetBody(line, ITAG);
                    if (body.isPresent()) {
                        line = DslStringUtils.trimOuterBody(line, ITAG).trim();
                        link.getSeq().add(DslStringUtils.formatOuterBody(body.get(), ITAG));
                        link.setJoin(DslStringUtils.formatOuterBody(body.get(), ITAG));
                    }
                    // ignore [c mediumblue][i] или[/i] [/c]
                    body = DslStringUtils.tryGetBody(line, CMEDIUMBLUE);
                    if (body.isPresent()) {
                        line = DslStringUtils.trimOuterBody(line, CMEDIUMBLUE).trim();
                        link.getSeq().add(DslStringUtils.formatOuterBody(body.get(), CMEDIUMBLUE));
                        link.setJoin(DslStringUtils.formatOuterBody(body.get(), CMEDIUMBLUE));
                    }
                }
            }
        }
        return line;
    }
}
