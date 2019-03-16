package md.leonis.assistant.utils;

import lombok.Builder;
import md.leonis.assistant.domain.LanguageLevel;;

import java.util.*;
import java.util.stream.Collectors;

//TODO fill the map on the fly, transform on the fly
@Builder
public class HtmlFormatter {

    private static final Random random = new Random();

    private static String TEMPLATE = "<span class='%s'>%s</span>";

    private static final Set<Character> PUNCTUATION = new TreeSet<>(Arrays.asList(',', '.', '!', '?', ';', ':'));

    public String toHtml(String text) {
        //TODO precompile
        String[] words = text.split("(?<=\\b|[^\\p{L}])");

        Map<String, String> wordsMap = Arrays.stream(words).distinct().collect(Collectors.toMap(w -> w, this::toSpan));

        return Arrays.stream(words).map(word -> {
            String tag = wordsMap.get(word);
            return (tag == null) ? word : tag;
        }).collect(Collectors.joining());
    }

    private String toSpan(String word) {
        if (word.equals("\n")) {
            return "<br>\n";
        }
        if (isSeparator(word)) {
            return word;
        }
        return String.format(TEMPLATE, getClass(word), word);
    }

    private String getClass(String word) {
        return getLevel(word) + " " + getKnownStatus(word);
    }

    private static boolean isSeparator(String word) {
        if (word.equals("")) {
            return true;
        }
        return PUNCTUATION.contains(word.charAt(0));
    }

    //TODO get word level (DB)
    private String getLevel(String word) {
        return LanguageLevel.values()[random.nextInt(LanguageLevel.values().length)].name().toLowerCase(); // a1...c2p, unk
    }

    //TODO  (DB)
    private String getKnownStatus(String word) {
        return random.nextBoolean() ? "known" : "unknown";
    }
}
