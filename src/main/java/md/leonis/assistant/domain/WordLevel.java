package md.leonis.assistant.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

//TODO study default columns matching, Column w/o name
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "WordLevels", schema = "Bank")
public class WordLevel {

    @Id
    @Column(name = "Word", length = 136, nullable = false)
    private String word;

    @Enumerated(EnumType.STRING)
    @Column(name = "Level", length = 3, nullable = false)
    private LanguageLevel level;

    @Column(name = "Percent")
    private byte percent;

}
