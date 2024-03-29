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
    public static final Tag LINK_U = new Tag(TagType.CBLUE, ",");

    public static final String LINK2_PRE = "[i]от[/i]";
    public static final String LINK_SEE_ALSO_PRE = "\\[[p]см. тж.[/p]";
    public static final String LINK_SEE_PRE = "\\[[p]см.[/p]";
    public static final String LINK_SEE_POST = "\\]";

    public static final String NEARLY = "[c darkred]≅[/c]";

    public static final Pair<String, String> LINK = new Pair<>("[c mediumblue] [b]=[/b] [/c] <<", ">>");
    public static final Pair<String, String> LINKR = new Pair<>("<<", ">>");

    public static final Pair<String, String> LINK_GREEN = new Pair<>("[c mediumblue] [b]=[/b] [/c] [c lightseagreen] [lang id=1033]", "[/lang] [/c]");
    public static final Pair<String, String> LINK_GREENR = new Pair<>("[c lightseagreen] [lang id=1033]", "[/lang] [/c]");

    public static final Pair<String, String> LINK_SEE_ALSO = new Pair<>("\\[[p]см. тж.[/p] <<", LINK_SEE_POST);
    public static final Pair<String, String> LINK_SEE = new Pair<>("\\[[p]см.[/p] <<", LINK_SEE_POST);

    public static final Pair<String, String> CBLUE = new Pair<>("[c blue]", "[/c]");
    public static final Pair<String, String> CMEDIUMBLUE = new Pair<>("[c mediumblue]", "[/c]");
    public static final Pair<String, String> CMEDIUMVIOLET = new Pair<>("[c mediumvioletred]", "[/c]");

    public static final Pair<String, String> LINK2 = new Pair<>("[i]от[/i] <<", ">>");

    public static final Pair<String, String> NOTES = new Pair<>("(", ")");

    //TODO use TagType instead Pair!!!
    public static final Pair<String, String> PTAG = new Pair<>("[p]", "[/p]");

    public static final Pair<String, String> MODIFICATIONS = new Pair<>("[c mediumvioletred] (", ") [/c]");

    public static final Pair<String, String> ITAG = new Pair<>("[i]", "[/i]");

    public static final Pair<String, String> CTEALTAG = new Pair<>("[c teal] [lang id=1033]", "[/lang] [/c]");

    public static final Tag ABBR = new Tag(TagType.P, "сокр.");
    public static final Tag FROM = new Tag(TagType.I, "от");

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
        line = tryReadNotes(line);

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

        String result = Preprocessor.normalize(dslObject.toM1String());
        //if (!DslStringUtils.compact(result).equals(DslStringUtils.compact(unchangedLine))) {
        if (!result.equals(unchangedLine)) {
            System.out.println("src: " + unchangedLine);
            System.out.println("res: " + result);
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

    private String tryReadNotes(String line) {
        boolean readNext = true;
        while (readNext) {
            // ([p]преим.[/p] [p]амер.[/p])
            Optional<String> body = DslStringUtils.tryGetBodyExactly(line, NOTES);
            if (body.isPresent()) {
                dslObject.addNewDetailContainer();
                dslObject.setNotes(processNotes(body.get()));
                line = DslStringUtils.trimOuterBodyExactly(line, NOTES).trim();
                readNext = !(dslObject.getCurrentDetailContainer().isEmpty()) && !line.isEmpty();
            } else {
                readNext = false;
            }
        }
        return line;
    }

    private String processNotes(String notes) {
        dslObject.addNewDetail();

        boolean readNext = true;
        while (readNext) {
            if (notes.startsWith(",")) {
                dslObject.getCurrentDetailLink().setJoin(new Tag(","));
                dslObject.addNewDetail();
                notes = notes.substring(1).trim();
            }
            if (notes.startsWith(";")) {
                dslObject.getCurrentDetailLink().setJoin(new Tag(";"));
                dslObject.addNewDetail();
                notes = notes.substring(1).trim();
            }
            if (notes.startsWith(":")) {
                dslObject.getCurrentDetailLink().setJoin(new Tag(":"));
                dslObject.addNewDetail();
                notes = notes.substring(1).trim();
            }
            if (notes.startsWith("—")) {
                dslObject.getCurrentDetail().addNewTag(new Tag(TagType.NONE, "—"));
                notes = notes.substring(1).trim();
                readNext = true;
                continue;
            }
            Optional<String> body = DslStringUtils.tryGetBody(notes, PTAG);
            if (body.isPresent()) {
                dslObject.getCurrentDetail().addNewTag(new Tag(TagType.P, body.get()));
                notes = DslStringUtils.trimOuterBody(notes, PTAG).trim();
                readNext = true;
                continue;
            }
            body = DslStringUtils.tryGetBody(notes, ITAG);
            if (body.isPresent()) {
                if (body.get().equals("или")) {
                    dslObject.getCurrentDetailLink().setJoin(new Tag(TagType.I, body.get()));
                } else {
                    dslObject.getCurrentDetail().addNewTag(new Tag(TagType.I, body.get()));
                }
                notes = DslStringUtils.trimOuterBody(notes, ITAG).trim();
                readNext = true;
                continue;
            }
            body = DslStringUtils.tryGetBody(notes, CTEALTAG);
            if (body.isPresent()) {
                dslObject.getCurrentDetail().getLinks().add(new Link(LinkType.CTEALTAG, body.get()));
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
            body = DslStringUtils.tryGetBody(notes, CMEDIUMVIOLET);
            if (body.isPresent()) {
                dslObject.getCurrentDetail().getLinks().add(new Link(LinkType.CMEDIUMVIOLET, body.get()));
                notes = DslStringUtils.trimOuterBody(notes, CMEDIUMVIOLET).trim();

                body = DslStringUtils.tryGetBody(notes, TRANSCRIPTION);
                if (body.isPresent()) {
                    //TODO unescape []
                    dslObject.getCurrentDetailLink().setTranscription(body.get().trim());
                    notes = DslStringUtils.trimOuterBody(notes, TRANSCRIPTION).trim();
                }

                readNext = true;
                continue;
            }
            body = DslStringUtils.tryGetBody(notes, LINKR);
            if (body.isPresent()) {

                notes = tryReadLink(notes, LinkType.SIMPLE, dslObject.getCurrentDetail().getLinks());

                readNext = !notes.isEmpty();
                continue;
            }
            readNext = false;

        }

        //fix for tests
        if (notes.isEmpty()) {
            notes = null;
        }

        return notes;
    }

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
            readNext = !oldLine.equals(line) && !line.isEmpty();
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
                link.setJoin(LINK_U);
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
                        link.setJoin(null);
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
                        link.setJoin(new Tag(TagType.I, body.get()));
                    }
                    // ,
                    if (line.startsWith(",")) {
                        line = line.substring(1).trim();
                        link.setJoin(new Tag(","));
                    }
                } else {
                    readNext = false;
                    // ,
                    if (line.startsWith(",")) {
                        line = line.substring(1).trim();
                        link.setJoin(new Tag(","));
                    }
                    // ignore [i]и[/i]
                    body = DslStringUtils.tryGetBody(line, ITAG);
                    if (body.isPresent()) {
                        line = DslStringUtils.trimOuterBody(line, ITAG).trim();
                        link.getSeq().add(DslStringUtils.formatOuterBody(body.get(), ITAG));
                        link.setJoin(new Tag(TagType.I, body.get()));
                    }
                    // ignore [c mediumblue][i] или[/i] [/c]
                    body = DslStringUtils.tryGetBody(line, CMEDIUMBLUE);
                    if (body.isPresent()) {
                        line = DslStringUtils.trimOuterBody(line, CMEDIUMBLUE).trim();
                        link.getSeq().add(DslStringUtils.formatOuterBody(body.get(), CMEDIUMBLUE));
                        link.setJoin(new Tag(TagType.CMEDIUMBLUE, body.get()));
                    }
                }
            }
        }
        return line;
    }
}
