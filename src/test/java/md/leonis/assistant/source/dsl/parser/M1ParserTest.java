package md.leonis.assistant.source.dsl.parser;

import md.leonis.assistant.source.dsl.parser.domain.IntermediateDslObject;
import md.leonis.assistant.source.dsl.parser.domain.ParserState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.*;

class M1ParserTest {

    private IntermediateDslObject dslObject = new IntermediateDslObject("word");

    @Test
    @DisplayName("[m1]word [c lightslategray]{{t}}\\[ˊbu:tblæk\\]{{/t}}[/c] [p]n[/p] ([p]преим.[/p] [p]амер.[/p])")
    void parse() {
        String m1 = "[m1]word [c lightslategray]{{t}}\\[ˊbu:tblæk\\]{{/t}}[/c] [p]n[/p] ([p]преим.[/p] [p]амер.[/p])";
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse(m1);

        assertEquals("word", dslObject.getWord());
        assertEquals("word", dslObject.getNewWord());
        assertEquals("\\[ˊbu:tblæk\\]", dslObject.getTranscription());
        assertEquals("[[], [n], []]", dslObject.getTags().toString());
        assertEquals("[[], [n], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getVars().isEmpty());
        assertEquals("[p]преим.[/p] [p]амер.[/p]", dslObject.getNotes());
        assertEquals("[]", dslObject.getLinks().toString());
        assertTrue(dslObject.getLinkSeq().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(StringUtils.compact(m1), dslObject.toM1CompactString());
    }

    @Test
    @DisplayName("[m1]word [c lightslategray]{{t}}\\[ˊbu:gɪ\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<boogie-woogie>>")
    void parse2() {
        String m1 = "[m1]word [c lightslategray]{{t}}\\[ˊbu:gɪ\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<boogie-woogie>>";
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse(m1);

        assertEquals("word", dslObject.getWord());
        assertEquals("word", dslObject.getNewWord());
        assertEquals("\\[ˊbu:gɪ\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[{EQ_ONE, boogie-woogie, {}}]", dslObject.getLinks().toString());
        assertTrue(dslObject.getLinkSeq().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(StringUtils.compact(m1), dslObject.toM1CompactString());
    }

    @Test
    @DisplayName("[m1]A, a [p]n[/p] [c lightslategray]{{t}}\\[eɪ\\]{{/t}}[/c] ([p]pl[/p] [c teal][lang id=1033]As, A's[/lang][/c] [c lightslategray]{{t}}\\[eɪz\\]{{/t}}[/c])")
    void parse3() {
        String m1 = "[m1]A, a [p]n[/p] [c lightslategray]{{t}}\\[eɪ\\]{{/t}}[/c] ([p]pl[/p] [c teal][lang id=1033]As, A's[/lang][/c] [c lightslategray]{{t}}\\[eɪz\\]{{/t}}[/c])";
        dslObject = new IntermediateDslObject("A");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse(m1);

        assertEquals("A", dslObject.getWord());
        assertEquals("A, a", dslObject.getNewWord());
        assertEquals("\\[eɪ\\]", dslObject.getTranscription());
        assertEquals("[[n], [], []]", dslObject.getTags().toString());
        assertEquals("[[n], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getVars().isEmpty());
        assertEquals("[p]pl[/p] [c teal][lang id=1033]As, A's[/lang][/c] [c lightslategray]{{t}}\\[eɪz\\]{{/t}}[/c]", dslObject.getNotes());
        assertTrue(dslObject.getLinks().isEmpty());
        assertTrue(dslObject.getLinkSeq().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(StringUtils.compact(m1), dslObject.toM1CompactString());
    }

    @Test
    @DisplayName("[m1]absent")
    void parse4() {
        String m1 = "[m1]absent";
        dslObject = new IntermediateDslObject("absent");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse(m1);

        assertEquals("absent", dslObject.getWord());
        assertEquals("absent", dslObject.getNewWord());
        assertNull(dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLinks().isEmpty());
        assertTrue(dslObject.getLinkSeq().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(StringUtils.compact(m1), dslObject.toM1CompactString());
    }

    @Test
    @DisplayName("[m1]aback [c lightslategray]{{t}}\\[əˊbæk\\]{{/t}}[/c]:")
    void parse5() {
        String m1 = "[m1]aback [c lightslategray]{{t}}\\[əˊbæk\\]{{/t}}[/c]:";
        dslObject = new IntermediateDslObject("aback");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse(m1);

        assertEquals("aback", dslObject.getWord());
        assertEquals("aback", dslObject.getNewWord());
        assertEquals("\\[əˊbæk\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLinks().isEmpty());
        assertTrue(dslObject.getLinkSeq().isEmpty());
        assertEquals(":", dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(StringUtils.compact(m1), dslObject.toM1CompactString());
    }

    @Test
    @DisplayName("[m1]ablaze [c lightslategray]{{t}}\\[əˊbleɪz\\]{{/t}}[/c] [p]a[/p] [p]predic.[/p]")
    void parse6() {
        String m1 = "[m1]ablaze [c lightslategray]{{t}}\\[əˊbleɪz\\]{{/t}}[/c] [p]a[/p] [p]predic.[/p]";
        dslObject = new IntermediateDslObject("ablaze");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse(m1);

        assertEquals("ablaze", dslObject.getWord());
        assertEquals("ablaze", dslObject.getNewWord());
        assertEquals("\\[əˊbleɪz\\]", dslObject.getTranscription());
        assertEquals("[[], [a, predic.], []]", dslObject.getTags().toString());
        assertEquals("[[], [a, predic.], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLinks().isEmpty());
        assertTrue(dslObject.getLinkSeq().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(StringUtils.compact(m1), dslObject.toM1CompactString());
    }

    @Test
    @DisplayName("[m1]aborigine [c lightslategray]{{t}}\\[ˏæbəˊrɪdʒɪnɪ\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<aboriginal>> [c blue]2[/c]")
    void parse7() {
        String m1 = "[m1]aborigine [c lightslategray]{{t}}\\[ˏæbəˊrɪdʒɪnɪ\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<aboriginal>> [c blue]2[/c]";
        dslObject = new IntermediateDslObject("aborigine");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse(m1);

        assertEquals("aborigine", dslObject.getWord());
        assertEquals("aborigine", dslObject.getNewWord());
        assertEquals("\\[ˏæbəˊrɪdʒɪnɪ\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[{EQ_ONE, aboriginal, {={2=[]}}}]", dslObject.getLinks().toString());
        assertThat(dslObject.getLinkSeq(), contains("2"));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(StringUtils.compact(m1), dslObject.toM1CompactString());
    }

    @Test
    @DisplayName("[m1]alleluia [c lightslategray]{{t}}\\[ˏæləˊlu:jə\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<halleluja>>[c blue],[/c] <<hallelujah>>")
    void parse8() {
        String m1 = "[m1]alleluia [c lightslategray]{{t}}\\[ˏæləˊlu:jə\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<halleluja>>[c blue],[/c] <<hallelujah>>";
        dslObject = new IntermediateDslObject("alleluia");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse(m1);

        assertEquals("alleluia", dslObject.getWord());
        assertEquals("alleluia", dslObject.getNewWord());
        assertEquals("\\[ˏæləˊlu:jə\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[{EQ_ONE, halleluja, {}}, {EQ_ONE, hallelujah, {}}]", dslObject.getLinks().toString());
        assertTrue(dslObject.getLinkSeq().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(StringUtils.compact(m1), dslObject.toM1CompactString());
    }

    @Test
    @DisplayName("[m1]arise [c lightslategray]{{t}}\\[əˊraɪz\\]{{/t}}[/c] [p]v[/p] [c mediumvioletred](arose; arisen)[/c]")
    void parse9() {
        String m1 = "[m1]arise [c lightslategray]{{t}}\\[əˊraɪz\\]{{/t}}[/c] [p]v[/p] [c mediumvioletred](arose; arisen)[/c]";
        dslObject = new IntermediateDslObject("arise");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse(m1);

        assertEquals("arise", dslObject.getWord());
        assertEquals("arise", dslObject.getNewWord());
        assertEquals("\\[əˊraɪz\\]", dslObject.getTranscription());
        assertEquals("[[], [v], []]", dslObject.getTags().toString());
        assertEquals("[[], [v], []]", dslObject.getTagsSeq().toString());
        assertThat(dslObject.getVars(), contains("arose", "arisen"));
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLinks().isEmpty());
        assertTrue(dslObject.getLinkSeq().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(StringUtils.compact(m1), dslObject.toM1CompactString());
    }

    @Test
    @DisplayName("[m1]about-face [c lightslategray]{{t}}\\[əˏbaυtˊfeɪs\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<about-turn>> [c blue]1, 2[/c]")
    void parse10() {
        String m1 = "[m1]about-face [c lightslategray]{{t}}\\[əˏbaυtˊfeɪs\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<about-turn>> [c blue]1, 2[/c]";
        dslObject = new IntermediateDslObject("about-face");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse(m1);

        assertEquals("about-face", dslObject.getWord());
        assertEquals("about-face", dslObject.getNewWord());
        assertEquals("\\[əˏbaυtˊfeɪs\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[{EQ_ONE, about-turn, {={1=[], 2=[]}}}]", dslObject.getLinks().toString());
        assertThat(dslObject.getLinkSeq(), contains("1", "2"));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(StringUtils.compact(m1), dslObject.toM1CompactString());
    }

    @Test
    @DisplayName("[m1]accusal [c lightslategray]{{t}}\\[əˊkju:zǝl\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<accusation>> [c blue]1)[/c]")
    void parse11() {
        String m1 = "[m1]accusal [c lightslategray]{{t}}\\[əˊkju:zǝl\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<accusation>> [c blue]1)[/c]";
        dslObject = new IntermediateDslObject("accusal");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse(m1);

        assertEquals("accusal", dslObject.getWord());
        assertEquals("accusal", dslObject.getNewWord());
        assertEquals("\\[əˊkju:zǝl\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[{EQ_ONE, accusation, {={=[1)]}}}]", dslObject.getLinks().toString());
        assertThat(dslObject.getLinkSeq(), contains("1)"));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(StringUtils.compact(m1), dslObject.toM1CompactString());
    }

    @Test
    @DisplayName("[m1]alleyway [c lightslategray]{{t}}\\[ˊælɪweɪ\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<alley>> [c blue]Ⅰ,[/c] [c blue]1)[/c] [i]и[/i] [c blue]2)[/c]")
    void parse12() {
        String m1 = "[m1]alleyway [c lightslategray]{{t}}\\[ˊælɪweɪ\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<alley>> [c blue]Ⅰ,[/c] [c blue]1)[/c] [i]и[/i] [c blue]2)[/c]";
        dslObject = new IntermediateDslObject("alleyway");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse(m1);

        assertEquals("alleyway", dslObject.getWord());
        assertEquals("alleyway", dslObject.getNewWord());
        assertEquals("\\[ˊælɪweɪ\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[{EQ_ONE, alley, {Ⅰ={=[1), 2)]}}}]", dslObject.getLinks().toString());
        assertThat(dslObject.getLinkSeq(), contains("Ⅰ", "1)", "[i]и[/i]", "2)"));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(StringUtils.compact(m1), dslObject.toM1CompactString());
    }

    @Test
    @DisplayName("[m1]anyway [c lightslategray]{{t}}\\[ˊenɪweɪ\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<anyhow>> [c blue]1),[/c] [c blue]2)[/c]")
    void parse13() {
        String m1 = "[m1]anyway [c lightslategray]{{t}}\\[ˊenɪweɪ\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<anyhow>> [c blue]1),[/c] [c blue]2)[/c]";
        dslObject = new IntermediateDslObject("anyway");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse(m1);

        assertEquals("anyway", dslObject.getWord());
        assertEquals("anyway", dslObject.getNewWord());
        assertEquals("\\[ˊenɪweɪ\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[{EQ_ONE, anyhow, {={=[1), 2)]}}}]", dslObject.getLinks().toString());
        assertThat(dslObject.getLinkSeq(), contains("1)", "2)"));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(StringUtils.compact(m1), dslObject.toM1CompactString());
    }

    @Test
    @DisplayName("[m1]balsamic [c lightslategray]{{t}}\\[bɔ:lˊsæmɪk\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<balmy>> [c blue]1)[/c] [i]и[/i] [c blue]4)[/c]")
    void parse14() {
        String m1 = "[m1]balsamic [c lightslategray]{{t}}\\[bɔ:lˊsæmɪk\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<balmy>> [c blue]1)[/c] [i]и[/i] [c blue]4)[/c]";
        dslObject = new IntermediateDslObject("balsamic");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse(m1);

        assertEquals("balsamic", dslObject.getWord());
        assertEquals("balsamic", dslObject.getNewWord());
        assertEquals("\\[bɔ:lˊsæmɪk\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[{EQ_ONE, balmy, {={=[1), 4)]}}}]", dslObject.getLinks().toString());
        assertThat(dslObject.getLinkSeq(), contains("1)", "[i]и[/i]", "4)"));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(StringUtils.compact(m1), dslObject.toM1CompactString());
    }

    @Test
    @DisplayName("[m1]bow-knot [c lightslategray]{{t}}\\[ˊbəυnɒt\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<bow>> [c blue]Ⅱ,[/c] [c blue]1,[/c] [c blue]1)[/c]")
    void parse15() {
        String m1 = "[m1]bow-knot [c lightslategray]{{t}}\\[ˊbəυnɒt\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<bow>> [c blue]Ⅱ,[/c] [c blue]1,[/c] [c blue]1)[/c]";
        dslObject = new IntermediateDslObject("bow-knot");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse(m1);

        assertEquals("bow-knot", dslObject.getWord());
        assertEquals("bow-knot", dslObject.getNewWord());
        assertEquals("\\[ˊbəυnɒt\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[{EQ_ONE, bow, {Ⅱ={1=[1)]}}}]", dslObject.getLinks().toString());
        assertThat(dslObject.getLinkSeq(), contains("Ⅱ", "1", "1)"));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(StringUtils.compact(m1), dslObject.toM1CompactString());
    }

    @Test
    @DisplayName("[m1]beheld [c lightslategray]{{t}}\\[bɪˊheld\\]{{/t}}[/c] [p]past[/p] [i]и[/i] [p]p. p.[/p] [i]от[/i] <<behold>> [c blue]1[/c]")
    void parse16() {
        String m1 = "[m1]beheld [c lightslategray]{{t}}\\[bɪˊheld\\]{{/t}}[/c] [p]past[/p] [i]и[/i] [p]p. p.[/p] [i]от[/i] <<behold>> [c blue]1[/c]";
        dslObject = new IntermediateDslObject("beheld");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse(m1);

        assertEquals("beheld", dslObject.getWord());
        assertEquals("beheld", dslObject.getNewWord());
        assertEquals("\\[bɪˊheld\\]", dslObject.getTranscription());
        assertEquals("[[], [past, p. p.], []]", dslObject.getTags().toString());
        assertEquals("[[], [past, [i]и[/i], p. p.], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[{FROM_TWO, behold, {={1=[]}}}]", dslObject.getLinks().toString());
        assertThat(dslObject.getLinkSeq(), contains("1"));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(StringUtils.compact(m1), dslObject.toM1CompactString());
    }

    @Test
    @DisplayName("[m1]archeo- [c lightslategray]{{t}}\\[ˊɑ:kɪə(υ)-\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] [c lightseagreen][lang id=1033]archaeo-[/lang][/c]")
    void parse17() {
        String m1 = "[m1]archeo- [c lightslategray]{{t}}\\[ˊɑ:kɪə(υ)-\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] [c lightseagreen][lang id=1033]archaeo-[/lang][/c]";
        dslObject = new IntermediateDslObject("archeo-");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse(m1);

        assertEquals("archeo-", dslObject.getWord());
        assertEquals("archeo-", dslObject.getNewWord());
        assertEquals("\\[ˊɑ:kɪə(υ)-\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[{EQ_GREEN, archaeo-, {}}]", dslObject.getLinks().toString());
        assertTrue(dslObject.getLinkSeq().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(StringUtils.compact(m1), dslObject.toM1CompactString());
    }

    @Test
    @DisplayName("[m1]axes [c lightslategray]{{t}}\\[ˊæksɪz\\]{{/t}}[/c] [p]pl[/p] [i]от[/i] <<ax>>[c blue] 1,[/c] <<axe>>[c blue] 1[/c]")
    void parse18() {
        String m1 = "[m1]axes [c lightslategray]{{t}}\\[ˊæksɪz\\]{{/t}}[/c] [p]pl[/p] [i]от[/i] <<ax>>[c blue] 1,[/c] <<axe>>[c blue] 1[/c]";
        dslObject = new IntermediateDslObject("axes");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse(m1);

        assertEquals("axes", dslObject.getWord());
        assertEquals("axes", dslObject.getNewWord());
        assertEquals("\\[ˊæksɪz\\]", dslObject.getTranscription());
        assertEquals("[[], [pl], []]", dslObject.getTags().toString());
        assertEquals("[[], [pl], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[{FROM_TWO, ax, {={1=[]}}}, {FROM_TWO, axe, {={1=[]}}}]", dslObject.getLinks().toString());
        assertThat(dslObject.getLinkSeq(), contains("1", "1"));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(StringUtils.compact(m1), dslObject.toM1CompactString());
    }

    @Test
    @DisplayName("[m1]blew [c lightslategray]{{t}}\\[blu:\\]{{/t}}[/c] [p]past[/p] [i]от[/i] <<blow>> [c blue]Ⅱ,[/c] [c blue]2[/c] [i]и[/i] <<blow>> [c blue]Ⅲ,[/c] [c blue]2[/c]")
    void parse19() {
        String m1 = "[m1]blew [c lightslategray]{{t}}\\[blu:\\]{{/t}}[/c] [p]past[/p] [i]от[/i] <<blow>> [c blue]Ⅱ,[/c] [c blue]2[/c] [i]и[/i] <<blow>> [c blue]Ⅲ,[/c] [c blue]2[/c]";
        dslObject = new IntermediateDslObject("blew");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse(m1);

        assertEquals("blew", dslObject.getWord());
        assertEquals("blew", dslObject.getNewWord());
        assertEquals("\\[blu:\\]", dslObject.getTranscription());
        assertEquals("[[], [past], []]", dslObject.getTags().toString());
        assertEquals("[[], [past], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[{FROM_TWO, blow, {Ⅱ={2=[]}}}, {FROM_TWO, blow, {Ⅲ={2=[]}}}]", dslObject.getLinks().toString());
        assertThat(dslObject.getLinkSeq(), contains("Ⅱ", "2", "[i]и[/i]", "Ⅲ", "2"));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(StringUtils.compact(m1), dslObject.toM1CompactString());
    }

    @Test
    @DisplayName("[m1]briar [c lightslategray]{{t}}\\[ˊbra(ɪ)ə\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<brier>> [c blue]Ⅰ[/c] [i]и[/i] <<brier>> [c blue]Ⅱ[/c]")
    void parse20() {
        String m1 = "[m1]briar [c lightslategray]{{t}}\\[ˊbra(ɪ)ə\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<brier>> [c blue]Ⅰ[/c] [i]и[/i] <<brier>> [c blue]Ⅱ[/c]";
        dslObject = new IntermediateDslObject("briar");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse(m1);

        assertEquals("briar", dslObject.getWord());
        assertEquals("briar", dslObject.getNewWord());
        assertEquals("\\[ˊbra(ɪ)ə\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[{EQ_ONE, brier, {Ⅰ={}}}, {EQ_ONE, brier, {Ⅱ={}}}]", dslObject.getLinks().toString());
        assertThat(dslObject.getLinkSeq(), contains("Ⅰ", "[i]и[/i]", "Ⅱ"));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(StringUtils.compact(m1), dslObject.toM1CompactString());
    }

    @Test
    @DisplayName("[m1]calves [c lightslategray]{{t}}\\[kɑ:vz\\]{{/t}}[/c] [p]pl[/p] [i]от[/i] <<calf>> [c blue]Ⅰ[/c] [i]и[/i] <<calf>> [c blue]Ⅱ[/c]")
    void parse21() {
        String m1 = "[m1]calves [c lightslategray]{{t}}\\[kɑ:vz\\]{{/t}}[/c] [p]pl[/p] [i]от[/i] <<calf>> [c blue]Ⅰ[/c] [i]и[/i] <<calf>> [c blue]Ⅱ[/c]";
        dslObject = new IntermediateDslObject("calves");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse(m1);

        assertEquals("calves", dslObject.getWord());
        assertEquals("calves", dslObject.getNewWord());
        assertEquals("\\[kɑ:vz\\]", dslObject.getTranscription());
        assertEquals("[[], [pl], []]", dslObject.getTags().toString());
        assertEquals("[[], [pl], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[{FROM_TWO, calf, {Ⅰ={}}}, {FROM_TWO, calf, {Ⅱ={}}}]", dslObject.getLinks().toString());
        assertThat(dslObject.getLinkSeq(), contains("Ⅰ", "[i]и[/i]", "Ⅱ"));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(StringUtils.compact(m1), dslObject.toM1CompactString());
    }

    @Test
    @DisplayName("[m1]curst [c lightslategray]{{t}}\\[kɜ:st\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<cursed>> [c blue]2[/c] [i]и[/i] [c blue]3[/c]")
    void parse22() {
        String m1 = "[m1]curst [c lightslategray]{{t}}\\[kɜ:st\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<cursed>> [c blue]2[/c] [i]и[/i] [c blue]3[/c]";
        dslObject = new IntermediateDslObject("curst");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse(m1);

        assertEquals("curst", dslObject.getWord());
        assertEquals("curst", dslObject.getNewWord());
        assertEquals("\\[kɜ:st\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[{EQ_ONE, cursed, {={2=[], 3=[]}}}]", dslObject.getLinks().toString());
        assertThat(dslObject.getLinkSeq(), contains("2", "[i]и[/i]", "3"));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        //assertEquals(StringUtils.compact(m1), dslObject.toM1CompactString());
    }

    @Test
    @DisplayName("[m1]upstair [c lightslategray]{{t}}\\[ˏʌpˊsteə\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<upstairs>> [c blue]1,[/c] [c blue]1)[/c] [i]и[/i] [c blue]3[/c]")
    void parse23() {
        String m1 = "[m1]upstair [c lightslategray]{{t}}\\[ˏʌpˊsteə\\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<upstairs>> [c blue]1,[/c] [c blue]1)[/c] [i]и[/i] [c blue]3[/c]";
        dslObject = new IntermediateDslObject("upstair");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse(m1);

        assertEquals("upstair", dslObject.getWord());
        assertEquals("upstair", dslObject.getNewWord());
        assertEquals("\\[ˏʌpˊsteə\\]", dslObject.getTranscription());
        assertEquals("[[], [], []]", dslObject.getTags().toString());
        assertEquals("[[], [], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[{EQ_ONE, upstairs, {={1=[1)], 3=[]}}}]", dslObject.getLinks().toString());
        assertThat(dslObject.getLinkSeq(), contains("1", "1)", "[i]и[/i]", "3"));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        //assertEquals(StringUtils.compact(m1), dslObject.toM1CompactString());
    }

    @Test
    @DisplayName("[m1]aboard [c lightslategray]{{t}}\\[əˊbɔ:d\\]{{/t}}[/c] [p]adv[/p][i],[/i] [p]prep[/p]")
    void parse24() {
        String m1 = "[m1]aboard [c lightslategray]{{t}}\\[əˊbɔ:d\\]{{/t}}[/c] [p]adv[/p][i],[/i] [p]prep[/p]";
        dslObject = new IntermediateDslObject("aboard");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse(m1);

        assertEquals("aboard", dslObject.getWord());
        assertEquals("aboard", dslObject.getNewWord());
        assertEquals("\\[əˊbɔ:d\\]", dslObject.getTranscription());
        assertEquals("[[], [adv, prep], []]", dslObject.getTags().toString());
        assertEquals("[[], [adv, [i],[/i], prep], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLinks().isEmpty());
        assertTrue(dslObject.getLinkSeq().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(StringUtils.compact(m1), dslObject.toM1CompactString());
    }

    @Test
    @DisplayName("[m1]à la carte [c lightslategray]{{t}}\\[ˏæləˊkɑ:t\\]{{/t}}[/c] [p]a[/p], [p]adv[/p]")
    void parse25() {
        String m1 = "[m1]à la carte [c lightslategray]{{t}}\\[ˏæləˊkɑ:t\\]{{/t}}[/c] [p]a[/p], [p]adv[/p]";
        dslObject = new IntermediateDslObject("à la carte");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse(m1);

        assertEquals("à la carte", dslObject.getWord());
        assertEquals("à la carte", dslObject.getNewWord());
        assertEquals("\\[ˏæləˊkɑ:t\\]", dslObject.getTranscription());
        assertEquals("[[], [a, adv], []]", dslObject.getTags().toString());
        assertEquals("[[], [a, ,, adv], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLinks().isEmpty());
        assertTrue(dslObject.getLinkSeq().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(StringUtils.compact(m1), dslObject.toM1CompactString());
    }

    @Test
    @DisplayName("[m1]betake [c lightslategray]{{t}}\\[bɪˊteɪk\\]{{/t}}[/c] [p]v[/p] [c mediumvioletred](betook; betaken)[/c] [p]refl.[/p]")
    void parse26() {
        String m1 = "[m1]betake [c lightslategray]{{t}}\\[bɪˊteɪk\\]{{/t}}[/c] [p]v[/p] [c mediumvioletred](betook; betaken)[/c] [p]refl.[/p]";
        dslObject = new IntermediateDslObject("betake");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse(m1);

        assertEquals("betake", dslObject.getWord());
        assertEquals("betake", dslObject.getNewWord());
        assertEquals("\\[bɪˊteɪk\\]", dslObject.getTranscription());
        assertEquals("[[], [v], [refl.]]", dslObject.getTags().toString());
        assertEquals("[[], [v], [refl.]]", dslObject.getTagsSeq().toString());
        assertThat(dslObject.getVars(), contains("betook", "betaken"));
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLinks().isEmpty());
        assertTrue(dslObject.getLinkSeq().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(StringUtils.compact(m1), dslObject.toM1CompactString());
    }

    @Test
    @DisplayName("[m1]caries [c lightslategray]{{t}}\\[ˊkeərɪz\\]{{/t}}[/c] [i]n[/i]")
    void parse27() {
        String m1 = "[m1]caries [c lightslategray]{{t}}\\[ˊkeərɪz\\]{{/t}}[/c] [i]n[/i]";
        dslObject = new IntermediateDslObject("caries");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse(m1);

        assertEquals("caries", dslObject.getWord());
        assertEquals("caries", dslObject.getNewWord());
        assertEquals("\\[ˊkeərɪz\\]", dslObject.getTranscription());
        assertEquals("[[], [n], []]", dslObject.getTags().toString());
        assertEquals("[[], [n], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLinks().isEmpty());
        assertTrue(dslObject.getLinkSeq().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        //assertEquals(StringUtils.compact(m1), dslObject.toM1CompactString());
    }

    @Test
    @DisplayName("[m1]continuing education [c lightslategray]{{t}}\\[kənˏtɪnju:ɪŋedjυˊkeɪʃən\\]{{/t}}[/c][i] n[/i]")
    void parse28() {
        String m1 = "[m1]continuing education [c lightslategray]{{t}}\\[kənˏtɪnju:ɪŋedjυˊkeɪʃən\\]{{/t}}[/c][i] n[/i]";
        dslObject = new IntermediateDslObject("continuing education");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse(m1);

        assertEquals("continuing education", dslObject.getWord());
        assertEquals("continuing education", dslObject.getNewWord());
        assertEquals("\\[kənˏtɪnju:ɪŋedjυˊkeɪʃən\\]", dslObject.getTranscription());
        assertEquals("[[], [n], []]", dslObject.getTags().toString());
        assertEquals("[[], [ n], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLinks().isEmpty());
        assertTrue(dslObject.getLinkSeq().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        //assertEquals(StringUtils.compact(m1), dslObject.toM1CompactString());
    }

    @Test
    @DisplayName("[m1]goggle-eyed [c lightslategray]{{t}}\\[ˏgɒglˊaɪd\\]{{/t}}[/c] [c teal][lang id=1033]a[/lang][/c]")
    void parse29() {
        String m1 = "[m1]goggle-eyed [c lightslategray]{{t}}\\[ˏgɒglˊaɪd\\]{{/t}}[/c] [c teal][lang id=1033]a[/lang][/c]";
        dslObject = new IntermediateDslObject("goggle-eyed");
        M1Parser m1Parser = new M1Parser(dslObject);
        m1Parser.parse(m1);

        assertEquals("goggle-eyed", dslObject.getWord());
        assertEquals("goggle-eyed", dslObject.getNewWord());
        assertEquals("\\[ˏgɒglˊaɪd\\]", dslObject.getTranscription());
        assertEquals("[[], [a], []]", dslObject.getTags().toString());
        assertEquals("[[], [a], []]", dslObject.getTagsSeq().toString());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLinks().isEmpty());
        assertTrue(dslObject.getLinkSeq().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        //assertEquals(StringUtils.compact(m1), dslObject.toM1CompactString());
    }

    //TODo
    // new fields:
    // translations -> word, nearly flag, examples

    // [m1]stone-cold [c lightslategray]{{t}}\[ˏstəυnˊkəυld\]{{/t}}[/c] [p]a[/p] [c darkred]≅[/c]

    //TODO switch to notes; finally retest tails



    // new fields
    // sameAs list -> in translation object
    // abbreviated list
    // Links: SEE_ALSO, SEE
    // see
}
