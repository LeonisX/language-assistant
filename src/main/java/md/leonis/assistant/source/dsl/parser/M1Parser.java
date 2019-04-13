package md.leonis.assistant.source.dsl.parser;

import javafx.util.Pair;
import md.leonis.assistant.source.dsl.parser.domain.IntermediateDslObject;
import md.leonis.assistant.source.dsl.parser.domain.ParserState;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class M1Parser {

    private static final Pair<String, String> TRANSCRIPTION = new Pair<>("[c lightslategray]{{t}}", "{{/t}}[/c]");

    private static final String LINK_PRE = "[c mediumblue][b]=[/b][/c]";
    private static final String LINK_U = "[c blue],[/c]";


    private static final Pair<String, String> LINK = new Pair<>("<<", ">>");

    private static final Pair<String, String> LINK_GROUP = new Pair<>("[c blue]", "[/c]");

    private static final Pair<String, String> LINK2 = new Pair<>("[i]от[/i] <<", ">>");

    private static final Pair<String, String> NOTES = new Pair<>("(", ")");

    private static final Pair<String, String> PTAG = new Pair<>("[p]", "[/p]");

    private static final Pair<String, String> VARS = new Pair<>("[c mediumvioletred](", ")[/c]");

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
                dslObject.getTags().add(body.get().trim());
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
                dslObject.getTags().add(body.get().trim());
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

            // [c blue],[/c]
            if (dslObject.getLink1() != null && line.startsWith(LINK_U)) {
                line = line.replace(LINK_U, "").trim();
                //TODO unify
                body = StringUtils.tryGetBody(line, LINK);
                if (body.isPresent()) {
                    dslObject.getLink1().add(body.get().trim());
                    line = StringUtils.trimOuterBody(line, LINK).trim();
                }
            }

            // [c blue]2[/c]
            if (dslObject.getLink1() != null) {
                body = StringUtils.tryGetBody(line, LINK_GROUP);
                if (body.isPresent()) {
                    dslObject.setLink1Group(body.get().trim());
                    line = StringUtils.trimOuterBody(line, LINK_GROUP).trim();
                }
            }
        }

        // [i]от[/i] <<abacus>>
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

        String result = String.format("[m1]%s", dslObject.getNewWord());
        if (dslObject.getTranscription() != null) {
            result += String.format(" %s%s%s", TRANSCRIPTION.getKey(), dslObject.getTranscription(), TRANSCRIPTION.getValue());
        }
        if (!dslObject.getTags().isEmpty()) {
            result += " " + dslObject.getTags().stream().map(p -> String.format("%s%s%s", PTAG.getKey(), p, PTAG.getValue())).collect(Collectors.joining(" "));
        }
        if (!dslObject.getVars().isEmpty()) {
            result += " " + VARS.getKey() + String.join("; ", dslObject.getVars()) + VARS.getValue();
        }
        if (dslObject.getNotes() != null) {
            result += String.format(" %s%s%s", NOTES.getKey(), dslObject.getNotes(), NOTES.getValue());
        }
        if (!dslObject.getLink1().isEmpty()) {
            result += " " + LINK_PRE + String.format(" %s%s%s", LINK.getKey(), dslObject.getLink1().get(0), LINK.getValue());
            if (dslObject.getLink1().size() > 1) {
                result += LINK_U + String.format(" %s%s%s", LINK.getKey(), dslObject.getLink1().get(1), LINK.getValue());
            }
        }
        if (dslObject.getLink1Group() != null) {
            result += String.format(" %s%s%s", LINK_GROUP.getKey(), dslObject.getLink1Group(), LINK_GROUP.getValue());
        }
        if (dslObject.getLink2() != null) {
            result += String.format(" %s%s%s", LINK2.getKey(), dslObject.getLink2(), LINK2.getValue());
        }
        if (dslObject.getTail() != null) {
            result += dslObject.getTail();
        }

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
