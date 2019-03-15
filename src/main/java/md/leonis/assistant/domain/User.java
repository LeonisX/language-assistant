package md.leonis.assistant.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;


@Data
@AllArgsConstructor
public class User {

    private UUID id;

    private String name;

    private String email;
    private String password;

    private Long score;

    private Long words;
    private Long readWords;
    private Long testWords;
    private Long writeWords;

    private Long videos;


}
