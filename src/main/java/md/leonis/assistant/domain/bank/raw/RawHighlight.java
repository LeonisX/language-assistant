package md.leonis.assistant.domain.bank.raw;

import lombok.Data;

import java.util.List;

@Data
public class RawHighlight {

    private List<String> expression; //TODO Optional
    //private List<String> region.variants.variant; //TODO Optional
}
