package md.leonis.assistant.domain.bank.raw;

import lombok.Data;
import md.leonis.assistant.domain.LanguageLevel;
import md.leonis.assistant.domain.standard.WordLevel;

import java.util.List;

@Data
public class RawData {

    private String itemId;
    private String expression;
    private RawAudioFiles audioFiles;
    private String thesaurus;
    private String definition;
    private String example;
    private String audience;
    private String cefr;
    private String gse;
    private boolean temporaryGse;
    private List<String> grammaticalCategories;
    private List<RawCollos> collos;
    private List<String> variants;
    private List<List<RawTopic>> topics;
    private RawRegion region;
    private RawHighlight highlight; // TODO can't parse :(

    public WordLevel toWordLevel() {
        //TODO last param - definition
        return new WordLevel(expression, mapLevel(), mapPercent(), cefr);
    }

    private Byte mapPercent() {
        if (gse.equals("N/A")) {
            return null;
        }
        return Byte.parseByte(gse);
    }

    private LanguageLevel mapLevel() {
        switch (cefr) {
            case "<A1 (10-21)": return LanguageLevel.A0;
            case "A1 (22-29)": return LanguageLevel.A1;
            case "A1 (22-29)*": return LanguageLevel.A1;
            case "A2 (30-35)": return LanguageLevel.A2;
            case "A2 (30-35)*": return LanguageLevel.A2;
            case "A2+ (36-42)": return LanguageLevel.A2P;
            case "A2+ (36-42)*": return LanguageLevel.A2P;
            case "B1 (43-50)": return LanguageLevel.B1;
            case "B1 (43-50)*": return LanguageLevel.B1;
            case "B1+ (51-58)": return LanguageLevel.B1P;
            case "B1+ (51-58)*": return LanguageLevel.B1P;
            case "B2 (59-66)": return LanguageLevel.B2;
            case "B2 (59-66)*": return LanguageLevel.B2;
            case "B2+ (67-75)": return LanguageLevel.B2P;
            case "B2+ (67-75)*": return LanguageLevel.B2P;
            case "C1 (76-84)": return LanguageLevel.C1;
            case "C1 (76-84)*": return LanguageLevel.C1;
            case "C2 (85-90)": return LanguageLevel.C2;
            case "C2 (85-90)*": return LanguageLevel.C2;
            case "N/A": return LanguageLevel.UNK;
            default: throw new RuntimeException(cefr);
        }
    }
}
