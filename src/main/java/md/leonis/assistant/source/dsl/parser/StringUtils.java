package md.leonis.assistant.source.dsl.parser;

import javafx.util.Pair;
import md.leonis.assistant.source.dsl.parser.domain.Triple;

import java.util.Optional;

public class StringUtils {

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

    public static String formatOuterBody(String body, Pair<String, String> pair) {
        return pair.getKey() + body + pair.getValue();
    }

    public static Optional<Pair<Integer, Integer>> tryGetBody(String line, final Triple... triples) {
        int startIndex = 0;
        int endIndex = 0;
        Optional<Pair<Integer, Integer>> pair = Optional.empty();
        for (final Triple triple : triples) {
            pair = tryGetStartTriple(line, startIndex, triple);
            if (!pair.isPresent()) {
                return Optional.empty();
            }
            Pair<Integer, Integer> startPair = pair.get();
            endIndex = startPair.getValue() + triple.getValue().length();
            startIndex = startPair.getValue() + triple.getValue().length();
        }
        int index = endIndex;
        return pair.map(p -> new Pair<>(0, index));
    }

    private static Optional<Pair<Integer, Integer>> tryGetStartTriple(String line, int startIndex, Triple triple) {
        int keyStart = line.indexOf(triple.getKey(), startIndex);
        int valueStart = line.indexOf(triple.getValue(), startIndex);
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
        String body = line.substring(keyStart + triple.getKey().length(), valueStart);
        if (!body.trim().equals(triple.getBody())) {
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

    public static boolean startsWith(String string, String substr) {
        return (string.indexOf(substr) == 0);
    }

    // Apache Commons Lang
    public static String removeStart(final String str, final String remove) {
        if (str.isEmpty() || remove.isEmpty()) {
            return str;
        }
        if (str.startsWith(remove)){
            return str.substring(remove.length());
        }
        return str;
    }


    private int read(String line, String chunk) {
        return read(line, 0, chunk);
    }

    private int read(String line, int startPos, String chunk) {
        // TODO
        return -1;
    }
}
