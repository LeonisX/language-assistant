package md.leonis.assistant.source.dsl.parser;

import md.leonis.assistant.source.dsl.parser.domain.IntermediateDslObject;
import md.leonis.assistant.source.dsl.parser.domain.ParserState;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class M1ParserTest {

    private IntermediateDslObject dslObject = new IntermediateDslObject("word");

    @Test
    @DisplayName("[m1]word [c lightslategray]{{t}}\\[ˊbu:tblæk\\]{{/t}}[/c] [p]n[/p] ([p]преим.[/p] [p]амер.[/p])")
    void parse() {
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse("[m1]word [c lightslategray]{{t}}\\[ˊbu:tblæk\\]{{/t}}[/c] [p]n[/p] ([p]преим.[/p] [p]амер.[/p])");

        assertEquals("word", dslObject.getWord());
        assertEquals("word", dslObject.getNewWord());
        assertEquals("\\[ˊbu:tblæk\\]", dslObject.getTranscription());
        assertEquals(1, dslObject.getTags().size());
        assertEquals("n", dslObject.getTags().get(0));
        assertTrue(dslObject.getVars().isEmpty());
        assertEquals("[p]преим.[/p] [p]амер.[/p]", dslObject.getNotes());
        assertTrue(dslObject.getLink1().isEmpty());
        assertNull(dslObject.getLink1Group());
        assertNull(dslObject.getLink1Meaning());
        assertNull(dslObject.getLink1Number());
        assertNull(dslObject.getLink2());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
    }

    @Test
    @DisplayName("[m1]word [c lightslategray]{{t}}\\[ˊbu:gɪ\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<boogie-woogie>>")
    void parse2() {
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse("[m1]word [c lightslategray]{{t}}\\[ˊbu:gɪ\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<boogie-woogie>>");

        assertEquals("word", dslObject.getWord());
        assertEquals("word", dslObject.getNewWord());
        assertEquals("\\[ˊbu:gɪ\\]", dslObject.getTranscription());
        assertTrue(dslObject.getTags().isEmpty());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertThat(dslObject.getLink1(), CoreMatchers.hasItems("boogie-woogie"));
        assertNull(dslObject.getLink1Group());
        assertNull(dslObject.getLink1Meaning());
        assertNull(dslObject.getLink1Number());
        assertNull(dslObject.getLink2());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
    }

    @Test
    @DisplayName("[m1]A, a [p]n[/p] [c lightslategray]{{t}}\\[eɪ\\]{{/t}}[/c] ([p]pl[/p] [c teal][lang id=1033]As, A's[/lang][/c] [c lightslategray]{{t}}\\[eɪz\\]{{/t}}[/c])")
    void parse3() {
        dslObject = new IntermediateDslObject("A");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse("[m1]A, a [p]n[/p] [c lightslategray]{{t}}\\[eɪ\\]{{/t}}[/c] ([p]pl[/p] [c teal][lang id=1033]As, A's[/lang][/c] [c lightslategray]{{t}}\\[eɪz\\]{{/t}}[/c])");

        assertEquals("A", dslObject.getWord());
        assertEquals("A, a", dslObject.getNewWord());
        assertEquals("\\[eɪ\\]", dslObject.getTranscription());
        assertEquals(1, dslObject.getTags().size());
        assertEquals("n", dslObject.getTags().get(0));
        assertTrue(dslObject.getVars().isEmpty());
        assertEquals("[p]pl[/p] [c teal][lang id=1033]As, A's[/lang][/c] [c lightslategray]{{t}}\\[eɪz\\]{{/t}}[/c]", dslObject.getNotes());
        assertTrue(dslObject.getLink1().isEmpty());
        assertNull(dslObject.getLink1Group());
        assertNull(dslObject.getLink1Meaning());
        assertNull(dslObject.getLink1Number());
        assertNull(dslObject.getLink2());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
    }

    @Test
    @DisplayName("[m1]absent")
    void parse4() {
        dslObject = new IntermediateDslObject("absent");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse("[m1]absent");

        assertEquals("absent", dslObject.getWord());
        assertEquals("absent", dslObject.getNewWord());
        assertNull(dslObject.getTranscription());
        assertTrue(dslObject.getTags().isEmpty());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLink1().isEmpty());
        assertNull(dslObject.getLink1Group());
        assertNull(dslObject.getLink1Meaning());
        assertNull(dslObject.getLink1Number());
        assertNull(dslObject.getLink2());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
    }

    @Test
    @DisplayName("[m1]aback")
    void parse5() {
        dslObject = new IntermediateDslObject("aback");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse("[m1]aback [c lightslategray]{{t}}\\[əˊbæk\\]{{/t}}[/c]:");

        assertEquals("aback", dslObject.getWord());
        assertEquals("aback", dslObject.getNewWord());
        assertEquals("\\[əˊbæk\\]", dslObject.getTranscription());
        assertTrue(dslObject.getTags().isEmpty());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLink1().isEmpty());
        assertNull(dslObject.getLink1Group());
        assertNull(dslObject.getLink1Meaning());
        assertNull(dslObject.getLink1Number());
        assertNull(dslObject.getLink2());
        assertEquals(":", dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
    }

    @Test
    @DisplayName("[m1]ablaze [c lightslategray]{{t}}\\[əˊbleɪz\\]{{/t}}[/c] [p]a[/p] [p]predic.[/p]")
    void parse6() {
        dslObject = new IntermediateDslObject("ablaze");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse("[m1]ablaze [c lightslategray]{{t}}\\[əˊbleɪz\\]{{/t}}[/c] [p]a[/p] [p]predic.[/p]");

        assertEquals("ablaze", dslObject.getWord());
        assertEquals("ablaze", dslObject.getNewWord());
        assertEquals("\\[əˊbleɪz\\]", dslObject.getTranscription());
        assertEquals(2, dslObject.getTags().size());
        assertEquals("a", dslObject.getTags().get(0));
        assertEquals("predic.", dslObject.getTags().get(1));
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLink1().isEmpty());
        assertNull(dslObject.getLink1Group());
        assertNull(dslObject.getLink1Meaning());
        assertNull(dslObject.getLink1Number());
        assertNull(dslObject.getLink2());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
    }

    @Test
    @DisplayName("[m1]aborigine [c lightslategray]{{t}}\\[ˏæbəˊrɪdʒɪnɪ\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<aboriginal>> [c blue]2[/c]")
    void parse7() {
        dslObject = new IntermediateDslObject("aborigine");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse("[m1]aborigine [c lightslategray]{{t}}\\[ˏæbəˊrɪdʒɪnɪ\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<aboriginal>> [c blue]2[/c]");

        assertEquals("aborigine", dslObject.getWord());
        assertEquals("aborigine", dslObject.getNewWord());
        assertEquals("\\[ˏæbəˊrɪdʒɪnɪ\\]", dslObject.getTranscription());
        assertTrue(dslObject.getTags().isEmpty());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertThat(dslObject.getLink1(), CoreMatchers.hasItems("aboriginal"));
        assertEquals("2", dslObject.getLink1Group());
        assertNull(dslObject.getLink1Meaning());
        assertNull(dslObject.getLink1Number());
        assertNull(dslObject.getLink2());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
    }

    @Test
    @DisplayName("[m1]alleluia [c lightslategray]{{t}}\\[ˏæləˊlu:jə\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<halleluja>>[c blue],[/c] <<hallelujah>>")
    void parse8() {
        dslObject = new IntermediateDslObject("alleluia");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse("[m1]alleluia [c lightslategray]{{t}}\\[ˏæləˊlu:jə\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<halleluja>>[c blue],[/c] <<hallelujah>>");

        assertEquals("alleluia", dslObject.getWord());
        assertEquals("alleluia", dslObject.getNewWord());
        assertEquals("\\[ˏæləˊlu:jə\\]", dslObject.getTranscription());
        assertTrue(dslObject.getTags().isEmpty());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertThat(dslObject.getLink1(), CoreMatchers.hasItems("halleluja", "hallelujah"));
        assertNull(dslObject.getLink1Group());
        assertNull(dslObject.getLink1Meaning());
        assertNull(dslObject.getLink1Number());
        assertNull(dslObject.getLink2());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
    }

    @Test
    @DisplayName("[m1]arise [c lightslategray]{{t}}\\[əˊraɪz\\]{{/t}}[/c] [p]v[/p] [c mediumvioletred](arose; arisen)[/c]")
    void parse9() {
        dslObject = new IntermediateDslObject("arise");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse("[m1]arise [c lightslategray]{{t}}\\[əˊraɪz\\]{{/t}}[/c] [p]v[/p] [c mediumvioletred](arose; arisen)[/c]");

        assertEquals("arise", dslObject.getWord());
        assertEquals("arise", dslObject.getNewWord());
        assertEquals("\\[əˊraɪz\\]", dslObject.getTranscription());
        assertThat(dslObject.getTags(), CoreMatchers.hasItems("v"));
        assertThat(dslObject.getVars(), CoreMatchers.hasItems("arose", "arisen"));
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLink1().isEmpty());
        assertNull(dslObject.getLink1Group());
        assertNull(dslObject.getLink1Meaning());
        assertNull(dslObject.getLink1Number());
        assertNull(dslObject.getLink2());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
    }


}
