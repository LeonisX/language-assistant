package md.leonis.assistant.domain.standard;

import lombok.*;
import md.leonis.assistant.domain.LanguageLevel;

import javax.persistence.*;

//TODO study default columns matching, Column w/o name
@Getter
@Setter
@ToString
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
