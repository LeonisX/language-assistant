package md.leonis.assistant.utils;

import lombok.Builder;
import md.leonis.assistant.domain.LanguageLevel;

import java.util.Arrays;
import java.util.stream.Collectors;

@Builder
public class CssGenerator {

    private static final String BLACK = "000000";
    private static final String WHITE = "FFFFFF";

    private static final String TEMPLATE = ".%s { color: #%s }";

    private boolean showColors;
    private boolean hideKnownWords;
    private boolean showUnknown;
    private boolean showA1;
    private boolean showA2;
    private boolean showB1;
    private boolean showB2;
    private boolean showC1;
    private boolean showC2;
    private boolean showC2p;

    public String generate() {

        String css = Arrays.stream(LanguageLevel.values()).map(level ->
                String.format(TEMPLATE, level.name().toLowerCase(), getColor(level))).collect(Collectors.joining(" "));

        if (hideKnownWords) {
            css += String.format(TEMPLATE, "known", WHITE);
        }
        css += " .decorated { background-color: #DDDDEE }";
        //css += " .decorated { background-color: #DDDDEE; text-decoration: underline; }";
        return css;
    }

    private String getColor(LanguageLevel level) {
        switch (level) {
            case A0:
                return getColorByLevel(level);
            case A1:
                return showA1 ? getColorByLevel(level) : WHITE;
            case A2:
            case A2P:
                return showA2 ? getColorByLevel(level) : WHITE;
            case B1:
            case B1P:
                return showB1 ? getColorByLevel(level) : WHITE;
            case B2:
            case B2P:
                return showB2 ? getColorByLevel(level) : WHITE;
            case C1:
                return showC1 ? getColorByLevel(level) : WHITE;
            case C2:
                return showC2 ? getColorByLevel(level) : WHITE;
            case C2P:
                return showC2p ? getColorByLevel(level) : WHITE;
            case UNK:
            default:
                return showUnknown ? BLACK : WHITE;
        }
    }

    private String getColorByLevel(LanguageLevel level) {
        if (!showColors) {
            return BLACK;
        }
        switch (level) {
            case A0:
                return "339933"; //RRGGBB
            case A1:
                return "33BB33";
            case A2:
                return "00BB00";
            case A2P:
                return "00BB77";
            case B1:
                return "3333AA";
            case B1P:
                return "3333BB";
            case B2:
                return "0000BB";
            case B2P:
                return "7700BB";
            case C1:
                return "BB3333";
            case C2:
                return "BB0000";
            case C2P:
                return "BB7777";
            case UNK:
            default:
                return BLACK;
        }
    }
}
