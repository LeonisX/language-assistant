package md.leonis.assistant.source.dsl.parser;

import javafx.util.Pair;

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
        return tryGetStartPair(line, pair).map(startPair -> line.substring(startPair.getKey() + pair.getKey().length(), startPair.getValue()));
    }

    private static Optional<Pair<Integer, Integer>> tryGetStartPair(String line, Pair<String, String> pair) {
        int keyStart = line.indexOf(pair.getKey());
        int valueStart = line.indexOf(pair.getValue());
        if (keyStart == -1 || valueStart == -1 || keyStart >= valueStart) {
            return Optional.empty();
        }
        return Optional.of(new Pair<>(keyStart, valueStart));
    }

    private int read(String line, String chunk) {
        return read(line, 0, chunk);
    }

    private int read(String line, int startPos, String chunk) {
        // TODO
        return -1;
    }
}
