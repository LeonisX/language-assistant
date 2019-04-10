package md.leonis.assistant.source.dsl.domain.parsed;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Group", schema = "Bank")
//TODO
public class DslGroup {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long groupId;

    @OneToMany(targetEntity=DslPartOfSpeech.class, mappedBy="dslGroup", fetch=FetchType.EAGER)
    private List<DslPartOfSpeech> partOfSpeechList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="id")
    private DslRawParsed dslRawParsed;
}
