package md.leonis.assistant.domain.bank.raw;

import lombok.Data;

import java.util.List;

@Data
public class RawData {

    private String itemId;
    private String expression;
    private List<RawAudioFiles> audioFiles;
    private String thesaurus;
    private String definition;
    private String example;
    private String audience;
    private String cefr;
    private String gse;
    private boolean temporaryGse;
    private List<String> grammaticalCategories;
    private List<RawCollos> collos;
    private List<String> variants;              // ???
    private List<List<RawTopic>> topics;
    private List<RawRegion> region;
    private RawHighlight highlight; // TODO Optional, Object

}
