package md.leonis.assistant.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Video {

    private UUID id;

    private String title;

    private String author;

    private String provider; //YouTube
    private String url; // Full url

    private List<String> categories;
    private Long averageWordLevel;
    private String accent;
    private Byte speechSpeed;
    private Byte soundQuality;

    private Long rating;

    //TODO thumbnail???

    //TODO for tests only
    public Video(String title, String url) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.author = "TODO";
        this.provider = "YouTube";
        this.url = url;
        this.categories = new ArrayList<>();
        this.averageWordLevel = 2000L;
        this.accent = "Indian";
        this.speechSpeed = 2;
        this.soundQuality = 5;
        this.rating = 5L;
    }
}
