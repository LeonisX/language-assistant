package md.leonis.assistant.source.dsl.parser;

import md.leonis.assistant.source.dsl.parser.domain.IntermediateDslObject;
import md.leonis.assistant.source.dsl.parser.domain.Link;
import md.leonis.assistant.source.dsl.parser.domain.ParserState;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualLinkedHashBidiMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.*;

class M1ParserTest {

    private IntermediateDslObject dslObject = new IntermediateDslObject("word");
    private static final BidiMap<String, String> abbrs = new DualLinkedHashBidiMap<>();

    @Test
    @DisplayName("[m1]word [c lightslategray]{{t}}\\[ˊbu:tblæk\\]{{/t}}[/c] [p]n[/p] ([p]преим.[/p] [p]амер.[/p])")
    void parse() {
        String m1 = "[m1]word [c lightslategray]{{t}}\\[ˊbu:tblæk\\]{{/t}}[/c] [p]n[/p] ([p]преим.[/p] [p]амер.[/p])";
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("word", dslObject.getWord());
        assertEquals("word", dslObject.getNewWord());
        assertEquals("\\[ˊbu:tblæk\\]", dslObject.getTranscription());
        assertEquals("[[], [n], []]", dslObject.getTags().toString());
        assertEquals("[[], [n], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]word [c lightslategray]{{t}}\\[ˊbu:gɪ\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<boogie-woogie>>")
    void parse2() {
        String m1 = "[m1]word [c lightslategray]{{t}}\\[ˊbu:gɪ\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<boogie-woogie>>";
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("word", dslObject.getWord());
        assertEquals("word", dslObject.getNewWord());
        assertEquals("\\[ˊbu:gɪ\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[c mediumblue] [b]=[/b] [/c] <<boogie-woogie>>", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    //TODO separate title to 2
    @Test
    @DisplayName("[m1]A, a [p]n[/p] [c lightslategray]{{t}}\\[eɪ\\]{{/t}}[/c] ([p]pl[/p] [c teal][lang id=1033]As, A's[/lang][/c] [c lightslategray]{{t}}\\[eɪz\\]{{/t}}[/c])")
    void parse3() {
        String m1 = "[m1]A, a [p]n[/p] [c lightslategray]{{t}}\\[eɪ\\]{{/t}}[/c] ([p]pl[/p] [c teal][lang id=1033]As, A's[/lang][/c] [c lightslategray]{{t}}\\[eɪz\\]{{/t}}[/c])";
        dslObject = new IntermediateDslObject("A");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("A", dslObject.getWord());
        assertEquals("A, a", dslObject.getNewWord());
        assertEquals("\\[eɪ\\]", dslObject.getTranscription());
        assertEquals("[[n], [], []]", dslObject.getTags().toString());
        assertEquals("[[n], [], []]", dslObject.getTagsSeq().toString());
        assertNull(dslObject.getNotes());
        assertNull(dslObject.getNote());
        assertEquals("[[p]pl[/p] [c teal] [lang id=1033]As, A's[/lang] [/c] [c lightslategray]{{t}}\\[eɪz\\]{{/t}}[/c]]", dslObject.getDetails().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLinks().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]absent")
    void parse4() {
        String m1 = "[m1]absent";
        dslObject = new IntermediateDslObject("absent");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("absent", dslObject.getWord());
        assertEquals("absent", dslObject.getNewWord());
        assertNull(dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLinks().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]aback [c lightslategray]{{t}}\\[əˊbæk\\]{{/t}}[/c]:")
    void parse5() {
        String m1 = "[m1]aback [c lightslategray]{{t}}\\[əˊbæk\\]{{/t}}[/c]:";
        dslObject = new IntermediateDslObject("aback");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("aback", dslObject.getWord());
        assertEquals("aback", dslObject.getNewWord());
        assertEquals("\\[əˊbæk\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLinks().isEmpty());
        assertEquals(":", dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]ablaze [c lightslategray]{{t}}\\[əˊbleɪz\\]{{/t}}[/c] [p]a[/p] [p]predic.[/p]")
    void parse6() {
        String m1 = "[m1]ablaze [c lightslategray]{{t}}\\[əˊbleɪz\\]{{/t}}[/c] [p]a[/p] [p]predic.[/p]";
        dslObject = new IntermediateDslObject("ablaze");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("ablaze", dslObject.getWord());
        assertEquals("ablaze", dslObject.getNewWord());
        assertEquals("\\[əˊbleɪz\\]", dslObject.getTranscription());
        assertEquals("[[], [a, predic.], []]", dslObject.getTags().toString());
        assertEquals("[[], [a, predic.], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLinks().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]aborigine [c lightslategray]{{t}}\\[ˏæbəˊrɪdʒɪnɪ\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<aboriginal>> [c blue]2[/c]")
    void parse7() {
        String m1 = "[m1]aborigine [c lightslategray]{{t}}\\[ˏæbəˊrɪdʒɪnɪ\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<aboriginal>> [c blue]2[/c]";
        dslObject = new IntermediateDslObject("aborigine");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("aborigine", dslObject.getWord());
        assertEquals("aborigine", dslObject.getNewWord());
        assertEquals("\\[ˏæbəˊrɪdʒɪnɪ\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[c mediumblue] [b]=[/b] [/c] <<aboriginal>> [c blue]2[/c]", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]alleluia [c lightslategray]{{t}}\\[ˏæləˊlu:jə\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<halleluja>>[c blue],[/c] <<hallelujah>>")
    void parse8() {
        String m1 = "[m1]alleluia [c lightslategray]{{t}}\\[ˏæləˊlu:jə\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<halleluja>>[c blue],[/c] <<hallelujah>>";
        dslObject = new IntermediateDslObject("alleluia");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("alleluia", dslObject.getWord());
        assertEquals("alleluia", dslObject.getNewWord());
        assertEquals("\\[ˏæləˊlu:jə\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[c mediumblue] [b]=[/b] [/c] <<halleluja>>[c blue],[/c] <<hallelujah>>", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]arise [c lightslategray]{{t}}\\[əˊraɪz\\]{{/t}}[/c] [p]v[/p] [c mediumvioletred](arose; arisen)[/c]")
    void parse9() {
        String m1 = "[m1]arise [c lightslategray]{{t}}\\[əˊraɪz\\]{{/t}}[/c] [p]v[/p] [c mediumvioletred](arose; arisen)[/c]";
        dslObject = new IntermediateDslObject("arise");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("arise", dslObject.getWord());
        assertEquals("arise", dslObject.getNewWord());
        assertEquals("\\[əˊraɪz\\]", dslObject.getTranscription());
        assertEquals("[[], [v], []]", dslObject.getTags().toString());
        assertEquals("[[], [v], []]", dslObject.getTagsSeq().toString());
        assertThat(dslObject.getModification(), contains("arose", "arisen"));
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLinks().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]about-face [c lightslategray]{{t}}\\[əˏbaυtˊfeɪs\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<about-turn>> [c blue]1, 2[/c]")
    void parse10() {
        String m1 = "[m1]about-face [c lightslategray]{{t}}\\[əˏbaυtˊfeɪs\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<about-turn>> [c blue]1, 2[/c]";
        dslObject = new IntermediateDslObject("about-face");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("about-face", dslObject.getWord());
        assertEquals("about-face", dslObject.getNewWord());
        assertEquals("\\[əˏbaυtˊfeɪs\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[c mediumblue] [b]=[/b] [/c] <<about-turn>> [c blue]1, 2[/c]", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]accusal [c lightslategray]{{t}}\\[əˊkju:zǝl\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<accusation>> [c blue]1)[/c]")
    void parse11() {
        String m1 = "[m1]accusal [c lightslategray]{{t}}\\[əˊkju:zǝl\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<accusation>> [c blue]1)[/c]";
        dslObject = new IntermediateDslObject("accusal");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("accusal", dslObject.getWord());
        assertEquals("accusal", dslObject.getNewWord());
        assertEquals("\\[əˊkju:zǝl\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[c mediumblue] [b]=[/b] [/c] <<accusation>> [c blue]1)[/c]", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]alleyway [c lightslategray]{{t}}\\[ˊælɪweɪ\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<alley>> [c blue]Ⅰ,[/c] [c blue]1)[/c] [i]и[/i] [c blue]2)[/c]")
    void parse12() {
        String m1 = "[m1]alleyway [c lightslategray]{{t}}\\[ˊælɪweɪ\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<alley>> [c blue]Ⅰ,[/c] [c blue]1)[/c] [i]и[/i] [c blue]2)[/c]";
        dslObject = new IntermediateDslObject("alleyway");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("alleyway", dslObject.getWord());
        assertEquals("alleyway", dslObject.getNewWord());
        assertEquals("\\[ˊælɪweɪ\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[c mediumblue] [b]=[/b] [/c] <<alley>> [c blue]Ⅰ,[/c] [c blue]1)[/c] [i]и[/i] [c blue]2)[/c]", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]anyway [c lightslategray]{{t}}\\[ˊenɪweɪ\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<anyhow>> [c blue]1),[/c] [c blue]2)[/c]")
    void parse13() {
        String m1 = "[m1]anyway [c lightslategray]{{t}}\\[ˊenɪweɪ\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<anyhow>> [c blue]1),[/c] [c blue]2)[/c]";
        dslObject = new IntermediateDslObject("anyway");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("anyway", dslObject.getWord());
        assertEquals("anyway", dslObject.getNewWord());
        assertEquals("\\[ˊenɪweɪ\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[c mediumblue] [b]=[/b] [/c] <<anyhow>> [c blue]1),[/c] [c blue]2)[/c]", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]balsamic [c lightslategray]{{t}}\\[bɔ:lˊsæmɪk\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<balmy>> [c blue]1)[/c] [i]и[/i] [c blue]4)[/c]")
    void parse14() {
        String m1 = "[m1]balsamic [c lightslategray]{{t}}\\[bɔ:lˊsæmɪk\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<balmy>> [c blue]1)[/c] [i]и[/i] [c blue]4)[/c]";
        dslObject = new IntermediateDslObject("balsamic");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("balsamic", dslObject.getWord());
        assertEquals("balsamic", dslObject.getNewWord());
        assertEquals("\\[bɔ:lˊsæmɪk\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[c mediumblue] [b]=[/b] [/c] <<balmy>> [c blue]1)[/c] [i]и[/i] [c blue]4)[/c]", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]bow-knot [c lightslategray]{{t}}\\[ˊbəυnɒt\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<bow>> [c blue]Ⅱ,[/c] [c blue]1,[/c] [c blue]1)[/c]")
    void parse15() {
        String m1 = "[m1]bow-knot [c lightslategray]{{t}}\\[ˊbəυnɒt\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<bow>> [c blue]Ⅱ,[/c] [c blue]1,[/c] [c blue]1)[/c]";
        dslObject = new IntermediateDslObject("bow-knot");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("bow-knot", dslObject.getWord());
        assertEquals("bow-knot", dslObject.getNewWord());
        assertEquals("\\[ˊbəυnɒt\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[c mediumblue] [b]=[/b] [/c] <<bow>> [c blue]Ⅱ,[/c] [c blue]1,[/c] [c blue]1)[/c]", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]beheld [c lightslategray]{{t}}\\[bɪˊheld\\]{{/t}}[/c] [p]past[/p] [i]и[/i] [p]p. p.[/p] [i]от[/i] <<behold>> [c blue]1[/c]")
    void parse16() {
        String m1 = "[m1]beheld [c lightslategray]{{t}}\\[bɪˊheld\\]{{/t}}[/c] [p]past[/p] [i]и[/i] [p]p. p.[/p] [i]от[/i] <<behold>> [c blue]1[/c]";
        dslObject = new IntermediateDslObject("beheld");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("beheld", dslObject.getWord());
        assertEquals("beheld", dslObject.getNewWord());
        assertEquals("\\[bɪˊheld\\]", dslObject.getTranscription());
        assertEquals("[[], [past, p. p.], []]", dslObject.getTags().toString());
        assertEquals("[[], [past, [i]и[/i], p. p.], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[i]от[/i] <<behold>> [c blue]1[/c]", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]archeo- [c lightslategray]{{t}}\\[ˊɑ:kɪə(υ)-\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] [c lightseagreen][lang id=1033]archaeo-[/lang][/c]")
    void parse17() {
        String m1 = "[m1]archeo- [c lightslategray]{{t}}\\[ˊɑ:kɪə(υ)-\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] [c lightseagreen][lang id=1033]archaeo-[/lang][/c]";
        dslObject = new IntermediateDslObject("archeo-");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("archeo-", dslObject.getWord());
        assertEquals("archeo-", dslObject.getNewWord());
        assertEquals("\\[ˊɑ:kɪə(υ)-\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[c mediumblue] [b]=[/b] [/c] [c lightseagreen] [lang id=1033]archaeo-[/lang] [/c]", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]axes [c lightslategray]{{t}}\\[ˊæksɪz\\]{{/t}}[/c] [p]pl[/p] [i]от[/i] <<ax>>[c blue] 1,[/c] <<axe>>[c blue] 1[/c]")
    void parse18() {
        String m1 = "[m1]axes [c lightslategray]{{t}}\\[ˊæksɪz\\]{{/t}}[/c] [p]pl[/p] [i]от[/i] <<ax>>[c blue] 1,[/c] <<axe>>[c blue] 1[/c]";
        dslObject = new IntermediateDslObject("axes");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("axes", dslObject.getWord());
        assertEquals("axes", dslObject.getNewWord());
        assertEquals("\\[ˊæksɪz\\]", dslObject.getTranscription());
        assertEquals("[[], [pl], []]", dslObject.getTags().toString());
        assertEquals("[[], [pl], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[i]от[/i] <<ax>> [c blue]1[/c], <<axe>> [c blue]1[/c]", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]blew [c lightslategray]{{t}}\\[blu:\\]{{/t}}[/c] [p]past[/p] [i]от[/i] <<blow>> [c blue]Ⅱ,[/c] [c blue]2[/c] [i]и[/i] <<blow>> [c blue]Ⅲ,[/c] [c blue]2[/c]")
    void parse19() {
        String m1 = "[m1]blew [c lightslategray]{{t}}\\[blu:\\]{{/t}}[/c] [p]past[/p] [i]от[/i] <<blow>> [c blue]Ⅱ,[/c] [c blue]2[/c] [i]и[/i] <<blow>> [c blue]Ⅲ,[/c] [c blue]2[/c]";
        dslObject = new IntermediateDslObject("blew");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("blew", dslObject.getWord());
        assertEquals("blew", dslObject.getNewWord());
        assertEquals("\\[blu:\\]", dslObject.getTranscription());
        assertEquals("[[], [past], []]", dslObject.getTags().toString());
        assertEquals("[[], [past], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[i]от[/i] <<blow>> [c blue]Ⅱ,[/c] [c blue]2[/c] [i]и[/i] <<blow>> [c blue]Ⅲ,[/c] [c blue]2[/c]", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]briar [c lightslategray]{{t}}\\[ˊbra(ɪ)ə\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<brier>> [c blue]Ⅰ[/c] [i]и[/i] <<brier>> [c blue]Ⅱ[/c]")
    void parse20() {
        String m1 = "[m1]briar [c lightslategray]{{t}}\\[ˊbra(ɪ)ə\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<brier>> [c blue]Ⅰ[/c] [i]и[/i] <<brier>> [c blue]Ⅱ[/c]";
        dslObject = new IntermediateDslObject("briar");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("briar", dslObject.getWord());
        assertEquals("briar", dslObject.getNewWord());
        assertEquals("\\[ˊbra(ɪ)ə\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[c mediumblue] [b]=[/b] [/c] <<brier>> [c blue]Ⅰ[/c] [i]и[/i] <<brier>> [c blue]Ⅱ[/c]", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]calves [c lightslategray]{{t}}\\[kɑ:vz\\]{{/t}}[/c] [p]pl[/p] [i]от[/i] <<calf>> [c blue]Ⅰ[/c] [i]и[/i] <<calf>> [c blue]Ⅱ[/c]")
    void parse21() {
        String m1 = "[m1]calves [c lightslategray]{{t}}\\[kɑ:vz\\]{{/t}}[/c] [p]pl[/p] [i]от[/i] <<calf>> [c blue]Ⅰ[/c] [i]и[/i] <<calf>> [c blue]Ⅱ[/c]";
        dslObject = new IntermediateDslObject("calves");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("calves", dslObject.getWord());
        assertEquals("calves", dslObject.getNewWord());
        assertEquals("\\[kɑ:vz\\]", dslObject.getTranscription());
        assertEquals("[[], [pl], []]", dslObject.getTags().toString());
        assertEquals("[[], [pl], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[i]от[/i] <<calf>> [c blue]Ⅰ[/c] [i]и[/i] <<calf>> [c blue]Ⅱ[/c]", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]curst [c lightslategray]{{t}}\\[kɜ:st\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<cursed>> [c blue]2[/c] [i]и[/i] [c blue]3[/c]")
    void parse22() {
        String m1 = "[m1]curst [c lightslategray]{{t}}\\[kɜ:st\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<cursed>> [c blue]2[/c] [i]и[/i] [c blue]3[/c]";
        dslObject = new IntermediateDslObject("curst");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("curst", dslObject.getWord());
        assertEquals("curst", dslObject.getNewWord());
        assertEquals("\\[kɜ:st\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[c mediumblue] [b]=[/b] [/c] <<cursed>> [c blue]2, 3[/c]", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        //assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]upstair [c lightslategray]{{t}}\\[ˏʌpˊsteə\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<upstairs>> [c blue]1,[/c] [c blue]1)[/c] [i]и[/i] [c blue]3[/c]")
    void parse23() {
        String m1 = "[m1]upstair [c lightslategray]{{t}}\\[ˏʌpˊsteə\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<upstairs>> [c blue]1,[/c] [c blue]1)[/c] [i]и[/i] [c blue]3[/c]";
        dslObject = new IntermediateDslObject("upstair");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("upstair", dslObject.getWord());
        assertEquals("upstair", dslObject.getNewWord());
        assertEquals("\\[ˏʌpˊsteə\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        //TODO upgrade links renderer
        assertEquals("[c mediumblue] [b]=[/b] [/c] <<upstairs>> [c blue]1,[/c] [c blue]1)[/c] [i]и[/i] [c blue]3[/c]", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]aboard [c lightslategray]{{t}}\\[əˊbɔ:d\\]{{/t}}[/c] [p]adv[/p][i],[/i] [p]prep[/p]")
    void parse24() {
        String m1 = "[m1]aboard [c lightslategray]{{t}}\\[əˊbɔ:d\\]{{/t}}[/c] [p]adv[/p][i],[/i] [p]prep[/p]";
        dslObject = new IntermediateDslObject("aboard");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("aboard", dslObject.getWord());
        assertEquals("aboard", dslObject.getNewWord());
        assertEquals("\\[əˊbɔ:d\\]", dslObject.getTranscription());
        assertEquals("[[], [adv, prep], []]", dslObject.getTags().toString());
        assertEquals("[[], [adv, [i],[/i], prep], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLinks().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]à la carte [c lightslategray]{{t}}\\[ˏæləˊkɑ:t\\]{{/t}}[/c] [p]a[/p], [p]adv[/p]")
    void parse25() {
        String m1 = "[m1]à la carte [c lightslategray]{{t}}\\[ˏæləˊkɑ:t\\]{{/t}}[/c] [p]a[/p], [p]adv[/p]";
        dslObject = new IntermediateDslObject("à la carte");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("à la carte", dslObject.getWord());
        assertEquals("à la carte", dslObject.getNewWord());
        assertEquals("\\[ˏæləˊkɑ:t\\]", dslObject.getTranscription());
        assertEquals("[[], [a, adv], []]", dslObject.getTags().toString());
        assertEquals("[[], [a, ,, adv], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLinks().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]betake [c lightslategray]{{t}}\\[bɪˊteɪk\\]{{/t}}[/c] [p]v[/p] [c mediumvioletred](betook; betaken)[/c] [p]refl.[/p]")
    void parse26() {
        String m1 = "[m1]betake [c lightslategray]{{t}}\\[bɪˊteɪk\\]{{/t}}[/c] [p]v[/p] [c mediumvioletred](betook; betaken)[/c] [p]refl.[/p]";
        dslObject = new IntermediateDslObject("betake");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("betake", dslObject.getWord());
        assertEquals("betake", dslObject.getNewWord());
        assertEquals("\\[bɪˊteɪk\\]", dslObject.getTranscription());
        assertEquals("[[], [v], [refl.]]", dslObject.getTags().toString());
        assertEquals("[[], [v], [refl.]]", dslObject.getTagsSeq().toString());
        assertThat(dslObject.getModification(), contains("betook", "betaken"));
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLinks().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]caries [c lightslategray]{{t}}\\[ˊkeərɪz\\]{{/t}}[/c] [i]n[/i]")
    void parse27() {
        String m1 = "[m1]caries [c lightslategray]{{t}}\\[ˊkeərɪz\\]{{/t}}[/c] [i]n[/i]";
        dslObject = new IntermediateDslObject("caries");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("caries", dslObject.getWord());
        assertEquals("caries", dslObject.getNewWord());
        assertEquals("\\[ˊkeərɪz\\]", dslObject.getTranscription());
        assertEquals("[[], [n], []]", dslObject.getTags().toString());
        assertEquals("[[], [n], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLinks().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        //assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]continuing education [c lightslategray]{{t}}\\[kənˏtɪnju:ɪŋedjυˊkeɪʃən\\]{{/t}}[/c][i] n[/i]")
    void parse28() {
        String m1 = "[m1]continuing education [c lightslategray]{{t}}\\[kənˏtɪnju:ɪŋedjυˊkeɪʃən\\]{{/t}}[/c][i] n[/i]";
        dslObject = new IntermediateDslObject("continuing education");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("continuing education", dslObject.getWord());
        assertEquals("continuing education", dslObject.getNewWord());
        assertEquals("\\[kənˏtɪnju:ɪŋedjυˊkeɪʃən\\]", dslObject.getTranscription());
        assertEquals("[[], [n], []]", dslObject.getTags().toString());
        assertEquals("[[], [n], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLinks().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        //assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]goggle-eyed [c lightslategray]{{t}}\\[ˏgɒglˊaɪd\\]{{/t}}[/c] [c teal][lang id=1033]a[/lang][/c]")
    void parse29() {
        String m1 = "[m1]goggle-eyed [c lightslategray]{{t}}\\[ˏgɒglˊaɪd\\]{{/t}}[/c] [c teal][lang id=1033]a[/lang][/c]";
        dslObject = new IntermediateDslObject("goggle-eyed");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("goggle-eyed", dslObject.getWord());
        assertEquals("goggle-eyed", dslObject.getNewWord());
        assertEquals("\\[ˏgɒglˊaɪd\\]", dslObject.getTranscription());
        assertEquals("[[], [a], []]", dslObject.getTags().toString());
        assertEquals("[[], [a], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLinks().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(1, dslObject.getTranslations().size());
        assertFalse(dslObject.getTranslations().get(0).isNearly());
        assertEquals(0, dslObject.getDslGroups().size());
        assertEquals(ParserState.TRN, dslObject.getState());
        //assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]stone-cold [c lightslategray]{{t}}\\[ˏstəυnˊkəυld\\]{{/t}}[/c] [p]a[/p] [c darkred]≅[/c]")
    void parse30() {
        String m1 = "[m1]stone-cold [c lightslategray]{{t}}\\[ˏstəυnˊkəυld\\]{{/t}}[/c] [p]a[/p] [c darkred]≅[/c]";
        dslObject = new IntermediateDslObject("stone-cold");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("stone-cold", dslObject.getWord());
        assertEquals("stone-cold", dslObject.getNewWord());
        assertEquals("\\[ˏstəυnˊkəυld\\]", dslObject.getTranscription());
        assertEquals("[[], [a], []]", dslObject.getTags().toString());
        assertEquals("[[], [a], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLinks().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(1, dslObject.getTranslations().size());
        assertTrue(dslObject.getTranslations().get(0).isNearly());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]curbstone [c lightslategray]{{t}}\\[ˊkɜ:bstəυn\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<kerbstone>> \\[[p]см. тж.[/p] <<kerb>> [i]и[/i] <<curb>> [c blue]1,[/c] [c blue]4)[/c]\\]")
    void parse31() {
        String m1 = "[m1]curbstone [c lightslategray]{{t}}\\[ˊkɜ:bstəυn\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<kerbstone>> \\[[p]см. тж.[/p] <<kerb>> [i]и[/i] <<curb>> [c blue]1,[/c] [c blue]4)[/c]\\]";
        dslObject = new IntermediateDslObject("curbstone");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("curbstone", dslObject.getWord());
        assertEquals("curbstone", dslObject.getNewWord());
        assertEquals("\\[ˊkɜ:bstəυn\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[c mediumblue] [b]=[/b] [/c] <<kerbstone>>\\[[p]см. тж.[/p] <<kerb>> [i]и[/i] <<curb>> [c blue]1,[/c] [c blue]4)[/c]\\]", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(1, dslObject.getTranslations().size());
        assertFalse(dslObject.getTranslations().get(0).isNearly());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]daresay [c lightslategray]{{t}}\\[ˏdeəˊseɪ\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] [c lightseagreen][lang id=1033]dare say[/lang][/c] \\[[p]см.[/p] <<dare>> [c blue]1,[/c] [c blue]1)[/c]\\]")
    void parse32() {
        String m1 = "[m1]daresay [c lightslategray]{{t}}\\[ˏdeəˊseɪ\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] [c lightseagreen][lang id=1033]dare say[/lang][/c] \\[[p]см.[/p] <<dare>> [c blue]1,[/c] [c blue]1)[/c]\\]";
        dslObject = new IntermediateDslObject("daresay");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("daresay", dslObject.getWord());
        assertEquals("daresay", dslObject.getNewWord());
        assertEquals("\\[ˏdeəˊseɪ\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertNull(dslObject.getNote());
        assertTrue(dslObject.getDetails().isEmpty());
        assertEquals("[c mediumblue] [b]=[/b] [/c] [c lightseagreen] [lang id=1033]dare say[/lang] [/c] \\[[p]см.[/p] <<dare>> [c blue]1,[/c] [c blue]1)[/c]\\]", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(1, dslObject.getTranslations().size());
        assertFalse(dslObject.getTranslations().get(0).isNearly());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]abash [c lightslategray]{{t}}\\[əˊbæʃ\\]{{/t}}[/c] [p]v[/p] ([p]обыкн.[/p] [p]pass.[/p])")
    void parse33() {
        String m1 = "[m1]abash [c lightslategray]{{t}}\\[əˊbæʃ\\]{{/t}}[/c] [p]v[/p] ([p]обыкн.[/p] [p]pass.[/p])";
        dslObject = new IntermediateDslObject("abash");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("abash", dslObject.getWord());
        assertEquals("abash", dslObject.getNewWord());
        assertEquals("\\[əˊbæʃ\\]", dslObject.getTranscription());
        assertEquals("[[], [v], []]", dslObject.getTags().toString());
        assertEquals("[[], [v], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertNull(dslObject.getNote());
        assertEquals("[[p]обыкн.[/p] [p]pass.[/p]]", dslObject.getDetails().toString());
        assertEquals("", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(1, dslObject.getTranslations().size());
        assertFalse(dslObject.getTranslations().get(0).isNearly());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]acoustics [c lightslategray]{{t}}\\[əˊku:stɪks\\]{{/t}}[/c] [p]n[/p] [p]pl[/p] ([p]употр.[/p] [i]как[/i] [p]sing[/p])")
    void parse34() {
        String m1 = "[m1]acoustics [c lightslategray]{{t}}\\[əˊku:stɪks\\]{{/t}}[/c] [p]n[/p] [p]pl[/p] ([p]употр.[/p] [i]как[/i] [p]sing[/p])";
        dslObject = new IntermediateDslObject("acoustics");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("[[], [n, pl], []]", dslObject.getTags().toString());
        assertEquals("[[], [n, pl], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertNull(dslObject.getNote());
        assertEquals("[[p]употр.[/p] [i]как[/i] [p]sing[/p]]", dslObject.getDetails().toString());
        assertEquals("", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(1, dslObject.getTranslations().size());
        assertFalse(dslObject.getTranslations().get(0).isNearly());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]agenda [c lightslategray]{{t}}\\[əˊdʒendə\\]{{/t}}[/c] [p]n[/p] [p]pl[/p] ([i]иногда[/i] [p]употр.[/p] [i]как[/i] [p]sing[/p])")
    void parse35() {
        String m1 = "[m1]agenda [c lightslategray]{{t}}\\[əˊdʒendə\\]{{/t}}[/c] [p]n[/p] [p]pl[/p] ([i]иногда[/i] [p]употр.[/p] [i]как[/i] [p]sing[/p])";
        dslObject = new IntermediateDslObject("agenda");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("[[], [n, pl], []]", dslObject.getTags().toString());
        assertEquals("[[], [n, pl], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertNull(dslObject.getNote());
        assertEquals("[[i]иногда[/i] [p]употр.[/p] [i]как[/i] [p]sing[/p]]", dslObject.getDetails().toString());
        assertEquals("", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(1, dslObject.getTranslations().size());
        assertFalse(dslObject.getTranslations().get(0).isNearly());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]Asiatic [c lightslategray]{{t}}\\[ˏeɪʃɪˊætɪk\\]{{/t}}[/c] ([i]часто[/i] [p]презр.[/p]) [c mediumblue][b]=[/b][/c] <<Asian>>")
    void parse36() {
        String m1 = "[m1]Asiatic [c lightslategray]{{t}}\\[ˏeɪʃɪˊætɪk\\]{{/t}}[/c] ([i]часто[/i] [p]презр.[/p]) [c mediumblue][b]=[/b][/c] <<Asian>>";
        dslObject = new IntermediateDslObject("Asiatic");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertNull(dslObject.getNote());
        assertEquals("[[i]часто[/i] [p]презр.[/p]]", dslObject.getDetails().toString());
        assertEquals("[c mediumblue] [b]=[/b] [/c] <<Asian>>", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(1, dslObject.getTranslations().size());
        assertFalse(dslObject.getTranslations().get(0).isNearly());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]abacus [c lightslategray]{{t}}\\[ˊæbəkəs\\]{{/t}}[/c] [p]n[/p] ([p]pl[/p] [c teal][lang id=1033]-es[/lang][/c] [c lightslategray]{{t}}\\[-ɪz\\]{{/t}}[/c], [c teal][lang id=1033]-ci[/lang][/c])")
    void parse37() {
        String m1 = "[m1]abacus [c lightslategray]{{t}}\\[ˊæbəkəs\\]{{/t}}[/c] [p]n[/p] ([p]pl[/p] [c teal][lang id=1033]-es[/lang][/c] [c lightslategray]{{t}}\\[-ɪz\\]{{/t}}[/c], [c teal][lang id=1033]-ci[/lang][/c])";
        dslObject = new IntermediateDslObject("abacus");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("[[], [n], []]", dslObject.getTags().toString());
        assertEquals("[[], [n], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertNull(dslObject.getNote());
        assertEquals("[[p]pl[/p] [c teal] [lang id=1033]-es[/lang] [/c] [c lightslategray]{{t}}\\[-ɪz\\]{{/t}}[/c],,  [c teal] [lang id=1033]-ci[/lang] [/c]]", dslObject.getDetails().toString());
        assertEquals("", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(1, dslObject.getTranslations().size());
        assertFalse(dslObject.getTranslations().get(0).isNearly());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    //TODO separate title to 2
    @Test
    @DisplayName("[m1]abatis, abattis [c lightslategray]{{t}}\\[ˊæbətɪs\\]{{/t}}[/c] [p]n[/p] ([p]pl[/p] [c teal][lang id=1033]abatis[/lang][/c] [c lightslategray]{{t}}\\[ˊæbəti:z\\]{{/t}}[/c], [c teal][lang id=1033]abatises, abattises[/lang][/c])")
    void parse38() {
        String m1 = "[m1]abatis, abattis [c lightslategray]{{t}}\\[ˊæbətɪs\\]{{/t}}[/c] [p]n[/p] ([p]pl[/p] [c teal][lang id=1033]abatis[/lang][/c] [c lightslategray]{{t}}\\[ˊæbəti:z\\]{{/t}}[/c], [c teal][lang id=1033]abatises, abattises[/lang][/c])";
        dslObject = new IntermediateDslObject("abatis, abattis");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("[[], [n], []]", dslObject.getTags().toString());
        assertEquals("[[], [n], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertNull(dslObject.getNote());
        assertEquals("[[p]pl[/p] [c teal] [lang id=1033]abatis[/lang] [/c] [c lightslategray]{{t}}\\[ˊæbəti:z\\]{{/t}}[/c],,  [c teal] [lang id=1033]abatises, abattises[/lang] [/c]]", dslObject.getDetails().toString());
        assertEquals("", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(1, dslObject.getTranslations().size());
        assertFalse(dslObject.getTranslations().get(0).isNearly());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]affection [c lightslategray]{{t}}\\[əˊfekʃn\\]{{/t}}[/c] [p]n[/p] ([i]часто[/i] [p]pl[/p])")
    void parse40() {
        String m1 = "[m1]affection [c lightslategray]{{t}}\\[əˊfekʃn\\]{{/t}}[/c] [p]n[/p] ([i]часто[/i] [p]pl[/p])";
        dslObject = new IntermediateDslObject("affection");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("[[], [n], []]", dslObject.getTags().toString());
        assertEquals("[[], [n], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertNull(dslObject.getNote());
        assertEquals("[[i]часто[/i] [p]pl[/p]]", dslObject.getDetails().toString());
        assertEquals("", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(1, dslObject.getTranslations().size());
        assertFalse(dslObject.getTranslations().get(0).isNearly());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]aileron [c lightslategray]{{t}}\\[ˊeɪlərɒn\\]{{/t}}[/c] [p]n[/p] ([p]обыкн.[/p] [p]pl[/p])")
    void parse41() {
        String m1 = "[m1]aileron [c lightslategray]{{t}}\\[ˊeɪlərɒn\\]{{/t}}[/c] [p]n[/p] ([p]обыкн.[/p] [p]pl[/p])";
        dslObject = new IntermediateDslObject("aileron");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("[[], [n], []]", dslObject.getTags().toString());
        assertEquals("[[], [n], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertNull(dslObject.getNote());
        assertEquals("[[p]обыкн.[/p] [p]pl[/p]]", dslObject.getDetails().toString());
        assertEquals("", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(1, dslObject.getTranslations().size());
        assertFalse(dslObject.getTranslations().get(0).isNearly());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]alms [c lightslategray]{{t}}\\[ɑ:mz\\]{{/t}}[/c] [p]n[/p] ([p]pl[/p] [p]без измен.[/p][i];[/i] [p]обыкн.[/p] [p]употр.[/p] [i]как[/i] [p]sing[/p])")
    void parse42() {
        String m1 = "[m1]alms [c lightslategray]{{t}}\\[ɑ:mz\\]{{/t}}[/c] [p]n[/p] ([p]pl[/p] [p]без измен.[/p][i];[/i] [p]обыкн.[/p] [p]употр.[/p] [i]как[/i] [p]sing[/p])";
        dslObject = new IntermediateDslObject("alms");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("[[], [n], []]", dslObject.getTags().toString());
        assertEquals("[[], [n], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertNull(dslObject.getNote());
        //TODO wrong, need process [i];[/i]
        assertEquals("[[p]pl[/p] [p]без измен.[/p] [i];[/i] [p]обыкн.[/p] [p]употр.[/p] [i]как[/i] [p]sing[/p]]", dslObject.getDetails().toString());
        assertEquals("", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(1, dslObject.getTranslations().size());
        assertFalse(dslObject.getTranslations().get(0).isNearly());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]brattle [c lightslategray]{{t}}\\[ˊbrætl\\]{{/t}}[/c] ([p]преим.[/p] [p]шотл.[/p])")
    void parse43() {
        String m1 = "[m1]brattle [c lightslategray]{{t}}\\[ˊbrætl\\]{{/t}}[/c] ([p]преим.[/p] [p]шотл.[/p])";
        dslObject = new IntermediateDslObject("brattle");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertNull(dslObject.getNote());
        assertEquals("[[p]преим.[/p] [p]шотл.[/p]]", dslObject.getDetails().toString());
        assertEquals("", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(1, dslObject.getTranslations().size());
        assertFalse(dslObject.getTranslations().get(0).isNearly());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]crux [c lightslategray]{{t}}\\[krʌks\\]{{/t}}[/c] [p]n[/p] ([p]pl[/p] [c teal][lang id=1033]cruxes[/lang][/c] [i]или[/i] [c teal][lang id=1033]cruces[/lang][/c])")
    void parse44() {
        String m1 = "[m1]crux [c lightslategray]{{t}}\\[krʌks\\]{{/t}}[/c] [p]n[/p] ([p]pl[/p] [c teal][lang id=1033]cruxes[/lang][/c] [i]или[/i] [c teal][lang id=1033]cruces[/lang][/c])";
        dslObject = new IntermediateDslObject("crux");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("[[], [n], []]", dslObject.getTags().toString());
        assertEquals("[[], [n], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertNull(dslObject.getNote());
        assertEquals("[[p]pl[/p] [c teal] [lang id=1033]cruxes[/lang] [/c] [i]или[/i] [c teal] [lang id=1033]cruces[/lang] [/c]]", dslObject.getDetails().toString());
        assertEquals("", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(1, dslObject.getTranslations().size());
        assertFalse(dslObject.getTranslations().get(0).isNearly());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]a. m. [c lightslategray]{{t}}\\[ˏeɪˊem\\]{{/t}}[/c] ([p]сокр.[/p][i] от[/i] [p]лат.[/p] [c teal][lang id=1033]ante meridiem[/lang][/c])")
    void parse45() {
        String m1 = "[m1]a. m. [c lightslategray]{{t}}\\[ˏeɪˊem\\]{{/t}}[/c] ([p]сокр.[/p][i] от[/i] [p]лат.[/p] [c teal][lang id=1033]ante meridiem[/lang][/c])";
        dslObject = new IntermediateDslObject("a. m.");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertNull(dslObject.getNote());
        assertEquals("[[p]сокр.[/p] [i]от[/i] [p]лат.[/p] [c teal] [lang id=1033]ante meridiem[/lang] [/c]]", dslObject.getDetails().toString());
        assertEquals("", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(1, dslObject.getTranslations().size());
        assertFalse(dslObject.getTranslations().get(0).isNearly());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]appro [c lightslategray]{{t}}\\[ˊæprəυ\\]{{/t}}[/c] [p]n[/p] ([p]сокр.[/p] [i]от[/i] <<approbation>>[c blue], [/c]<<approval>>):")
    void parse46() {
        String m1 = "[m1]appro [c lightslategray]{{t}}\\[ˊæprəυ\\]{{/t}}[/c] [p]n[/p] ([p]сокр.[/p] [i]от[/i] <<approbation>>[c blue], [/c]<<approval>>):";
        dslObject = new IntermediateDslObject("appro");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("[[], [n], []]", dslObject.getTags().toString());
        assertEquals("[[], [n], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertNull(dslObject.getNote());
        assertEquals("[[p]сокр.[/p] [i]от[/i] <<approbation>>[c blue],[/c] <<approval>>]", dslObject.getDetails().toString());
        assertEquals("", Link.renderLinks(dslObject.getLinks()));
        assertEquals(":", dslObject.getTail());
        assertEquals(1, dslObject.getTranslations().size());
        assertFalse(dslObject.getTranslations().get(0).isNearly());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]vert [c lightslategray]{{t}}\\[vɜ:t\\]{{/t}}[/c] ([p]сокр.[/p] [i]от[/i] <<convert>>[c mediumblue][i] или[/i] [/c]<<pervert>>)")
    void parse47() {
        String m1 = "[m1]vert [c lightslategray]{{t}}\\[vɜ:t\\]{{/t}}[/c] ([p]сокр.[/p] [i]от[/i] <<convert>>[c mediumblue][i] или[/i] [/c]<<pervert>>)";
        dslObject = new IntermediateDslObject("vert");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertNull(dslObject.getNote());
        assertEquals("[[p]сокр.[/p] [i]от[/i] <<convert>> [c mediumblue] [i]или[/i] [/c] <<pervert>>]", dslObject.getDetails().toString());
        assertEquals("", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(1, dslObject.getTranslations().size());
        assertFalse(dslObject.getTranslations().get(0).isNearly());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]cello [c lightslategray]{{t}}\\[ˊtʃeləυ\\]{{/t}}[/c] [p]n[/p] ([p]pl[/p] [c teal][lang id=1033]-os[/lang][/c] [c lightslategray]{{t}}\\[-əυz\\]{{/t}}[/c]; [p]сокр.[/p] [i]от[/i] <<violoncello>>)")
    void parse48() {
        String m1 = "[m1]cello [c lightslategray]{{t}}\\[ˊtʃeləυ\\]{{/t}}[/c] [p]n[/p] ([p]pl[/p] [c teal][lang id=1033]-os[/lang][/c] [c lightslategray]{{t}}\\[-əυz\\]{{/t}}[/c]; [p]сокр.[/p] [i]от[/i] <<violoncello>>)";
        dslObject = new IntermediateDslObject("cello");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("[[], [n], []]", dslObject.getTags().toString());
        assertEquals("[[], [n], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertNull(dslObject.getNote());
        assertEquals("[[p]pl[/p] [c teal] [lang id=1033]-os[/lang] [/c] [c lightslategray]{{t}}\\[-əυz\\]{{/t}}[/c] ;, [p]сокр.[/p] [i]от[/i] <<violoncello>>]", dslObject.getDetails().toString());
        assertEquals("", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(1, dslObject.getTranslations().size());
        assertFalse(dslObject.getTranslations().get(0).isNearly());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]hew [c lightslategray]{{t}}\\[hju:\\]{{/t}}[/c] [p]v[/p] ([c mediumvioletred]hewed[/c] [c lightslategray]{{t}}\\[-d\\]{{/t}}[/c]; [c mediumvioletred]hewed, hewn[/c])")
    void parse49() {
        String m1 = "[m1]hew [c lightslategray]{{t}}\\[hju:\\]{{/t}}[/c] [p]v[/p] ([c mediumvioletred]hewed[/c] [c lightslategray]{{t}}\\[-d\\]{{/t}}[/c]; [c mediumvioletred]hewed, hewn[/c])";
        dslObject = new IntermediateDslObject("hew");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("[[], [v], []]", dslObject.getTags().toString());
        assertEquals("[[], [v], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertNull(dslObject.getNote());
        assertEquals("[ [c mediumvioletred]hewed[/c] [c lightslategray]{{t}}\\[-d\\]{{/t}}[/c] ;,  [c mediumvioletred]hewed, hewn[/c]]", dslObject.getDetails().toString());
        assertEquals("", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(1, dslObject.getTranslations().size());
        assertFalse(dslObject.getTranslations().get(0).isNearly());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    @Test
    @DisplayName("[m1]jinnee [c lightslategray]{{t}}\\[dʒɪˊni:\\]{{/t}}[/c] [p]n[/p] ([p]pl[/p] [c teal][lang id=1033]jinn[/lang][/c], [i]часто[/i] [p]употр.[/p] [i]как[/i] [p]sing[/p])")
    void parse50() {
        String m1 = "[m1]jinnee [c lightslategray]{{t}}\\[dʒɪˊni:\\]{{/t}}[/c] [p]n[/p] ([p]pl[/p] [c teal][lang id=1033]jinn[/lang][/c], [i]часто[/i] [p]употр.[/p] [i]как[/i] [p]sing[/p])";
        dslObject = new IntermediateDslObject("jinnee");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("[[], [n], []]", dslObject.getTags().toString());
        assertEquals("[[], [n], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertNull(dslObject.getNote());
        assertEquals("[[p]pl[/p] [c teal] [lang id=1033]jinn[/lang] [/c],, [i]часто[/i] [p]употр.[/p] [i]как[/i] [p]sing[/p]]", dslObject.getDetails().toString());
        assertEquals("", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(1, dslObject.getTranslations().size());
        assertFalse(dslObject.getTranslations().get(0).isNearly());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }

    //TODO find bug and repair
    @Test
    @DisplayName("[m1]hers [c lightslategray]{{t}}\\[hɜ:z\\]{{/t}}[/c] [p]pron[/p] [p]poss.[/p] ([i]абсолютная форма[/i]; [i]не[/i] [p]употр.[/p] [i]атрибутивно[/i]; [p]ср.[/p] <<her>> [c blue]Ⅱ[/c])")
    void parse51() {
        String m1 = "[m1]hers [c lightslategray]{{t}}\\[hɜ:z\\]{{/t}}[/c] [p]pron[/p] [p]poss.[/p] ([i]абсолютная форма[/i]; [i]не[/i] [p]употр.[/p] [i]атрибутивно[/i]; [p]ср.[/p] <<her>> [c blue]Ⅱ[/c])";
        dslObject = new IntermediateDslObject("hers");
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);
        m1Parser.parse(m1);

        assertEquals("[[], [pron, poss.], []]", dslObject.getTags().toString());
        assertEquals("[[], [pron, poss.], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getModification().isEmpty());
        assertNull(dslObject.getNotes());
        assertNull(dslObject.getNote());
        assertEquals("[i]абсолютная форма[/i]; [i]не[/i] [p]употр.[/p] [i]атрибутивно[/i]; [p]ср.[/p] <<her>> [c blue]Ⅱ[/c]", dslObject.getDetails().toString());
        assertEquals("", Link.renderLinks(dslObject.getLinks()));
        assertNull(dslObject.getTail());
        assertEquals(1, dslObject.getTranslations().size());
        assertFalse(dslObject.getTranslations().get(0).isNearly());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(Preprocessor.normalize(m1), Preprocessor.normalize(dslObject.toM1String()));
    }


    //TODO simplify code, may be remove compact()
    
    //TODO modifications - object + trancr

    //TODO smart notes parser
    // v 0. Начать новый бренч.
    // v 1, Read all abbr from DB
    // v 2. Create object Details: String[] abbr, String[] words, Links[] links
    // v 2. parse tag-by-tag.
    // v 3. если встречается ссылка или ключевое слово - заполняем.
    // v 3. идея такая - вытаскивать слова и как-то их сохранять. Так же вытаскивать ссылки.
    // v 4. если ";" - повторяем. Добавить других разделителей
    // 8. починить все тесты, написать несколько новых, потом вручную просмотреть около сотни нотесов
    // 9. Вмерджить если всё ОК

    //TODO switch to notes; finally retest tails again and cover all cases

    // for tail:
    // new fields
    // sameAs list -> in translation object

    //TODO separate title to 2: [m1]abatis, abattis [....

    @BeforeAll
    static void setAbbrs() {
        abbrs.put("a", "adjective — имя прилагательное");
        abbrs.put("adv", "adverb — наречие");
        abbrs.put("attr.", "attributive — атрибутивное употребление (в качестве определения)");
        abbrs.put("cj", "conjunction — союз");
        abbrs.put("conj.", "(pronoun) conjunctive — союзное (местоимение)");
        abbrs.put("demonstr.", "demonstrative (pronoun) — указательное (местоимение)");
        abbrs.put("emph.", "emphatic (pronoun) — усилительное (местоимение)");
        abbrs.put("etc.", "et cetera — и так далее");
        abbrs.put("imp.", "imperative — повелительное (наклонение)");
        abbrs.put("impers.", "impersonal — безличный");
        abbrs.put("indef.", "indefinite (pronoun) — неопределенное (местоимение)");
        abbrs.put("inf.", "infinitive — неопределенная форма глагола");
        abbrs.put("int", "interjection — междометие");
        abbrs.put("inter.", "interrogative (pronoun) — вопросительное (местоимение)");
        abbrs.put("n", "noun — имя существительное");
        abbrs.put("neg.", "negative — отрицательное");
        abbrs.put("num. card.", "numeral cardinal — количественное числительное");
        abbrs.put("num. ord.", "numeral ordinal — порядковое числительное");
        abbrs.put("p. p.", "past participle — причастие прошедшего времени");
        abbrs.put("pass.", "passive — страдательный (залог)");
        abbrs.put("past", "past (tense) — прошедшее (время)");
        abbrs.put("perf.", "perfect — перфект");
        abbrs.put("pers.", "personal (pronoun) — личное (местоимение)");
        abbrs.put("pl", "plural — множественное число");
        abbrs.put("poss.", "possessive (pronoun) — притяжательное (местоимение)");
        abbrs.put("predic.", "predicative — предикативное употребление");
        abbrs.put("pref", "prefix — приставка");
        abbrs.put("prep", "preposition — предлог");
        abbrs.put("pres", "present (tense) — настоящее (время)");
        abbrs.put("pres. p.", "present participle — причастие настоящего времени");
        abbrs.put("pres. perf.", "present perfect — настоящее совершенное время");
        abbrs.put("pron", "pronoun — местоимение");
        abbrs.put("recipr.", "reciprocal (pronoun) — взаимное (местоимение)");
        abbrs.put("refl.", "reflexive — употребляется с возвратным местоимением");
        abbrs.put("rel.", "relative (pronoun) — относительное (местоимение)");
        abbrs.put("sing", "singular — единственное число");
        abbrs.put("sl.", "slang — сленг, жаргон");
        abbrs.put("v", "verb — глагол");
        abbrs.put("ав.", "авиация");
        abbrs.put("австрал.", "употребительно в Австралии");
        abbrs.put("авто", "автомобильное дело");
        abbrs.put("ак.", "акустика");
        abbrs.put("амер.", "американизм");
        abbrs.put("анат.", "анатомия");
        abbrs.put("англ.", "английский");
        abbrs.put("антроп.", "антропология");
        abbrs.put("араб.", "арабский (язык)");
        abbrs.put("арт.", "артиллерия");
        abbrs.put("археол.", "археология");
        abbrs.put("архит.", "архитектура");
        abbrs.put("астр.", "астрономия");
        abbrs.put("бакт.", "бактериология");
        abbrs.put("без измен.", "без изменений");
        abbrs.put("библ.", "библеизм");
        abbrs.put("биол.", "биология");
        abbrs.put("биохим.", "биохимия");
        abbrs.put("бирж.", "биржевой термин");
        abbrs.put("бот.", "ботаника");
        abbrs.put("букв.", "буквально");
        abbrs.put("бухг.", "бухгалтерия");
        abbrs.put("в разн. знач.", "в разных значениях");
        abbrs.put("в.", "век, века");
        abbrs.put("вв.", "века (мн.ч), веков");
        abbrs.put("вет.", "ветеринария");
        abbrs.put("вм.", "вместо");
        abbrs.put("воен.", "военное дело");
        abbrs.put("возвыш.", "возвышенно");
        abbrs.put("вопр.", "вопросительный");
        abbrs.put("вульг.", "вульгарное слово, выражение");
        abbrs.put("вчт.", "вычислительная техника");
        abbrs.put("г.", "1) год 2) город");
        abbrs.put("гг.", "года (мн.ч), годов");
        abbrs.put("геогр.", "география");
        abbrs.put("геод.", "геодезия");
        abbrs.put("геол.", "геология");
        abbrs.put("геом.", "геометрия");
        abbrs.put("геральд.", "геральдика");
        abbrs.put("гидр.", "гидротехника");
        abbrs.put("гл.", "глагол");
        abbrs.put("гл. обр.", "главным образом");
        abbrs.put("горн.", "горное дело");
        abbrs.put("грам.", "грамматика");
        abbrs.put("греч.", "греческий (язык)");
        abbrs.put("груб.", "грубое слово, выражение");
        abbrs.put("д.", "дюйм");
        abbrs.put("детск.", "детская речь");
        abbrs.put("диал.", "диалектизм");
        abbrs.put("дип.", "дипломатия");
        abbrs.put("дор.", "дорожное дело");
        abbrs.put("др.", "другие");
        abbrs.put("др.-греч.", "древнегреческий (-ая история)");
        abbrs.put("др.-евр.", "древнееврейский (язык)");
        abbrs.put("др.-рим.", "древнеримский (-ая история)");
        abbrs.put("египт.", "египетский");
        abbrs.put("ед. ч.", "единственное число");
        abbrs.put("ж.", "женский род");
        abbrs.put("ж.-д.", "железнодорожный транспорт");
        abbrs.put("жарг.", "жаргон, жаргонизм");
        abbrs.put("жив.", "живопись");
        abbrs.put("зоол.", "зоология");
        abbrs.put("и пр.", "и прочее");
        abbrs.put("и т.д.", "и так далее");
        abbrs.put("и т.п.", "и тому подобное");
        abbrs.put("инд.", "индийские языки; употребительно в Индии");
        abbrs.put("информ.", "информатика");
        abbrs.put("ирл.", "ирландский (язык)");
        abbrs.put("ирон.", "в ироническом смысле, иронический");
        abbrs.put("иск.", "искусство");
        abbrs.put("исп.", "испанский (язык)");
        abbrs.put("ист.", "история");
        abbrs.put("ит.", "итальянский (язык)");
        abbrs.put("канад.", "употребительно в Канаде");
        abbrs.put("канц.", "канцелярское слово, выражение");
        abbrs.put("карт.", "термин карточной игры");
        abbrs.put("кино", "кинематография");
        abbrs.put("книжн.", "книжный стиль");
        abbrs.put("ком.", "коммерческий термин");
        abbrs.put("косв.", "косвенный (падеж)");
        abbrs.put("косм.", "космонавтика");
        abbrs.put("кул.", "кулинария");
        abbrs.put("л.", "лицо");
        abbrs.put("ласк.", "ласкательная форма");
        abbrs.put("лат.", "латинский (язык)");
        abbrs.put("лес.", "лесное дело");
        abbrs.put("лингв.", "лингвистика");
        abbrs.put("лит.", "литература, литературоведение");
        abbrs.put("лог.", "логика");
        abbrs.put("м.", "мужской род");
        abbrs.put("мат.", "математика");
        abbrs.put("мед.", "медицина");
        abbrs.put("метал.", "металлургия");
        abbrs.put("метео", "метеорология");
        abbrs.put("мех.", "механика");
        abbrs.put("мин.", "минералогия");
        abbrs.put("миф.", "мифология");
        abbrs.put("мн. ч.", "множественное число");
        abbrs.put("мор.", "морской термин");
        abbrs.put("муз.", "музыка");
        abbrs.put("н. э.", "наша эра");
        abbrs.put("напр.", "например");
        abbrs.put("нареч.", "наречие");
        abbrs.put("нем.", "немецкий (язык)");
        abbrs.put("неодобр.", "неодобрительно");
        abbrs.put("непр.", "неправильно");
        abbrs.put("неправ.", "неправильно");
        abbrs.put("о-в", "остров");
        abbrs.put("обыкн.", "обыкновенно");
        abbrs.put("ок.", "около");
        abbrs.put("опт.", "оптика");
        abbrs.put("особ.", "особенно");
        abbrs.put("отриц.", "отрицательный");
        abbrs.put("охот.", "охота");
        abbrs.put("п.", "падеж");
        abbrs.put("палеонт.", "палеонтология");
        abbrs.put("парл.", "парламентское выражение");
        abbrs.put("первонач.", "первоначально");
        abbrs.put("перен.", "в переносном значении");
        abbrs.put("полигр.", "полиграфия");
        abbrs.put("полит.", "политический термин");
        abbrs.put("полит.-эк.", "политическая экономия");
        abbrs.put("португ.", "португальский (язык)");
        abbrs.put("посл.", "пословица");
        abbrs.put("поэт.", "поэтическое слово, выражение");
        abbrs.put("превосх. ст.", "превосходная степень");
        abbrs.put("предл.", "предложение");
        abbrs.put("презр.", "презрительно");
        abbrs.put("преим.", "преимущественно");
        abbrs.put("пренебр.", "пренебрежительно");
        abbrs.put("прил.", "имя прилагательное");
        abbrs.put("прос.", "просодия");
        abbrs.put("противоп.", "противоположное значение");
        abbrs.put("проф.", "профессиональный термин");
        abbrs.put("психол.", "психология");
        abbrs.put("р.", "1) род 2) река");
        abbrs.put("радио", "радиотехника");
        abbrs.put("разг.", "разговорное слово, выражение");
        abbrs.put("распр.", "в распространенном, неточном значении");
        abbrs.put("реакт.", "реактивная техника");
        abbrs.put("редк.", "редко");
        abbrs.put("рез.", "резиновая промышленность");
        abbrs.put("рел.", "религия");
        abbrs.put("римск. миф.", "римская мифология");
        abbrs.put("ритор.", "риторический");
        abbrs.put("русск.", "русский (язык)");
        abbrs.put("с.-х.", "сельское хозяйство");
        abbrs.put("сев.", "употребительно на севере Англии и в Шотландии");
        abbrs.put("сканд.", "скандинавский");
        abbrs.put("сл.", "сленг, жаргон");
        abbrs.put("см.", "смотри");
        abbrs.put("см. тж.", "смотри также");
        abbrs.put("собир.", "собирательно");
        abbrs.put("собств.", "имя собственное");
        abbrs.put("сокр.", "сокращение, сокращенно");
        abbrs.put("спец.", "специальный термин");
        abbrs.put("спорт.", "физкультура и спорт");
        abbrs.put("ср.", "сравни");
        abbrs.put("ср. тж.", "сравни также");
        abbrs.put("сравн. ст.", "сравнительная степень");
        abbrs.put("стат.", "статистика");
        abbrs.put("стил.", "стилистика");
        abbrs.put("стих.", "стихосложение");
        abbrs.put("стр.", "строительное дело");
        abbrs.put("страх.", "страховой термин");
        abbrs.put("студ.", "студенческое слово, выражение");
        abbrs.put("сущ.", "имя существительное");
        abbrs.put("т. е.", "то есть");
        abbrs.put("твор.", "творительный (падеж)");
        abbrs.put("театр.", "театральный термин");
        abbrs.put("текст.", "текстильное дело");
        abbrs.put("тех.", "техника");
        abbrs.put("тж.", "также");
        abbrs.put("тк.", "только");
        abbrs.put("тлв.", "телевидение");
        abbrs.put("тлг.", "телеграфия");
        abbrs.put("тлф.", "телефония");
        abbrs.put("топ.", "топография");
        abbrs.put("тур.", "турецкий (язык)");
        abbrs.put("уменьш.", "уменьшительная форма");
        abbrs.put("унив.", "университетское выражение");
        abbrs.put("употр.", "употребляется");
        abbrs.put("уст.", "устаревшее слово, выражение");
        abbrs.put("утв.", "утвердительный");
        abbrs.put("фарм.", "фармакология");
        abbrs.put("физ.", "физика");
        abbrs.put("физиол.", "физиология");
        abbrs.put("филос.", "философия");
        abbrs.put("фин.", "финансовый термин");
        abbrs.put("фон.", "фонетика");
        abbrs.put("фото", "фотография");
        abbrs.put("фр.", "французский (язык)");
        abbrs.put("хим.", "химия");
        abbrs.put("хир.", "хирургия");
        abbrs.put("церк.", "церковное слово, выражение");
        abbrs.put("ч.", "час, часов");
        abbrs.put("числ.", "числительное");
        abbrs.put("шахм.", "шахматы");
        abbrs.put("швед.", "шведский (язык)");
        abbrs.put("школ.", "школьное слово, выражение");
        abbrs.put("шотл.", "употребительно в Шотландии");
        abbrs.put("шутл.", "шутливое слово, выражение");
        abbrs.put("эвф.", "эвфемизм");
        abbrs.put("эк.", "экономика");
        abbrs.put("эл.", "электротехника");
        abbrs.put("элн.", "электроника");
        abbrs.put("этн.", "этнография");
        abbrs.put("южно-афр.", "употребительно в Южной Африке");
        abbrs.put("юр.", "юридический термин");
        abbrs.put("яп.", "японский (язык)");
    }
}
