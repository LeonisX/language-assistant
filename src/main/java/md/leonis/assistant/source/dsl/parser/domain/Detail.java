package md.leonis.assistant.source.dsl.parser.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class Detail {

    private List<Tag> tags = new ArrayList<>();
    private List<Link> links = new ArrayList<>();

    @Override
    public String toString() {
        String result = tags.isEmpty() ? "" : tags.stream().map(Tag::toString).collect(Collectors.joining(" "));
        if (links.isEmpty()) {
            return result;
        }
        return result + Link.renderLinks(links);
    }
}
