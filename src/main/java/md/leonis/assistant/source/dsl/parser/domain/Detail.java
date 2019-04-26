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
    private List<Tag> tags2 = new ArrayList<>();

    public void addNewTag(Tag tag) {
        if (!links.isEmpty() && links.get(links.size() - 1).getWord() != null) {
            tags2.add(tag);
        } else {
            tags.add(tag);
        }
    }

    @Override
    public String toString() {
        String renderedTags = tags.stream().map(Tag::toString).collect(Collectors.joining(" "));
        String renderedLinks = Link.renderLinks(links);
        String renderedTags2 = tags2.stream().map(Tag::toString).collect(Collectors.joining(" "));

        String result = tags.isEmpty() ? "" : renderedTags;
        result = links.isEmpty()? result: (result + renderedLinks).trim();
        result = tags2.isEmpty()? result: result + " " + renderedTags2;

        return result;
    }
}
