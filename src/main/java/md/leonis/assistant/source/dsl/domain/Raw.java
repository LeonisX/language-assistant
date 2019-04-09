package md.leonis.assistant.source.dsl.domain;

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
@Table(name = "Raws", schema = "Bank")
public class Raw {

    @Id
    @Column(name = "Id", nullable = false)
    private Long id;

    @Column(name = "Raw", length = 65535, columnDefinition = "text", nullable = false)
    private String raw;
}
