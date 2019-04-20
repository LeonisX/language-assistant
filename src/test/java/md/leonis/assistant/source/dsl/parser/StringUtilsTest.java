package md.leonis.assistant.source.dsl.parser;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static md.leonis.assistant.source.dsl.parser.M1Parser.ABBR;
import static md.leonis.assistant.source.dsl.parser.M1Parser.FROM;
import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    void getBody() {
        assertEquals("asd", StringUtils.getBody("<p>asd</p>", new Pair<>("<p>", "</p>")));
    }

    @Test
    void getBody2() {
        assertEquals("asd", StringUtils.getBody("<c><p>asd</p></c>", new Pair<>("<p>", "</p>")));
    }

    @Test
    void getBody3() {
        assertEquals("<p>asd</p>", StringUtils.getBody("<c><p>asd</p></c>", new Pair<>("<c>", "</c>")));
    }

    @Test
    void getBodyError() {
        assertThrows(IllegalStateException.class, () -> StringUtils.getBody("<p>asd</p>", new Pair<>("<c>", "</p>")));
    }

    @Test
    void getBodyError2() {
        assertThrows(IllegalStateException.class, () -> StringUtils.getBody("<p>asd</p>", new Pair<>("</p>", "<p>")));
    }

    @Test
    void trimOuterBody() {
        assertEquals("", StringUtils.trimOuterBody("<p>asd</p>", new Pair<>("<p>", "</p>")));
    }

    @Test
    void trimOuterBody2() {
        assertEquals("<c></c>", StringUtils.trimOuterBody("<c><p>asd</p></c>", new Pair<>("<p>", "</p>")));
    }

    @Test
    void trimOuterBody3() {
        assertEquals("", StringUtils.trimOuterBody("<c><p>asd</p></c>", new Pair<>("<c>", "</c>")));
    }

    @Test
    void trimOuterBodyError() {
        assertThrows(IllegalStateException.class, () -> StringUtils.trimOuterBody("<p>asd</p>", new Pair<>("<c>", "</p>")));
    }

    @Test
    void trimOuterBodyError2() {
        assertThrows(IllegalStateException.class, () -> StringUtils.trimOuterBody("<p>asd</p>", new Pair<>("</p>", "<p>")));
    }

    @Test
    void tryGetBodyTriple() {
        assertEquals(Optional.of(new Pair<>(0, 12)), StringUtils.tryGetBody("[p]сокр.[/p]", ABBR));
    }

    @Test
    void tryGetBodyTriple2() {
        assertEquals(Optional.of(new Pair<>(0, 23)), StringUtils.tryGetBody("[p] сокр.[/p] [i]от[/i]", ABBR, FROM));
    }

    @Test
    void tryGetBodyTriple3() {
        assertEquals(Optional.of(new Pair<>(0, 26)), StringUtils.tryGetBody("[p] сокр. [/p]  [i]от [/i]", ABBR, FROM));
    }

    @Test
    void trimOuterBodyTriple() {
        assertEquals("", StringUtils.trim("[p]сокр.[/p]", new Pair<>(0, 12)));
    }

    @Test
    void trimOuterBodyTriple2() {
        assertEquals("", StringUtils.trim("[p] сокр.[/p] [i]от[/i]", new Pair<>(0, 23)));
    }

    @Test
    void trimOuterBodyTriple3() {
        assertEquals(":", StringUtils.trim("[p] сокр. [/p]  [i]от [/i]:", new Pair<>(0, 26)));
    }

    // public static final Triple ABBR = new Triple("[p]", "сокр.", "[/p]");
    //    public static final Triple FROM = new Triple("[i]", "от", "[/i]");
}