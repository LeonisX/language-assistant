package md.leonis.assistant.utils;

import lombok.Getter;
import md.leonis.assistant.domain.LanguageLevel;
import md.leonis.assistant.domain.ScriptWord;
import md.leonis.assistant.service.TestService;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Getter
public class HtmlFormatter {

    private static final Random random = new Random();

    private static final String TEMPLATE = "<span class='%s'>%s</span>";

    private static final Pattern PATTERN = Pattern.compile("(?<=\\b|[^\\p{L}])");

    private static final Set<Character> PUNCTUATION = new TreeSet<>(Arrays.asList(',', '.', '!', '?', ';', ':'));

    private final String text;
    private final String html;
    private final TestService testService;

    private final Map<String, ScriptWord> wordsMap = new HashMap<>();
    private final Set<LanguageLevel> levels;

    public HtmlFormatter(String text, TestService testService, Set<LanguageLevel> levels) {
        this.text = text;
        this.testService = testService;
        this.html = toHtml(text);
        this.levels = levels;
    }

    private String toHtml(String text) {
        String[] words = PATTERN.split(text);

        for (String word : words) {
            ScriptWord scriptWord = wordsMap.get(word);
            if (scriptWord == null) {
                LanguageLevel level = getLevel(word);
                wordsMap.put(word, new ScriptWord(word, level, toSpan(word, level)));
            } else {
                scriptWord.increment();
            }
        }

        return Arrays.stream(words).map(word -> wordsMap.get(word).getTag()).collect(Collectors.joining());
    }

    private String toSpan(String word, LanguageLevel level) {
        if (word.equals("\n")) {
            return "<br>\n";
        }
        if (isSeparator(word)) {
            return word;
        }
        String clazz = getLevelName(level) + " " + getKnownStatus(word);
        return String.format(TEMPLATE, clazz, word);
    }

    private static boolean isSeparator(String word) {
        if (word.equals("")) {
            return true;
        }
        return PUNCTUATION.contains(word.charAt(0));
    }

    private String getLevelName(LanguageLevel level) {
        return level.name().toLowerCase(); // a1...c2p, unk
    }

    private LanguageLevel getLevel(String word) {
        return testService.getLevel(word);
        //return LanguageLevel.values()[random.nextInt(LanguageLevel.values().length - 1) + 1];
    }

    private String getKnownStatus(String word) {
        return testService.getKnownStatus(word) ? "known" : "unknown";
        //return random.nextBoolean() ? "known" : "unknown";
    }
}
