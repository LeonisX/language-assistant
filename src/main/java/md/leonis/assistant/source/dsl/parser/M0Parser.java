package md.leonis.assistant.source.dsl.parser;

import javafx.util.Pair;
import md.leonis.assistant.source.dsl.parser.domain.IntermediateDslObject;
import md.leonis.assistant.source.dsl.parser.domain.ParserState;

public class M0Parser {

    private static final Pair<String, String> NUMBER = new Pair<>("[m0]{{Roman}}[b]", "[/b]{{/Roman}}");

    private final IntermediateDslObject dslObject;

    public M0Parser(IntermediateDslObject dslObject) {
        this.dslObject = dslObject;
    }

    // [m0]{{Roman}}[b]Ⅰ[/b]{{/Roman}}
    public void tryToReadGroup(String line) {
        dslObject.setState(ParserState.M1);
        if (!isGroup(line)) {
            return;
        }

        String body = StringUtils.getBody(line, NUMBER);
        line = StringUtils.trimOuterBody(line, NUMBER);
        if (!line.isEmpty()) {
            throw new IllegalStateException("M0: Non-empty line: " + line);
        }

        int number = RomanToNumber.romanToDecimal(body);
        if (number == dslObject.getDslGroups().size()) {
            return;
        }
        if (number == dslObject.getDslGroups().size() + 1) {
            dslObject.addNewGroup();
            return;
        }
        throw new IllegalStateException(line + ": " + number);
    }

    private boolean isGroup(String line) {
        return line.startsWith(NUMBER.getKey());
    }
}
