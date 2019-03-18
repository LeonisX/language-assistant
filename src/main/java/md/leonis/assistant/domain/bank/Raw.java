package md.leonis.assistant.domain.bank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Raws", schema = "Bank")
public class Raw {

    @Id
    @Column(name = "Id", nullable = false)
    private Long id;

    @Column(name = "Raw", length = 65535, columnDefinition = "text", nullable = false)
    private String raw;
}
