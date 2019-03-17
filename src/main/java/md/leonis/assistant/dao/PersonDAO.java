package md.leonis.assistant.dao;

import java.util.Date;
import java.util.List;

import md.leonis.assistant.domain.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//TODO test; delete
@Repository
public interface PersonDAO extends CrudRepository<Person, Long> {

    List<Person> findByFullNameLike(String name);

    List<Person> findByDateOfBirthGreaterThan(Date date);

}
