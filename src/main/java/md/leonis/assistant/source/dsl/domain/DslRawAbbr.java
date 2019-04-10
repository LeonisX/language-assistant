package md.leonis.assistant.source.dsl.domain;

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
@Table(name = "RawAbbr", schema = "Bank")
public class DslRawAbbr {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String word;

    @Column(length = 65535, columnDefinition = "text", nullable = false)
    private String raw;

    public static WordLevel toWordLevel(DslRawAbbr dslRaw) {
        return new WordLevel(dslRaw.word, LanguageLevel.UNK);
    }
}
