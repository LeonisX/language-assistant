package md.leonis.assistant.domain.standard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
//TODO add different meanings of the one word
public class UserWordBank {

    @Id
    private String word;

    //TODO need boolean???? we can read long values
    private boolean smoke;
    private boolean reading;
    private boolean writing;

    private long smoked;
    private long read;
    private long written;

}
