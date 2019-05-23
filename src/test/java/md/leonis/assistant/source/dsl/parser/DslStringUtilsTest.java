package md.leonis.assistant.source.dsl.parser;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static md.leonis.assistant.source.dsl.parser.M1Parser.ABBR;
import static md.leonis.assistant.source.dsl.parser.M1Parser.FROM;
import static org.junit.jupiter.api.Assertions.*;

class DslStringUtilsTest {

    @Test
    void getBody() {
        assertEquals("asd", DslStringUtils.getBody("<p>asd</p>", new Pair<>("<p>", "</p>")));
    }

    @Test
    void getBody2() {
        assertEquals("asd", DslStringUtils.getBody("<c><p>asd</p></c>", new Pair<>("<p>", "</p>")));
    }

    @Test
    void getBody3() {
        assertEquals("<p>asd</p>", DslStringUtils.getBody("<c><p>asd</p></c>", new Pair<>("<c>", "</c>")));
    }

    @Test
    void getBodyError() {
        assertThrows(IllegalStateException.class, () -> DslStringUtils.getBody("<p>asd</p>", new Pair<>("<c>", "</p>")));
    }

    @Test
    void getBodyError2() {
        assertThrows(IllegalStateException.class, () -> DslStringUtils.getBody("<p>asd</p>", new Pair<>("</p>", "<p>")));
    }

    @Test
    void trimOuterBody() {
        assertEquals("", DslStringUtils.trimOuterBody("<p>asd</p>", new Pair<>("<p>", "</p>")));
    }

    @Test
    void trimOuterBody2() {
        assertEquals("<c></c>", DslStringUtils.trimOuterBody("<c><p>asd</p></c>", new Pair<>("<p>", "</p>")));
    }

    @Test
    void trimOuterBody3() {
        assertEquals("", DslStringUtils.trimOuterBody("<c><p>asd</p></c>", new Pair<>("<c>", "</c>")));
    }

    @Test
    void trimOuterBodyError() {
        assertThrows(IllegalStateException.class, () -> DslStringUtils.trimOuterBody("<p>asd</p>", new Pair<>("<c>", "</p>")));
    }

    @Test
    void trimOuterBodyError2() {
        assertThrows(IllegalStateException.class, () -> DslStringUtils.trimOuterBody("<p>asd</p>", new Pair<>("</p>", "<p>")));
    }

    @Test
    void tryGetBodyTriple() {
        assertEquals(Optional.of(new Pair<>(0, 12)), DslStringUtils.tryGetBody("[p]сокр.[/p]", ABBR));
    }

    @Test
    void tryGetBodyTriple2() {
        assertEquals(Optional.of(new Pair<>(0, 23)), DslStringUtils.tryGetBody("[p] сокр.[/p] [i]от[/i]", ABBR, FROM));
    }

    @Test
    void tryGetBodyTriple3() {
        assertEquals(Optional.of(new Pair<>(0, 26)), DslStringUtils.tryGetBody("[p] сокр. [/p]  [i]от [/i]", ABBR, FROM));
    }

    @Test
    void trimOuterBodyTriple() {
        assertEquals("", DslStringUtils.trim("[p]сокр.[/p]", new Pair<>(0, 12)));
    }

    @Test
    void trimOuterBodyTriple2() {
        assertEquals("", DslStringUtils.trim("[p] сокр.[/p] [i]от[/i]", new Pair<>(0, 23)));
    }

    @Test
    void trimOuterBodyTriple3() {
        assertEquals(":", DslStringUtils.trim("[p] сокр. [/p]  [i]от [/i]:", new Pair<>(0, 26)));
    }

    @Test
    void toIndexesMap() {
        Map<Integer, String> indexes = DslStringUtils.toIndexesMap("asd", new Pair<>("(", ")"));
        assertTrue(indexes.isEmpty());
    }

    @Test
    void toIndexesMap2() {
        Map<Integer, String> indexes = DslStringUtils.toIndexesMap("(asd)", new Pair<>("(", ")"));
        assertEquals(indexes.get(0), "(");
        assertEquals(indexes.get(4), ")");
    }

    @Test
    void toIndexesMap3() {
        Map<Integer, String> indexes = DslStringUtils.toIndexesMap(" (asd) ", new Pair<>("(", ")"));
        assertEquals(indexes.get(1), "(");
        assertEquals(indexes.get(5), ")");
    }

    @Test
    void toIndexesMap4() {
        Map<Integer, String> indexes = DslStringUtils.toIndexesMap(" ((asd)) ", new Pair<>("((", "))"));
        assertEquals(indexes.get(1), "((");
        assertEquals(indexes.get(6), "))");
    }

    @Test
    void tryGetBodyExactly() {
        Optional<String> s = DslStringUtils.tryGetBodyExactly("asd", new Pair<>("(", ")"));
        assertFalse(s.isPresent());
    }

    @Test
    void tryGetBodyExactly2() {
        Optional<String> s = DslStringUtils.tryGetBodyExactly("(asd)", new Pair<>("(", ")"));
        assertEquals("asd", s.orElse(""));
    }

    @Test
    void tryGetBodyExactly3() {
        Optional<String> s = DslStringUtils.tryGetBodyExactly(" ( asd ) ", new Pair<>("(", ")"));
        assertEquals("", s.orElse(""));
    }

    @Test
    void tryGetBodyExactly4() {
        Optional<String> s = DslStringUtils.tryGetBodyExactly("( asd ) ", new Pair<>("(", ")"));
        assertEquals(" asd ", s.orElse(""));
    }

    @Test
    void tryGetBodyExactly6() {
        Optional<String> s = DslStringUtils.tryGetBodyExactly("( 1) ) ", new Pair<>("(", ")"));
        assertEquals(" 1) ", s.orElse(""));
    }

    @Test
    void tryGetBodyExactly7() {
        Optional<String> s = DslStringUtils.tryGetBodyExactly("(a) (b) ", new Pair<>("(", ")"));
        assertEquals("a", s.orElse(""));
    }
}