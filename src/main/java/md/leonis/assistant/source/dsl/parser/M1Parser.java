package md.leonis.assistant.source.dsl.parser;

import javafx.util.Pair;
import md.leonis.assistant.source.dsl.parser.domain.*;

import java.util.*;
import java.util.stream.Collectors;

public class M1Parser {

    public static final Pair<String, String> TRANSCRIPTION = new Pair<>("[c lightslategray]{{t}}", "{{/t}}[/c]");

    public static final String LINK_PRE = "[c mediumblue][b]=[/b][/c]";
    public static final Triple LINK_U = new Triple("[c blue]", ",", "[/c]");

    public static final String LINK2_PRE = "[i]от[/i]";
    public static final String LINK_SEE_ALSO_PRE = "\\[[p]см. тж.[/p]";
    public static final String LINK_SEE_PRE = "\\[[p]см.[/p]";
    public static final String LINK_SEE_POST = "\\]";

    public static final String ITAG_OR = "[i]или[/i]";

    public static final String NEARLY = "[c darkred]≅[/c]";

    public static final Pair<String, String> LINK = new Pair<>("[c mediumblue][b]=[/b][/c] <<", ">>");
    public static final Pair<String, String> LINKR = new Pair<>("<<", ">>");

    public static final Pair<String, String> LINK_GREEN = new Pair<>("[c mediumblue][b]=[/b][/c] [c lightseagreen][lang id=1033]", "[/lang][/c]");
    public static final Pair<String, String> LINK_GREENR = new Pair<>("[c lightseagreen][lang id=1033]", "[/lang][/c]");

    public static final Pair<String, String> LINK_SEE_ALSO = new Pair<>("\\[[p]см. тж.[/p] <<", LINK_SEE_POST);
    public static final Pair<String, String> LINK_SEE = new Pair<>("\\[[p]см.[/p] <<", LINK_SEE_POST);

    public static final Pair<String, String> CBLUE = new Pair<>("[c blue]", "[/c]");
    public static final Pair<String, String> CMEDIUMBLUE = new Pair<>("[c mediumblue]", "[/c]");

    public static final Pair<String, String> LINK2 = new Pair<>("[i]от[/i] <<", ">>");

    public static final Pair<String, String> NOTES = new Pair<>("(", ")");

    public static final Pair<String, String> PTAG = new Pair<>("[p]", "[/p]");

    public static final Pair<String, String> MODIFICATIONS = new Pair<>("[c mediumvioletred](", ")[/c]");

    public static final Pair<String, String> ITAG = new Pair<>("[i]", "[/i]");

    public static final Pair<String, String> CTEALTAG = new Pair<>("[c teal][lang id=1033]", "[/lang][/c]");

    public static final Triple ABBR = new Triple("[p]", "сокр.", "[/p]");
    public static final Triple FROM = new Triple("[i]", "от", "[/i]");

    //TODO need smart parsing
    public static final Map<String, String> NOTES_MAP = new HashMap<String, String>() {{
        put("[p]употр.[/p] [i]как[/i] [p]sing[/p]", "употребляется как единственное число");
        put("[p]употр.[/p] [i]как[/i] [p]sing[/p] [i]и как[/i] [p]pl[/p]", "употребляется как единственное число и как множественное число");

        put("[p]преим.[/p] [p]шотл.[/p]", "преимущественно употребительно в Шотландии");
        put("[p]преим.[/p] [p]амер.[/p]", "преимущественно американизм");

        put("[p]обыкн.[/p] [p]pass.[/p]", "обычно страдательный залог");
        put("[p]обыкн.[/p] [p]pl[/p]", "обычно множественное число");
        put("[p]обыкн.[/p] [p]поэт.[/p]", "обычно поэтическое слово, выражение");
        put("[i]обыкн.[/i] [p]презр.[/p]", "обычно презрительно");
        put("[p]обыкн.[/p] [p]амер.[/p]", "обычно американизм");
        put("[p]обыкн.[/p] [p]собир.[/p]", "обычно собирательно");

        put("[p]особ.[/p] [p]амер.[/p]", "особенно американизм");

        put("[i]часто[/i] [p]презр.[/p]", "часто презрительно");
        put("[i]часто[/i] [p]pl[/p]", "часто множественное число");

        put("[i]иногда[/i] [p]употр.[/p] [i]как[/i] [p]sing[/p]", "иногда употребляется как единственное число");

        put("[p]pl[/p] [p]без измен.[/p][i]", "множественное число без изменений");
        put("[p]pl[/p] [p]без измен.[/p][i];[/i] [p]обыкн.[/p] [p]употр.[/p] [i]как[/i] [p]sing[/p]", "множественное число без изменений; обыкновенно употребляется как единственное число");
    }};

    public static final Pair<String, String> PLURAL_NOTE = new Pair<>("[p]pl[/p] [c teal][lang id=1033]", "[/lang][/c]");
    public static final Pair<String, String> PLURAL_NOTER = new Pair<>("[c teal][lang id=1033]", "[/lang][/c]");


