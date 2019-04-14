package md.leonis.assistant.source.dsl.parser;

import javafx.util.Pair;
import md.leonis.assistant.source.dsl.parser.domain.IntermediateDslObject;
import md.leonis.assistant.source.dsl.parser.domain.ParserState;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class M1Parser {

    public static final Pair<String, String> TRANSCRIPTION = new Pair<>("[c lightslategray]{{t}}", "{{/t}}[/c]");

    public static final String LINK_PRE = "[c mediumblue][b]=[/b][/c]";
    public static final String LINK_U = "[c blue],[/c]";


    public static final Pair<String, String> LINK = new Pair<>("<<", ">>");

    public static final Pair<String, String> CBLUE = new Pair<>("[c blue]", "[/c]");

    public static final Pair<String, String> LINK2 = new Pair<>("[i]от[/i] <<", ">>");

    public static final Pair<String, String> NOTES = new Pair<>("(", ")");

    public static final Pair<String, String> PTAG = new Pair<>("[p]", "[/p]");

    public static final Pair<String, String> VARS = new Pair<>("[c mediumvioletred](", ")[/c]");

    public static final Pair<String, String> ITAG = new Pair<>("[i]", "[/i]");

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
        boolean readNext = true;

        //TODO unify
        while (readNext) {
            Optional<String> body = StringUtils.tryGetBody(line, PTAG);
            if (body.isPresent()) {
                //TODO unescape []
                dslObject.getTags1().add(body.get().trim());
                line = StringUtils.trimOuterBody(line, PTAG).trim();
            } else {
                readNext = false;
            }
        }

        // [c lightslategray]{{t}}\[ˊbu:tblæk\]{{/t}}[/c]
        Optional<String> body = StringUtils.tryGetBody(line, TRANSCRIPTION);
        if (body.isPresent()) {
            //TODO unescape []
            dslObject.setTranscription(body.get().trim());
            line = StringUtils.trimOuterBody(line, TRANSCRIPTION).trim();
        }

        // [p]v[/p]
        readNext = true;

        while (readNext) {
            body = StringUtils.tryGetBody(line, PTAG);
            if (body.isPresent()) {
                //TODO unescape []
                dslObject.getTags2().add(body.get().trim());
                line = StringUtils.trimOuterBody(line, PTAG).trim();
            } else {
                readNext = false;
            }
        }

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

        // [c mediumblue][b]=[/b][/c]
        if (line.startsWith(LINK_PRE)) {
            line = line.replace(LINK_PRE, "").trim();

            // <<boogie-woogie>>
            body = StringUtils.tryGetBody(line, LINK);
            if (body.isPresent()) {
                dslObject.getLink1().add(body.get().trim());
                line = StringUtils.trimOuterBody(line, LINK).trim();
            }

            if (dslObject.getLink1() != null) {

                // [c blue],[/c]
                if (line.startsWith(LINK_U)) {
                    line = line.replace(LINK_U, "").trim();
                    //TODO unify
                    body = StringUtils.tryGetBody(line, LINK);
                    if (body.isPresent()) {
                        dslObject.getLink1().add(body.get().trim());
                        line = StringUtils.trimOuterBody(line, LINK).trim();
                    }
                }

                readNext = true;
                // [c blue]2[/c]{
                while (readNext) {
                    body = StringUtils.tryGetBody(line, CBLUE);
                    if (body.isPresent()) {
                        //identify: split by `,`, trim, remove ""
                        List<String> chunks = Arrays.stream(body.get().split(",")).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
                        if (RomanToNumber.isValidRomanNumeral(chunks.get(0))) {
                            dslObject.addLink1Groups(chunks);
                        } else if (!chunks.get(0).endsWith(")")) {
                            dslObject.addLink1Meanings(chunks);
                        } else {
                            dslObject.addLink1Numbers(chunks);
                        }

                        line = StringUtils.trimOuterBody(line, CBLUE).trim();

                        // ignore [i]и[/i]
                        body = StringUtils.tryGetBody(line, ITAG);
                        if (body.isPresent()) {
                            line = StringUtils.trimOuterBody(line, ITAG).trim();
                            dslObject.getLink1Seq().add(StringUtils.formatOuterBody(body.get(), ITAG));
                        }
                    } else {
                        readNext = false;
                    }
                }


            }
        }

        // [i]от[/i] <<abacus>>
        //TODO check if (dslObject.getLink1() != null) ???
        body = StringUtils.tryGetBody(line, LINK2);
        if (body.isPresent()) {
            dslObject.setLink2(body.get().trim());
            line = StringUtils.trimOuterBody(line, LINK2).trim();
        }


        //TODO investigate
        if (!line.isEmpty()) {
            //throw new RuntimeException(line);
            //System.out.println("---------------------------------" + line);
            dslObject.setTail(line);
        }

        String result = dslObject.toM1String();
        if (!result.equals(unchangedLine)) {
            System.out.println(unchangedLine);
            System.out.println(result);
            System.out.println();
        }

        //TODO find all lines word != newWord
        //TODO probably split newWord A, a -> A; a

        dslObject.setState(ParserState.TRN);
    }


}
