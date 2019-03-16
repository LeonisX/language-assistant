package md.leonis.assistant.utils;

import md.leonis.assistant.domain.LanguageLevel;

import java.util.Arrays;
import java.util.stream.Collectors;

//TODO builder
public class CssGenerator {

    private static final String BLACK = "000000";
    private static final String WHITE = "FFFFFF";

    private static final String TEMPLATE = ".%s { color: #%s }";

    public static String generate(boolean showColors, boolean hideKnownWords, boolean showUnknown,
                                  boolean showA1,
                                  boolean showA2,
                                  boolean showB1,
                                  boolean showB2,
                                  boolean showC1,
                                  boolean showC2,
                                  boolean showC2p
    ) {

        String result = Arrays.stream(LanguageLevel.values())
                .map(level -> {

                    //TODO separate method getColor
                    String color = getColorByLevel(level, showColors);

                    switch (level) {
                        case A0:
                            break;
                        case A1:
                            color = showA1 ? color : WHITE;
                            break;
                        case A2:
                        case A2P:
                            color = showA2 ? color : WHITE;
                            break;
                        case B1:
                        case B1P:
                            color = showB1 ? color : WHITE;
                            break;
                        case B2:
                        case B2P:
                            color = showB2 ? color : WHITE;
                            break;
                        case C1:
                            color = showC1 ? color : WHITE;
                            break;
                        case C2:
                            color = showC2 ? color : WHITE;
                            break;
                        case C2P:
                            color = showC2p ? color : WHITE;
                            break;
                        case UNK:
                        default:
                            color = showUnknown ? BLACK : WHITE;
                    }

                    return String.format(TEMPLATE, level.name().toLowerCase(), color);
                }).collect(Collectors.joining(" "));

        if (hideKnownWords) {
            result += String.format(TEMPLATE, "known", WHITE);
        }
        return result;
    }

    private static String getColorByLevel(LanguageLevel level, boolean showColors) {
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