    private final IntermediateDslObject dslObject;

    public M1Parser(IntermediateDslObject dslObject) {
        this.dslObject = dslObject;
    }

    // m1 (not in [trn])
    // [m1]bootblack [c lightslategray]{{t}}\[ˊbu:tblæk\]{{/t}}[/c] [p]n[/p] ([p]преим.[/p] [p]амер.[/p])
    // [m1]boogie [c lightslategray]{{t}}\[ˊbu:gɪ\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<boogie-woogie>>
    public void parse(String line) {

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
        Optional<String> body = StringUtils.tryGetBody(line, TRANSCRIPTION);
        if (body.isPresent()) {
            //TODO unescape []
            dslObject.setTranscription(body.get().trim());
            line = StringUtils.trimOuterBody(line, TRANSCRIPTION).trim();
        }

        // [p]v[/p]
        line = tryReadTags(line, 2);

        body = StringUtils.tryGetBody(line, MODIFICATIONS);
        if (body.isPresent()) {
            dslObject.setModification(Arrays.stream(body.get().trim().split(";")).map(String::trim).collect(Collectors.toList()));
            line = StringUtils.trimOuterBody(line, MODIFICATIONS).trim();
        }

        // ([p]преим.[/p] [p]амер.[/p])
        body = StringUtils.tryGetBody(line, NOTES);
        if (body.isPresent()) {
            if (NOTES_MAP.get(body.get()) != null) {
                dslObject.setNote(NOTES_MAP.get(body.get()));
            } else {
                String processedBody = processNotes(body.get());

                if (!processedBody.isEmpty()) {
                    dslObject.setNotes(body.get().trim());
                }
            }
            line = StringUtils.trimOuterBody(line, NOTES).trim();
        }

        // [p]v[/p]
        line = tryReadTags(line, 3);

        // [c mediumblue][b]=[/b][/c]
        body = StringUtils.tryGetBody(line, LINK);
        if (body.isPresent()) {
            line = StringUtils.trimOuterBody(line, LINK);
            line = StringUtils.formatOuterBody(body.get(), LINKR) + line;
            line = tryReadLink(line, LinkType.EQ_ONE, dslObject.getLinks());
        }
        // [c mediumblue][b]=[/b][/c]
        body = StringUtils.tryGetBody(line, LINK_GREEN);
        if (body.isPresent()) {
            line = StringUtils.trimOuterBody(line, LINK_GREEN);
            line = StringUtils.formatOuterBody(body.get(), LINKR) + line;
            line = tryReadLink(line, LinkType.EQ_GREEN, dslObject.getLinks());
        }
        // [i]от[/i] <<abacus>>
        body = StringUtils.tryGetBody(line, LINK2);
        if (body.isPresent()) {
            line = StringUtils.trimOuterBody(line, LINK2);
            line = StringUtils.formatOuterBody(body.get(), LINKR) + line;
            line = tryReadLink(line, LinkType.FROM_TWO, dslObject.getLinks());
        }
        // \[[p]см. тж.[/p] <<kerb>> [i]и[/i] <<curb>> [c blue]1,[/c] [c blue]4)[/c]\]
        body = StringUtils.tryGetBody(line, LINK_SEE_ALSO);
        if (body.isPresent()) {
            line = LINKR.getKey() + body.get();
            line = tryReadLink(line, LinkType.SEE_ALSO, dslObject.getLinks());
        }
        // \[[p]см.[/p] <<dare>> [c blue]1,[/c] [c blue]1)[/c]\]
        body = StringUtils.tryGetBody(line, LINK_SEE);
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
        if (!StringUtils.compact(result).equals(StringUtils.compact(unchangedLine))) {
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
        boolean readNext = true;
        while (readNext) {
            int plurals = this.dslObject.getPlurals().size();
            notes = tryReadPlurals(notes);
            readNext = plurals < this.dslObject.getPlurals().size();
        }

        readNext = true;
        while (readNext) {
            int plurals = this.dslObject.getAbbrFrom().size();
            notes = tryReadAbbr(notes);
            readNext = plurals < this.dslObject.getAbbrFrom().size();
        }

        return notes;
    }

    private String tryReadAbbr(String notes) {
        // [p]сокр.[/p][i] от[/i] [p]лат.[/p] [c teal][lang id=1033]ante meridiem[/lang][/c]
        Optional<Pair<Integer, Integer>> pair = StringUtils.tryGetBody(notes, ABBR, FROM);
        if (pair.isPresent()) {
            notes = StringUtils.trim(notes, pair.get()).trim();
            // Either abbr from or abbr link
            Optional<String> body = StringUtils.tryGetBody(notes, PTAG);
            if (body.isPresent()) { // abbr from
                dslObject.addNewAbbrFrom(body.get());
                notes = StringUtils.trimOuterBody(notes, PTAG).trim();
                return tryReadAbbrFrom(notes);
            } else {
                notes = tryReadLink(notes, LinkType.ABBR_FROM, dslObject.getAbbrLinks()).trim();
            }
        }
        return notes;
    }

    private String tryReadAbbrFrom(String notes) {
        Optional<String> body = StringUtils.tryGetBody(notes, CTEALTAG);
        if (body.isPresent()) {
            dslObject.getCurrentAbbrFrom().setName(body.get());
            notes = StringUtils.trimOuterBody(notes, CTEALTAG).trim();
        }
        return notes;
    }

    private String tryReadPlurals(String notes) {
        // ([p]pl[/p] [c teal][lang id=1033]As, A's[/lang][/c] [c lightslategray]{{t}}\[eɪz\]{{/t}}[/c])
        Pair<String, String> pair = this.dslObject.getPlurals().isEmpty() ? PLURAL_NOTE : PLURAL_NOTER;
        Optional<String> body = StringUtils.tryGetBody(notes, pair);
        if (body.isPresent()) {
            dslObject.addNewPlural(body.get());
            notes = StringUtils.trimOuterBody(notes, pair).trim();
        }

        body = StringUtils.tryGetBody(notes, TRANSCRIPTION);
        if (body.isPresent()) {
            //TODO unescape []
            dslObject.getCurrentPlural().setTranscription(body.get().trim());
            notes = StringUtils.trimOuterBody(notes, TRANSCRIPTION).trim();
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
    }

    private String tryReadTags(String line, int number) {
        // [p]v[/p]
        boolean readNext = true;

        while (readNext) {
            //hack
            Optional<String> body = StringUtils.tryGetBody(line, ITAG);
            if (body.isPresent() && (body.get().trim().equals("a") || body.get().trim().equals("n") || body.get().trim().equals("v"))) {
                dslObject.getTags().get(number - 1).add(body.get().trim());
                dslObject.getTagsSeq().get(number - 1).add(body.get());
                line = StringUtils.trimOuterBody(line, ITAG).trim();
            }
            //hack
            body = StringUtils.tryGetBody(line, CTEALTAG);
            if (body.isPresent() && (body.get().trim().equals("a") || body.get().trim().equals("n") || body.get().trim().equals("v"))) {
                dslObject.getTags().get(number - 1).add(body.get().trim());
                dslObject.getTagsSeq().get(number - 1).add(body.get());
                line = StringUtils.trimOuterBody(line, CTEALTAG).trim();
            }

            body = StringUtils.tryGetBody(line, PTAG);
            if (body.isPresent()) {
                dslObject.getTags().get(number - 1).add(body.get().trim());
                dslObject.getTagsSeq().get(number - 1).add(body.get());
                line = StringUtils.trimOuterBody(line, PTAG).trim();

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
        Optional<String> body = StringUtils.tryGetBody(line, LINKR);
        Link link = null;
        if (body.isPresent()) {
            link = new Link(linkType, body.get().trim());
            links.add(link);
            line = StringUtils.trimOuterBody(line, LINKR).trim();
        }

        if (link != null) {
            // [c blue],[/c]

            Optional<Pair<Integer, Integer>> pair = StringUtils.tryGetBody(line, LINK_U);
            if (pair.isPresent()) {
                line = StringUtils.trim(line, pair.get()).trim();
                link.setJoin(LINK_U.toString());
                //TODO unify
                body = StringUtils.tryGetBody(line, LINKR);
                if (body.isPresent()) {
                    link = new Link(linkType, body.get().trim());
                    links.add(link);
                    line = StringUtils.trimOuterBody(line, LINKR).trim();
                }
            }

            boolean readNext = true;
            // [c blue]2[/c]{
            while (readNext) {
                body = StringUtils.tryGetBody(line, CBLUE);
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

                    line = StringUtils.trimOuterBody(line, CBLUE).trim();

                    // ignore [i]и[/i]
                    body = StringUtils.tryGetBody(line, ITAG);
                    if (body.isPresent()) {
                        line = StringUtils.trimOuterBody(line, ITAG).trim();
                        link.getSeq().add(StringUtils.formatOuterBody(body.get(), ITAG));
                        link.setJoin(StringUtils.formatOuterBody(body.get(), ITAG));
                    }
                } else {
                    readNext = false;
                    // ignore [i]и[/i]
                    body = StringUtils.tryGetBody(line, ITAG);
                    if (body.isPresent()) {
                        line = StringUtils.trimOuterBody(line, ITAG).trim();
                        link.getSeq().add(StringUtils.formatOuterBody(body.get(), ITAG));
                        link.setJoin(StringUtils.formatOuterBody(body.get(), ITAG));
                    }
                    // ignore [c mediumblue][i] или[/i] [/c]
                    body = StringUtils.tryGetBody(line, CMEDIUMBLUE);
                    if (body.isPresent()) {
                        line = StringUtils.trimOuterBody(line, CMEDIUMBLUE).trim();
                        link.getSeq().add(StringUtils.formatOuterBody(body.get(), CMEDIUMBLUE));
                        link.setJoin(StringUtils.formatOuterBody(body.get(), CMEDIUMBLUE));
                    }
                }
            }
        }
        return line;
    }
}
