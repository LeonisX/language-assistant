package md.leonis.assistant.domain.user;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
//TODO add different meanings of the one word
public class UserWordBank {

    @Id
    private String word;

    private long smoked;
    private long read;
    private long written;

    private MemorizationLevel status;
    private LocalDateTime repeatTime;
    private int level;

}
