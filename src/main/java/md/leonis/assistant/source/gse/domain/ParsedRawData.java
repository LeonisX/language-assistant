package md.leonis.assistant.source.gse.domain;

import lombok.*;
import md.leonis.assistant.domain.LanguageLevel;
import md.leonis.assistant.domain.test.WordLevel;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Builder(toBuilder = true)
@Entity
public class ParsedRawData {

    @Id
    private String itemId;

    private String expression;
    private String audioFiles;
    private String thesaurus;
    private String definition;
    private String example;
    private String audience;
    private String cefr;
    private String gse;
    private boolean temporaryGse;
    private String grammaticalCategories;
    private String collos;
    private String variants;
    private String topics;
    private String region;
    private String highlight;

    public WordLevel toWordLevel() {
        return new WordLevel(null, expression, mapLevel(), mapPercent(), definition);
    }

    private Byte mapPercent() {
        if (gse.equals("N/A")) {
            return null;
        }
        return Byte.parseByte(removeStar(gse));
    }

    private LanguageLevel mapLevel() {
        switch (removeStar(cefr)) {
            case "<A1 (10-21)": return LanguageLevel.A0;
            case "A1 (22-29)": return LanguageLevel.A1;
            case "A2 (30-35)": return LanguageLevel.A2;
            case "A2+ (36-42)": return LanguageLevel.A2P;
            case "B1 (43-50)": return LanguageLevel.B1;
            case "B1+ (51-58)": return LanguageLevel.B1P;
            case "B2 (59-66)": return LanguageLevel.B2;
            case "B2+ (67-75)": return LanguageLevel.B2P;
            case "C1 (76-84)": return LanguageLevel.C1;
            case "C2 (85-90)": return LanguageLevel.C2;
            case "N/A": return LanguageLevel.UNK;
            default: throw new RuntimeException(cefr);
        }
    }

    private String removeStar(String value) {
        return value.replace("*", "");
    }
}
