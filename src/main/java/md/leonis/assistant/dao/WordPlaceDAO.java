package md.leonis.assistant.dao;

import md.leonis.assistant.domain.WordPlace;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordPlaceDAO extends CrudRepository<WordPlace, Long> {

}
