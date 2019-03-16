package md.leonis.assistant.utils;

import java.util.*;
import java.util.stream.Collectors;

//TODO service
//TODO fill the map on the fly, transform on the fly
public class HtmlUtils {

    private static String TEMPLATE = "<span class='%s'>%s</span>";

    private static final Set<Character> PUNCTUATION = new TreeSet<>(Arrays.asList(',','.','!','?',';',':'));

    public static String toHtml(String text) {
        String[] words = text.split("(?<=\\b|[^\\p{L}])");

        Map<String, String> wordsMap = Arrays.stream(words).distinct().collect(Collectors.toMap(w -> w, HtmlUtils::toSpan));

        return Arrays.stream(words).map(word -> {
            String tag = wordsMap.get(word);
            return (tag == null) ? word : tag;
        }).collect(Collectors.joining());
    }

    private static String toSpan(String word) {
        return String.format(TEMPLATE, getLevel(word), word);
    }

    private static String getLevel(String word) {
        if (word.equals("\n")) {
            return "<br>\n";
        }
        if (isSeparator(word)) {
            return word;
        }
        //TODO get word level (DB)
        return "a1 unknown";
    }

    private static boolean isSeparator(String word) {
        if (word.equals("")) {
            return true;
        }
        return PUNCTUATION.contains(word.charAt(0));
    }
}
