package md.leonis.assistant.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Raws", schema = "Bank")
public class Raw {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Long id;

    @Column(name = "Raw", length = 65535, columnDefinition = "text", nullable = false)
    private String raw;

    public Raw(String raw) {
        this.raw = raw;
    }
}
