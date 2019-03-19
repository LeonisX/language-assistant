package md.leonis.assistant.domain.standard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import md.leonis.assistant.domain.LanguageLevel;

import javax.persistence.*;

//TODO study default columns matching, Column w/o name
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WordLevel {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String word;

    @Enumerated(EnumType.STRING)
    private LanguageLevel level;

    private Byte percent;

    private String definition;

}
