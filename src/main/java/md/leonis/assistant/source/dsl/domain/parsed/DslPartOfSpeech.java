package md.leonis.assistant.source.dsl.domain.parsed;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PartOfSpeech", schema = "Bank")
//TODO
public class DslPartOfSpeech {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String word;

    @ManyToOne
    @JoinColumn(name="groupId")
    private DslGroup dslGroup;
}
