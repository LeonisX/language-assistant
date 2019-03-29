package md.leonis.assistant.domain.bank.raw;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import md.leonis.assistant.domain.LanguageLevel;
import md.leonis.assistant.domain.test.ParsedRawData;
import md.leonis.assistant.domain.test.WordLevel;

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

    public ParsedRawData toParsedRawData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return ParsedRawData.builder()
                    .itemId(itemId)
                    .expression(expression)
                    .audioFiles(objectMapper.writeValueAsString(audioFiles))
                    .thesaurus(thesaurus)
                    .definition(definition)
                    .example(example)
                    .audience(audience)
                    .cefr(cefr)
                    .gse(gse)
                    .temporaryGse(temporaryGse)
                    .grammaticalCategories(objectMapper.writeValueAsString(grammaticalCategories))
                    .collos(objectMapper.writeValueAsString(collos))
                    .variants(objectMapper.writeValueAsString(variants))
                    .topics(objectMapper.writeValueAsString(topics))
                    .region(objectMapper.writeValueAsString(region))
                    .highlight(objectMapper.writeValueAsString(highlight))
                    .build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

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
            case "<A1 (10-21)":
                return LanguageLevel.A0;
            case "A1 (22-29)":
                return LanguageLevel.A1;
            case "A2 (30-35)":
                return LanguageLevel.A2;
            case "A2+ (36-42)":
                return LanguageLevel.A2P;
            case "B1 (43-50)":
                return LanguageLevel.B1;
            case "B1+ (51-58)":
                return LanguageLevel.B1P;
            case "B2 (59-66)":
                return LanguageLevel.B2;
            case "B2+ (67-75)":
                return LanguageLevel.B2P;
            case "C1 (76-84)":
                return LanguageLevel.C1;
            case "C2 (85-90)":
                return LanguageLevel.C2;
            case "N/A":
                return LanguageLevel.UNK;
            default:
                throw new RuntimeException(cefr);
        }
    }

    private String removeStar(String value) {
        return value.replace("*", "");
    }
}
