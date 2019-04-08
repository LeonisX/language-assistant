package md.leonis.assistant.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import md.leonis.assistant.domain.User;
import md.leonis.assistant.domain.Video;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ConfigHolder {

    private User user = new User(UUID.randomUUID(), "Leonis", "email", "password", 0L, 3000L, 200L, 30L, 50L, 2L);

    private List<String> wordBank = new ArrayList<>();

    private List<Video> videos = Arrays.asList(
            new Video("Variables in Java", "https://www.youtube.com/watch?v=iJ3Me1RBW8Q"),
            new Video("JavaFX - Switching Scenes Like A Boss! (Part 1)", "https://www.youtube.com/watch?v=RifjriAxbw8")
    );

    private Video currentVideo;

    private int wordsToLearnCount = 20;
}
