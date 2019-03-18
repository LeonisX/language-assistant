package md.leonis.assistant.domain.bank.raw;

import lombok.Data;

import java.util.List;

@Data
public class RawRegion {

    private List<RawVariants> variants;
    private String label;
    private String note;

}
