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
        assertTrue(dslObject.getTags1().isEmpty());
        assertThat(dslObject.getTags2(), contains("n"));
        assertThat(dslObject.getTags2Seq(), contains("n"));
        assertTrue(dslObject.getVars().isEmpty());
        assertEquals("[p]преим.[/p] [p]амер.[/p]", dslObject.getNotes());
        assertEquals("[]", dslObject.getLinks().toString());
        assertTrue(dslObject.getLinkSeq().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(m1, dslObject.toM1String());
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
        assertTrue(dslObject.getTags1().isEmpty());
        assertTrue(dslObject.getTags2().isEmpty());
        assertTrue(dslObject.getTags2Seq().isEmpty());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[{ONE, boogie-woogie, {}}]", dslObject.getLinks().toString());
        assertTrue(dslObject.getLinkSeq().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(m1, dslObject.toM1String());
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
        assertThat(dslObject.getTags1(), contains("n"));
        assertTrue(dslObject.getTags2().isEmpty());
        assertTrue(dslObject.getTags2Seq().isEmpty());
        assertTrue(dslObject.getVars().isEmpty());
        assertEquals("[p]pl[/p] [c teal][lang id=1033]As, A's[/lang][/c] [c lightslategray]{{t}}\\[eɪz\\]{{/t}}[/c]", dslObject.getNotes());
        assertTrue(dslObject.getLinks().isEmpty());
        assertTrue(dslObject.getLinkSeq().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(m1, dslObject.toM1String());
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
        assertTrue(dslObject.getTags1().isEmpty());
        assertTrue(dslObject.getTags2().isEmpty());
        assertTrue(dslObject.getTags2Seq().isEmpty());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLinks().isEmpty());
        assertTrue(dslObject.getLinkSeq().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(m1, dslObject.toM1String());
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
        assertTrue(dslObject.getTags1().isEmpty());
        assertTrue(dslObject.getTags2().isEmpty());
        assertTrue(dslObject.getTags2Seq().isEmpty());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLinks().isEmpty());
        assertTrue(dslObject.getLinkSeq().isEmpty());
        assertEquals(":", dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(m1, dslObject.toM1String());
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
        assertTrue(dslObject.getTags1().isEmpty());
        assertThat(dslObject.getTags2(), contains("a", "predic."));
        assertThat(dslObject.getTags2Seq(), contains("a", "predic."));
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLinks().isEmpty());
        assertTrue(dslObject.getLinkSeq().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(m1, dslObject.toM1String());
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
        assertTrue(dslObject.getTags1().isEmpty());
        assertTrue(dslObject.getTags2().isEmpty());
        assertTrue(dslObject.getTags2Seq().isEmpty());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[{ONE, aboriginal, {={2=[]}}}]", dslObject.getLinks().toString());
        assertThat(dslObject.getLinkSeq(), contains("2"));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(m1, dslObject.toM1String());
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
        assertTrue(dslObject.getTags1().isEmpty());
        assertTrue(dslObject.getTags2().isEmpty());
        assertTrue(dslObject.getTags2Seq().isEmpty());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[{ONE, halleluja, {}}, {ONE, hallelujah, {}}]", dslObject.getLinks().toString());
        assertTrue(dslObject.getLinkSeq().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(m1, dslObject.toM1String());
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
        assertTrue(dslObject.getTags1().isEmpty());
        assertThat(dslObject.getTags2(), contains("v"));
        assertThat(dslObject.getTags2Seq(), contains("v"));
        assertThat(dslObject.getVars(), contains("arose", "arisen"));
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLinks().isEmpty());
        assertTrue(dslObject.getLinkSeq().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(m1, dslObject.toM1String());
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
        assertTrue(dslObject.getTags1().isEmpty());
        assertTrue(dslObject.getTags2().isEmpty());
        assertTrue(dslObject.getTags2Seq().isEmpty());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[{ONE, about-turn, {={1=[], 2=[]}}}]", dslObject.getLinks().toString());
        assertThat(dslObject.getLinkSeq(), contains("1", "2"));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(m1, dslObject.toM1String());
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
        assertTrue(dslObject.getTags1().isEmpty());
        assertTrue(dslObject.getTags2().isEmpty());
        assertTrue(dslObject.getTags2Seq().isEmpty());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[{ONE, accusation, {={=[1)]}}}]", dslObject.getLinks().toString());
        assertThat(dslObject.getLinkSeq(), contains("1)"));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(m1, dslObject.toM1String());
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
        assertTrue(dslObject.getTags1().isEmpty());
        assertTrue(dslObject.getTags2().isEmpty());
        assertTrue(dslObject.getTags2Seq().isEmpty());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[{ONE, alley, {Ⅰ={=[1), 2)]}}}]", dslObject.getLinks().toString());
        assertThat(dslObject.getLinkSeq(), contains("Ⅰ", "1)", "[i]и[/i]", "2)"));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(m1, dslObject.toM1String());
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
        assertTrue(dslObject.getTags1().isEmpty());
        assertTrue(dslObject.getTags2().isEmpty());
        assertTrue(dslObject.getTags2Seq().isEmpty());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[{ONE, anyhow, {={=[1), 2)]}}}]", dslObject.getLinks().toString());
        assertThat(dslObject.getLinkSeq(), contains("1)", "2)"));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(m1, dslObject.toM1String());
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
        assertTrue(dslObject.getTags1().isEmpty());
        assertTrue(dslObject.getTags2().isEmpty());
        assertTrue(dslObject.getTags2Seq().isEmpty());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[{ONE, balmy, {={=[1), 4)]}}}]", dslObject.getLinks().toString());
        assertThat(dslObject.getLinkSeq(), contains("1)", "[i]и[/i]", "4)"));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(m1, dslObject.toM1String());
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
        assertTrue(dslObject.getTags1().isEmpty());
        assertTrue(dslObject.getTags2().isEmpty());
        assertTrue(dslObject.getTags2Seq().isEmpty());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[{ONE, bow, {Ⅱ={1=[1)]}}}]", dslObject.getLinks().toString());
        assertThat(dslObject.getLinkSeq(), contains("Ⅱ", "1", "1)"));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(m1, dslObject.toM1String());
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
        assertTrue(dslObject.getTags1().isEmpty());
        assertThat(dslObject.getTags2(), contains("past", "p. p."));
        assertThat(dslObject.getTags2Seq(), contains("past", "[i]и[/i]", "p. p."));
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[{TWO, behold, {={1=[]}}}]", dslObject.getLinks().toString());
        assertThat(dslObject.getLinkSeq(), contains("1"));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(m1, dslObject.toM1String());
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
        assertTrue(dslObject.getTags1().isEmpty());
        assertTrue(dslObject.getTags2().isEmpty());
        assertTrue(dslObject.getTags2Seq().isEmpty());
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[{GREEN, archaeo-, {}}]", dslObject.getLinks().toString());
        assertTrue(dslObject.getLinkSeq().isEmpty());
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(m1, dslObject.toM1String());
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
        assertTrue(dslObject.getTags1().isEmpty());
        assertThat(dslObject.getTags2(), contains("pl"));
        assertThat(dslObject.getTags2Seq(), contains("pl"));
        assertTrue(dslObject.getVars().isEmpty());
        assertNull(dslObject.getNotes());
        assertEquals("[{TWO, ax, {={1=[]}}}, {TWO, axe, {={1=[]}}}]", dslObject.getLinks().toString());
        assertThat(dslObject.getLinkSeq(), contains("1", "1"));
        assertNull(dslObject.getTail());
        assertEquals(ParserState.TRN, dslObject.getState());
        assertEquals(m1.replace("[c blue] 1", " [c blue]1"), dslObject.toM1String());
    }

    //TODO
    // TODO when next link - start new group in linkaddress.

    //TODO new object Link(type, addresses)
    //TODO new enum LinkType: ONE, TWO, GREEN
    //TODO delete all links, rewrite, unify

    // в цикле читать: линк, числа, игноры
    // [m1]blew [c lightslategray]{{t}}\[blu:\]{{/t}}[/c] [p]past[/p] [i]от[/i] <<blow>> [c blue]Ⅱ,[/c] [c blue]2[/c] [i]и[/i] <<blow>> [c blue]Ⅲ,[/c] [c blue]2[/c]
    // [m1]briar [c lightslategray]{{t}}\[ˊbra(ɪ)ə\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<brier>> [c blue]Ⅰ[/c] [i]и[/i] <<brier>> [c blue]Ⅱ[/c]
    // [m1]calves [c lightslategray]{{t}}\[kɑ:vz\]{{/t}}[/c] [p]pl[/p] [i]от[/i] <<calf>> [c blue]Ⅰ[/c] [i]и[/i] <<calf>> [c blue]Ⅱ[/c]
    // [m1]curst [c lightslategray]{{t}}\[kɜ:st\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<cursed>> [c blue]2[/c] [i]и[/i] [c blue]3[/c]
}
