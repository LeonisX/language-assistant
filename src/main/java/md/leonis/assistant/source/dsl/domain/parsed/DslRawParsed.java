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
@Table(name = "RawParsed", schema = "Bank")
//TODO
public class DslRawParsed {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String word;

    private String transcription;

    @OneToMany(targetEntity=DslGroup.class, mappedBy="dslRawParsed", fetch=FetchType.EAGER)
    private List<DslGroup> groups = new ArrayList<>();
}
