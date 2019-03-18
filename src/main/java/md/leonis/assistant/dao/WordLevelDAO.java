package md.leonis.assistant.dao;

import md.leonis.assistant.domain.WordLevel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordLevelDAO extends CrudRepository<WordLevel, String> {

}
