package md.leonis.assistant.source.dsl.parser;

import md.leonis.assistant.source.dsl.parser.domain.IntermediateDslObject;
import md.leonis.assistant.source.dsl.parser.domain.ParserState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class M0ParserTest {

    private IntermediateDslObject dslObject = new IntermediateDslObject("word");

    @Test
    void tryToReadGroup() {
        M0Parser m0Parser = new M0Parser(dslObject);
        m0Parser.tryToReadGroup("");
        assertEquals(1, dslObject.getDslGroups().size());
    }

    @Test
    @DisplayName("Ⅰ test successful")
    void tryToReadGroup1() {
        M0Parser m0Parser = new M0Parser(dslObject);
        m0Parser.tryToReadGroup("[m0]{{Roman}}[b]Ⅰ[/b]{{/Roman}}");
        assertEquals(1, dslObject.getDslGroups().size());
        assertEquals(ParserState.M1, dslObject.getState());
    }

    @Test
    @DisplayName("Ⅱ-Ⅶ test successful")
    void tryToReadGroup2() {
        M0Parser m0Parser = new M0Parser(dslObject);
        m0Parser.tryToReadGroup("[m0]{{Roman}}[b]Ⅱ[/b]{{/Roman}}");
        assertEquals(2, dslObject.getDslGroups().size());
        assertEquals(ParserState.M1, dslObject.getState());

        dslObject.addNewGroup();
        m0Parser.tryToReadGroup("[m0]{{Roman}}[b]Ⅲ[/b]{{/Roman}}");
        assertEquals(3, dslObject.getDslGroups().size());

        dslObject.addNewGroup();
        m0Parser.tryToReadGroup("[m0]{{Roman}}[b]Ⅳ[/b]{{/Roman}}");
        assertEquals(4, dslObject.getDslGroups().size());

        dslObject.addNewGroup();
        m0Parser.tryToReadGroup("[m0]{{Roman}}[b]Ⅴ[/b]{{/Roman}}");
        assertEquals(5, dslObject.getDslGroups().size());

        dslObject.addNewGroup();
        m0Parser.tryToReadGroup("[m0]{{Roman}}[b]Ⅵ[/b]{{/Roman}}");
        assertEquals(6, dslObject.getDslGroups().size());

        dslObject.addNewGroup();
        m0Parser.tryToReadGroup("[m0]{{Roman}}[b]Ⅶ[/b]{{/Roman}}");
        assertEquals(7, dslObject.getDslGroups().size());
    }

    @Test
    void tryToReadGroupError() {
        M0Parser m0Parser = new M0Parser(dslObject);
        assertThrows(IllegalStateException.class, () -> m0Parser.tryToReadGroup("[m0]{{Roman}}[b]Ⅶ[/b]{{/Roman}}"));
    }

    @Test
    void tryToReadGroupError2() {
        M0Parser m0Parser = new M0Parser(dslObject);
        assertThrows(IllegalStateException.class, () -> m0Parser.tryToReadGroup("[m0]{{Roman}}[b][/b]{{/Roman}}"));
    }

    @Test
    void tryToReadGroupError3() {
        M0Parser m0Parser = new M0Parser(dslObject);
        assertThrows(IllegalStateException.class, () -> m0Parser.tryToReadGroup("[m0]{{Roman}}[b]Ⅰ[/b]{{/Roman}}-"));
    }
}
