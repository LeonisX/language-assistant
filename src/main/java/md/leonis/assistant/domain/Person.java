package md.leonis.assistant.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import javax.persistence.*;

//TODO test; delete
@Getter
@Setter
@Entity
@Table(name = "PERSONS")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Long id;

    @Column(name = "Full_Name", length = 64, nullable = false)
    private String fullName;

    @Temporal(TemporalType.DATE)
    @Column(name = "Date_Of_Birth", nullable = false)
    private Date dateOfBirth;

}
