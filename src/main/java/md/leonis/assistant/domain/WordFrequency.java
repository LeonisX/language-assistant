package md.leonis.assistant.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "WORD_FREQUENCY", schema = "Bank")
public class WordFrequency {

    @Id
    @Column(name = "Word", length = 136, nullable = false)
    private String word;

    @Column(name = "Frequency", nullable = false)
    private long frequency;

}
