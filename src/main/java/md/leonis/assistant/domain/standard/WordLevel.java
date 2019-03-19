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
@Table(name = "WordLevels", schema = "Test")
public class WordLevel {

    @Id
    @Column(name = "Word", length = 136, nullable = false)
    private String word;

    @Enumerated(EnumType.STRING)
    @Column(name = "Level", length = 3, nullable = false)
    private LanguageLevel level;

    @Column(name = "Percent")
    private byte percent;

    @Column(name = "Definition")
    private byte definition;

}
