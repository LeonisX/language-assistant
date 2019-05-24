package md.leonis.assistant.source.dsl.parser.domain;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class DetailContainer extends ArrayList<Detail> {

    @Override
    public String toString() {
        return this.stream().map(Detail::toString).collect(Collectors.joining(" "));
    }
}
