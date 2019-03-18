package md.leonis.assistant.dao.standard;

import md.leonis.assistant.domain.standard.WordPlace;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordPlaceDAO extends CrudRepository<WordPlace, String> {

}
