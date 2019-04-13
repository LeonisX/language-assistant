package md.leonis.assistant.source.dsl.parser;

import javafx.util.Pair;
import md.leonis.assistant.source.dsl.parser.domain.IntermediateDslObject;
import md.leonis.assistant.source.dsl.parser.domain.ParserState;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class M1Parser {

    static final String LINK_START = "[c mediumblue][b]=[/b][/c] <<";
    static final String LINK_END = ">>";
    private static final String LINK_REGEX = Pattern.quote(LINK_START) + ".+" + Pattern.quote(LINK_END);
    static final Pattern LINK_PATTERN = Pattern.compile(LINK_REGEX);

    static final String LINK2_START = "[i]от[/i] <<";
    static final String LINK2_END = ">>";
    private static final String LINK2_REGEX = Pattern.quote(LINK2_START) + ".+" + Pattern.quote(LINK2_END);
    static final Pattern LINK2_PATTERN = Pattern.compile(LINK2_REGEX);

    static final String NOTES_START = "(";
    static final String NOTES_END = ")";
    private static final String NOTES_REGEX = "\\(.+\\)";
    static final Pattern NOTES_PATTERN = Pattern.compile(NOTES_REGEX);

    static final String POS_START = "[p]";
    static final String POS_END = "[/p]";
    private static final String POS_REGEX = Pattern.quote(POS_START) + "((?! ).)+" + Pattern.quote(POS_END);
    static final Pattern POS_PATTERN = Pattern.compile(POS_REGEX);

    private static final Pair<String, String> TRANSCRIPTION = new Pair<>("[c lightslategray]{{t}}", "{{/t}}[/c]");

    private static final Pair<String, String> LINK = new Pair<>("[c lightslategray]{{t}}", ">>");

    private static final Pair<String, String> LINK2 = new Pair<>("[i]от[/i] <<", ">>");

    private static final Pair<String, String> NOTES = new Pair<>("(", ")");

    private static final Pair<String, String> PTAG = new Pair<>("[p]", "[/p]");

    private final IntermediateDslObject dslObject;

    public M1Parser(IntermediateDslObject dslObject) {
        this.dslObject = dslObject;
    }

    // m1 (not in [trn])
    // [m1]bootblack [c lightslategray]{{t}}\[ˊbu:tblæk\]{{/t}}[/c] [p]n[/p] ([p]преим.[/p] [p]амер.[/p])
    // [m1]boogie [c lightslategray]{{t}}\[ˊbu:gɪ\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<boogie-woogie>>
    public void parse(String line) {

        if (!line.startsWith("[m1]")) {
            throw new IllegalStateException(line + " isn't [m1]");
        }


        //TODO remove word from start, trim

        String unchangedLine = line;

        line = line.replace("[m1]", "");

        // [c lightslategray]{{t}}\[ˊbu:tblæk\]{{/t}}[/c]
        Optional<String> body = StringUtils.tryGetBody(line, TRANSCRIPTION);
        if (body.isPresent()) {
            //TODO unescape []
            dslObject.setTranscription(body.get().trim());
            line = StringUtils.trimOuterBody(line, TRANSCRIPTION);
        }

        // ([p]преим.[/p] [p]амер.[/p])
        boolean readNext = true;




        Matcher matcher = NOTES_PATTERN.matcher(line);
        if (matcher.find()) {
            //TODO deep parse
            dslObject.setNotes(matcher.group(0).replace(NOTES_START, "").replace(NOTES_END, "").trim());
            line = matcher.replaceAll("");
        }

        // [c mediumblue][b]=[/b][/c] <<boogie-woogie>>
        matcher = LINK_PATTERN.matcher(line);
        if (matcher.find()) {
            dslObject.setLink(matcher.group(0).replace(LINK_START, "").replace(LINK_END, "").trim());
            line = matcher.replaceAll("");
        }

        // [i]от[/i] <<abacus>>
        matcher = LINK2_PATTERN.matcher(line);
        if (matcher.find()) {
            dslObject.setLink(matcher.group(0).replace(LINK2_START, "").replace(LINK2_END, "").trim());
            line = matcher.replaceAll("");
        }

        // [p]n[/p]
        matcher = POS_PATTERN.matcher(line);
        //TODO few, use while
        while (matcher.find()) {
            dslObject.getTags().add(matcher.group(0).replace(POS_START, "").replace(POS_END, "").trim());
            line = matcher.replaceAll("");
        }

        //TODO investigate
        if (!word.equals(line.trim())) {
            //throw new RuntimeException(line);
            //System.out.println("---------------------------------" + line);
        }
        word = line.trim();

        String result = String.format("[m1]%s", word);
        if (dslObject.getTranscription() != null) {
            result += String.format(" %s%s%s", TRANSCRIPTION.getKey(), dslObject.getTranscription(), TRANSCRIPTION.getValue());
        }
        if (!dslObject.getTags().isEmpty()) {
            result += dslObject.getTags().stream().map(p -> String.format(" %s%s%s", POS_START, p, POS_END)).collect(Collectors.joining(" "));
        }
        if (dslObject.getNotes() != null) {
            result += String.format(" %s%s%s", NOTES_START, dslObject.getNotes(), NOTES_END);
        }
        if (dslObject.getLink() != null) {
            result += String.format(" %s%s%s", LINK_START, dslObject.getLink(), LINK_END);
        }
        if (dslObject.getLink2() != null) {
            result += String.format(" %s%s%s", LINK2_START, dslObject.getLink2(), LINK2_END);
        }

        if (!result.equals(unchangedLine)) {
            System.out.println(unchangedLine);
            System.out.println(result);
            System.out.println();
        }


        dslObject.setState(ParserState.TRN);
    }


}
