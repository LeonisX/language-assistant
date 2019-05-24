package md.leonis.assistant.source.dsl.parser;

import javafx.util.Pair;
import md.leonis.assistant.source.dsl.parser.domain.Tag;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DslStringUtils {

    public static String getBody(String line, Pair<String, String> pair) {
        Pair<Integer, Integer> startPair = getStartPair(line, pair);
        return line.substring(startPair.getKey() + pair.getKey().length(), startPair.getValue());
    }

    public static String trimOuterBody(String line, Pair<String, String> pair) {
        Pair<Integer, Integer> startPair = getStartPair(line, pair);
        return line.substring(0, startPair.getKey()) + line.substring(startPair.getValue() + pair.getValue().length());
    }

    private static Pair<Integer, Integer> getStartPair(String line, Pair<String, String> pair) {
        int keyStart = line.indexOf(pair.getKey());
        int valueStart = line.indexOf(pair.getValue());
        if (keyStart == -1 || valueStart == -1 || keyStart >= valueStart) {
            throw new IllegalStateException(line);
        }
        return new Pair<>(keyStart, valueStart);
    }

    public static String trimOuterBodyExactly(String line, Pair<String, String> pair) {
        Pair<Integer, Integer> startPair = tryGetStartPairExactly(line, pair).get();
        return line.substring(0, startPair.getKey()) + line.substring(startPair.getValue() + pair.getValue().length());
    }

    private static Pair<Integer, Integer> getStartPairExactly(String line, Pair<String, String> pair) {
        int keyStart = line.indexOf(pair.getKey());
        int valueStart = line.lastIndexOf(pair.getValue());
        if (keyStart == -1 || valueStart == -1 || keyStart >= valueStart) {
            throw new IllegalStateException(line);
        }
        return new Pair<>(keyStart, valueStart);
    }

    public static Optional<String> tryGetBody(String line, Pair<String, String> pair) {
        return tryGetStartPair(line, pair).map(startPair -> line.substring(pair.getKey().length(), startPair.getValue()));
    }

    private static Optional<Pair<Integer, Integer>> tryGetStartPair(String line, Pair<String, String> pair) {
        int keyStart = line.indexOf(pair.getKey());
        int valueStart = line.indexOf(pair.getValue());
        if (keyStart == -1 || valueStart == -1 || keyStart >= valueStart) {
            return Optional.empty();
        }
        if (keyStart != 0) {
            return Optional.empty();
        }
        return Optional.of(new Pair<>(keyStart, valueStart));
    }

    public static Optional<String> tryGetBodyExactly(String line, Pair<String, String> pair) {
        return tryGetStartPairExactly(line, pair).map(startPair -> line.substring(pair.getKey().length(), startPair.getValue()));
    }

    private static Optional<Pair<Integer, Integer>> tryGetStartPairExactly(String line, Pair<String, String> pair) {
        Map<Integer, String> indexes = toIndexesMap(line, pair);
        int opened = 0;
        int valueStart = -1;

        for (Integer i: indexes.keySet()) {
            if (indexes.get(i).equals(pair.getKey())) {
                opened++;
            }
            if (indexes.get(i).equals(pair.getValue())) {
                valueStart = i;
            }
            if (opened > 1) {
                break;
            }
        }

        int keyStart = line.indexOf(pair.getKey());
        if (keyStart == -1 || valueStart == -1 || keyStart >= valueStart) {
            return Optional.empty();
        }
        if (keyStart != 0) {
            return Optional.empty();
        }
        return Optional.of(new Pair<>(keyStart, valueStart));
    }

    static Map<Integer, String> toIndexesMap(String line, Pair<String, String> pair) {
        Map<Integer, String> indexes = new HashMap<>();
        int index = 0;
        char prevChar = 0;
        while (!line.isEmpty()) {
            if (line.startsWith(pair.getKey())) {
                indexes.put(index, pair.getKey());
                index += pair.getKey().length();
                prevChar = pair.getKey().charAt(pair.getKey().length() - 1);
                line = line.substring(pair.getKey().length());
            } else if (line.startsWith(pair.getValue()) && prevChar != '1') {
                indexes.put(index, pair.getValue());
                index += pair.getValue().length();
                prevChar = pair.getValue().charAt(pair.getValue().length() - 1);
                line = line.substring(pair.getValue().length());
            } else {
                index++;
                prevChar = line.charAt(0);
                line = line.substring(1);
            }
        }
        return indexes;
    }

    public static String formatOuterBody(String body, Pair<String, String> pair) {
        return pair.getKey() + body + pair.getValue();
    }

    public static Optional<Pair<Integer, Integer>> tryGetBody(String line, final Tag... tags) {
        int startIndex = 0;
        int endIndex = 0;
        Optional<Pair<Integer, Integer>> pair = Optional.empty();
        for (final Tag tag : tags) {
            pair = tryGetStartTag(line, startIndex, tag);
            if (!pair.isPresent()) {
                return Optional.empty();
            }
            Pair<Integer, Integer> startPair = pair.get();
            endIndex = startPair.getValue() + tag.getType().getClosing().length();
            startIndex = startPair.getValue() + tag.getType().getClosing().length();
        }
        int index = endIndex;
        return pair.map(p -> new Pair<>(0, index));
    }

    private static Optional<Pair<Integer, Integer>> tryGetStartTag(String line, int startIndex, Tag tag) {
        int keyStart = line.indexOf(tag.getType().getOpening(), startIndex);
        int valueStart = line.indexOf(tag.getType().getClosing(), startIndex);
        if (keyStart == -1 || valueStart == -1 || keyStart >= valueStart) {
            return Optional.empty();
        }
        if (keyStart != startIndex) {
            // skip spaces
            String substr = line.substring(startIndex, keyStart);
            if (!substr.trim().isEmpty()) {
                return Optional.empty();
            }
        }
        String body = line.substring(keyStart + tag.getType().getOpening().length(), valueStart);
        if (!body.trim().equals(tag.getWord())) {
            return Optional.empty();
        }
        return Optional.of(new Pair<>(keyStart, valueStart));
    }

    public static String trim(String line, Pair<Integer, Integer> startPair) {
        return line.substring(startPair.getValue());
    }

    public static String compact(String string) {
        return string.replace(" ", "").replace(",[/c]", "[/c]");
    }

    private int read(String line, String chunk) {
        return read(line, 0, chunk);
    }

    private int read(String line, int startPos, String chunk) {
        // TODO
        return -1;
    }
}
