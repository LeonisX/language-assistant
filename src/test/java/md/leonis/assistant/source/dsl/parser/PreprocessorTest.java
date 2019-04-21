package md.leonis.assistant.source.dsl.parser;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PreprocessorTest {


    public static String normalize(String string) {
        string = string.replace(",", ", ");
        string = string.replace(";", "; ");
        string = string.replace("] ", "]");
        string = string.replace(" [/", "[/");
        string = string.replace("  ", " ");
        string = string.replace("  ", " ");
        string = StringUtils.stripEnd(string, " ");
        return string;
    }

    @Test
    void normalize() {
        assertEquals("[a][/a]", normalize("[a][/a]  "));
    }

    @Test
    void normalize2() {
        assertEquals("[a]v[/a]", normalize("[a]v[/a]"));
    }

    @Test
    void normalize3() {
        assertEquals("[a],[/a]", normalize("[a],[/a]"));
    }

    @Test
    void normalize4() {
        assertEquals("[a][/a][a][/a]", normalize("[a][/a] [a][/a]"));
    }
}