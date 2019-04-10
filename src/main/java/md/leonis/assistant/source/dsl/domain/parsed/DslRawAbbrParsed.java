package md.leonis.assistant.source.dsl.domain.parsed;

import lombok.*;
import md.leonis.assistant.domain.LanguageLevel;
import md.leonis.assistant.domain.test.WordLevel;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "RawAbbrParsed", schema = "Bank")
public class DslRawAbbrParsed {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String word;

    private String raw;
}
