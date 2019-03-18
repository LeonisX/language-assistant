package md.leonis.assistant.domain.standard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "WordFrequencies", schema = "Test")
public class WordFrequency {

    @Id
    @Column(name = "Word", length = 136, nullable = false)
    private String word;

    @Column(name = "Frequency", nullable = false)
    private long frequency;

}
