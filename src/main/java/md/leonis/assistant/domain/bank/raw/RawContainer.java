package md.leonis.assistant.domain.bank.raw;

import lombok.Data;

import java.util.List;

@Data
public class RawContainer {

    private int count;
    private List<RawData> data;

}
