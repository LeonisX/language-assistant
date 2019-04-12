package md.leonis.assistant.source.dsl.parser;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;

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
}