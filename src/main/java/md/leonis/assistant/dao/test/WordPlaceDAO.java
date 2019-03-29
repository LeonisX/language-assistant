package md.leonis.assistant.dao.test;

import md.leonis.assistant.domain.test.WordPlace;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordPlaceDAO extends CrudRepository<WordPlace, String> {

}
