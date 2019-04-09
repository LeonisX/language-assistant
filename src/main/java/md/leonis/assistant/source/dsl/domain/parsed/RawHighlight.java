package md.leonis.assistant.source.dsl.domain.parsed;

import lombok.Data;

import java.util.List;

@Data
public class RawHighlight {

    private List<String> expression; //TODO Optional
    //private List<String> region.variants.variant; //TODO Optional
}
