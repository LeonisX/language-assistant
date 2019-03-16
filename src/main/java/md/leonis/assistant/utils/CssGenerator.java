package md.leonis.assistant.utils;

import md.leonis.assistant.domain.LanguageLevel;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CssGenerator {

    private static String template = ".%s { color: #%s }";

    public static String generate() {
        String result = Arrays.stream(LanguageLevel.values())
                .map(level -> String.format(template, level.name().toLowerCase(), "CCC")).collect(Collectors.joining(" "));
        return result + String.format(template, "unknown", "CCC");
    }
}
