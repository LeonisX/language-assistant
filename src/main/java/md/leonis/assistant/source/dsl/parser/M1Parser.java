package md.leonis.assistant.source.dsl.parser;

import javafx.util.Pair;
import md.leonis.assistant.source.dsl.parser.domain.IntermediateDslObject;
import md.leonis.assistant.source.dsl.parser.domain.Link;
import md.leonis.assistant.source.dsl.parser.domain.LinkType;
import md.leonis.assistant.source.dsl.parser.domain.ParserState;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class M1Parser {

    public static final Pair<String, String> TRANSCRIPTION = new Pair<>("[c lightslategray]{{t}}", "{{/t}}[/c]");

    public static final String LINK_PRE = "[c mediumblue][b]=[/b][/c]";
    public static final String LINK_U = "[c blue],[/c]";
    public static final String LINK2_PRE = "[i]от[/i]";
    public static final String LINK_SEE_ALSO_PRE = "\\[[p]см. тж.[/p]";
    public static final String LINK_SEE_PRE = "\\[[p]см.[/p]";
    public static final String LINK_SEE_POST = "\\]";

    public static final String NEARLY = "[c darkred]≅[/c]";

    public static final Pair<String, String> LINK = new Pair<>("[c mediumblue][b]=[/b][/c] <<", ">>");
    public static final Pair<String, String> LINKR = new Pair<>("<<", ">>");

    public static final Pair<String, String> LINK_GREEN = new Pair<>("[c mediumblue][b]=[/b][/c] [c lightseagreen][lang id=1033]", "[/lang][/c]");
    public static final Pair<String, String> LINK_GREENR = new Pair<>("[c lightseagreen][lang id=1033]", "[/lang][/c]");

    public static final Pair<String, String> LINK_SEE_ALSO = new Pair<>("\\[[p]см. тж.[/p] <<", LINK_SEE_POST);
    public static final Pair<String, String> LINK_SEE = new Pair<>("\\[[p]см.[/p] <<", LINK_SEE_POST);

    public static final Pair<String, String> CBLUE = new Pair<>("[c blue]", "[/c]");

    public static final Pair<String, String> LINK2 = new Pair<>("[i]от[/i] <<", ">>");

    public static final Pair<String, String> NOTES = new Pair<>("(", ")");

    public static final Pair<String, String> PTAG = new Pair<>("[p]", "[/p]");

    public static final Pair<String, String> VARS = new Pair<>("[c mediumvioletred](", ")[/c]");

    public static final Pair<String, String> ITAG = new Pair<>("[i]", "[/i]");

    public static final Pair<String, String> CTEALTAG = new Pair<>("[c teal][lang id=1033]", "[/lang][/c]");


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

        body = StringUtils.tryGetBody(line, VARS);
        if (body.isPresent()) {
            dslObject.setVars(Arrays.stream(body.get().trim().split(";")).map(String::trim).collect(Collectors.toList()));
            line = StringUtils.trimOuterBody(line, VARS).trim();
        }

        // ([p]преим.[/p] [p]амер.[/p])
        //TODO parse deeply
        body = StringUtils.tryGetBody(line, NOTES);
        if (body.isPresent()) {
            dslObject.setNotes(body.get().trim());
            line = StringUtils.trimOuterBody(line, NOTES).trim();
        }

        // [p]v[/p]
        line = tryReadTags(line, 3);

        // [c mediumblue][b]=[/b][/c]
        body = StringUtils.tryGetBody(line, LINK);
        if (body.isPresent()) {
            line = StringUtils.trimOuterBody(line, LINK);
            line = StringUtils.formatOuterBody(body.get(), LINKR) + line;
            line = tryReadLink(line, LinkType.EQ_ONE);
        }
        // [c mediumblue][b]=[/b][/c]
        body = StringUtils.tryGetBody(line, LINK_GREEN);
        if (body.isPresent()) {
            line = StringUtils.trimOuterBody(line, LINK_GREEN);
            line = StringUtils.formatOuterBody(body.get(), LINKR) + line;
            line = tryReadLink(line, LinkType.EQ_GREEN);
        }
        // [i]от[/i] <<abacus>>
        body = StringUtils.tryGetBody(line, LINK2);
        if (body.isPresent()) {
            line = StringUtils.trimOuterBody(line, LINK2);
            line = StringUtils.formatOuterBody(body.get(), LINKR) + line;
            line = tryReadLink(line, LinkType.FROM_TWO);
        }
        // \[[p]см. тж.[/p] <<kerb>> [i]и[/i] <<curb>> [c blue]1,[/c] [c blue]4)[/c]\]
        body = StringUtils.tryGetBody(line, LINK_SEE_ALSO);
        if (body.isPresent()) {
            line = StringUtils.trimOuterBody(line, LINK_SEE_ALSO);
            line = LINKR.getKey() + body.get();
            line = tryReadLink(line, LinkType.SEE_ALSO);
        }
        // \[[p]см.[/p] <<dare>> [c blue]1,[/c] [c blue]1)[/c]\]
        body = StringUtils.tryGetBody(line, LINK_SEE);
        if (body.isPresent()) {
            line = StringUtils.trimOuterBody(line, LINK_SEE);
            line = LINKR.getKey() + body.get();
            line = tryReadLink(line, LinkType.SEE);
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
        if (dslObject.getTail() != null && !dslObject.getTail().equals(":") && !dslObject.getTail().equals("[i]:[/i]")) {
            System.out.println(unchangedLine + " ( " + dslObject.getTail() + " )");
        }

        //TODO probably split newWord A, a -> A; a

        dslObject.setState(ParserState.TRN);
    }

    private String tryReadTags(String line, int n) {
        // [p]v[/p]
        boolean readNext = true;

        while (readNext) {
            //hack
            Optional<String> body = StringUtils.tryGetBody(line, ITAG);
            if (body.isPresent() && (body.get().trim().equals("a") || body.get().trim().equals("n") || body.get().trim().equals("v"))) {
                dslObject.getTags().get(n - 1).add(body.get().trim());
                dslObject.getTagsSeq().get(n - 1).add(body.get());
                line = StringUtils.trimOuterBody(line, ITAG).trim();
            }
            //hack
            body = StringUtils.tryGetBody(line, CTEALTAG);
            if (body.isPresent() && (body.get().trim().equals("a") || body.get().trim().equals("n") || body.get().trim().equals("v"))) {
                dslObject.getTags().get(n - 1).add(body.get().trim());
                dslObject.getTagsSeq().get(n - 1).add(body.get());
                line = StringUtils.trimOuterBody(line, CTEALTAG).trim();
            }

            body = StringUtils.tryGetBody(line, PTAG);
            if (body.isPresent()) {
                dslObject.getTags().get(n - 1).add(body.get().trim());
                dslObject.getTagsSeq().get(n - 1).add(body.get());
                line = StringUtils.trimOuterBody(line, PTAG).trim();

                line = tryIgnore(line, n, "[i]и[/i]");
                line = tryIgnore(line, n, "[i],[/i]");
                line = tryIgnore(line, n, ",");
            } else {
                readNext = false;
            }
        }
        return line;
    }

    private String tryIgnore(String line, int n, String tag) {
        if (StringUtils.startsWith(line, tag)) {
            line = StringUtils.removeStart(line, tag).trim();
            dslObject.getTagsSeq().get(n - 1).add(tag);
        }
        return line;
    }

    private String tryReadLink(String line, LinkType linkType) {
        boolean readNext = true;
        while (readNext) {
            String oldLine = line;
            line = tryReadLink1(line, linkType);
            readNext = !oldLine.equals(line);
        }
        return line;
    }

    private String tryReadLink1(String line, LinkType linkType) {
        Optional<String> body = StringUtils.tryGetBody(line, LINKR);
        Link link = null;
        if (body.isPresent()) {
            link = new Link(linkType, body.get().trim());
            this.dslObject.getLinks().add(link);
            line = StringUtils.trimOuterBody(line, LINKR).trim();
        }

        if (link != null) {
            // [c blue],[/c]
            if (line.startsWith(LINK_U)) {
                line = line.replace(LINK_U, "").trim();
                link.setSep(LINK_U);
                //TODO unify
                body = StringUtils.tryGetBody(line, LINKR);
                if (body.isPresent()) {
                    link = new Link(linkType, body.get().trim());
                    this.dslObject.getLinks().add(link);
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
                    if (RomanToNumber.isValidRomanNumeral(chunks.get(0))) {
                        this.dslObject.addLinkGroups(chunks);
                    } else if (!chunks.get(0).endsWith(")")) {
                        this.dslObject.addLinkMeanings(chunks);
                    } else {
                        this.dslObject.addLinkNumbers(chunks);
                    }

                    line = StringUtils.trimOuterBody(line, CBLUE).trim();

                    // ignore [i]и[/i]
                    body = StringUtils.tryGetBody(line, ITAG);
                    if (body.isPresent()) {
                        line = StringUtils.trimOuterBody(line, ITAG).trim();
                        link.getSeq().add(StringUtils.formatOuterBody(body.get(), ITAG));
                        link.setSep(StringUtils.formatOuterBody(body.get(), ITAG));
                    }
                } else {
                    readNext = false;
                    // ignore [i]и[/i]
                    body = StringUtils.tryGetBody(line, ITAG);
                    if (body.isPresent()) {
                        line = StringUtils.trimOuterBody(line, ITAG).trim();
                        link.getSeq().add(StringUtils.formatOuterBody(body.get(), ITAG));
                        link.setSep(StringUtils.formatOuterBody(body.get(), ITAG));
                    }
                }
            }
        }
        return line;
    }
}
