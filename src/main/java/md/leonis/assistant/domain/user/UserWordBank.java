package md.leonis.assistant.domain.user;

import lombok.*;
import md.leonis.assistant.domain.LanguageLevel;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

    @Enumerated(EnumType.STRING)
    private LanguageLevel wordLevel;

    private long smoked;
    private long read;
    private long written;

    private MemorizationLevel status;
    private LocalDateTime repeatTime;
    private int level;

    public UserWordBank(String word, LanguageLevel wordLevel) {
        this.word = word;
        this.wordLevel = wordLevel;
        this.smoked = 0;
        this.read = 0;
        this.written = 0;
        this.status = MemorizationLevel.UNKNOWN;
        this.repeatTime = LocalDateTime.now();
        this.level = (byte) 0;
    }
}
