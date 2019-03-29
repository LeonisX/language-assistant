package md.leonis.assistant.source.gse.domain.parsed;

import lombok.Data;

import java.util.List;

@Data
public class RawContainer {

    private int count;
    private List<RawData> data;

}
