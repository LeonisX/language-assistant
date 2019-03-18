package md.leonis.assistant.dao.standard;

import md.leonis.assistant.domain.standard.WordLevel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordLevelDAO extends CrudRepository<WordLevel, String> {

}
