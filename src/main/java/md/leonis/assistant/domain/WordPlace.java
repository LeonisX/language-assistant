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
@Table(name = "WORD_PLACE", schema = "Bank")
public class WordPlace {

    @Id
    @Column(name = "Word", length = 136, nullable = false)
    private String word;

    @Column(name = "Place", nullable = false)
    private long place;

}
