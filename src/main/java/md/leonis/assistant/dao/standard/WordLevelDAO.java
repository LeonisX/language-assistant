package md.leonis.assistant.dao.standard;

import md.leonis.assistant.domain.standard.WordLevel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordLevelDAO extends CrudRepository<WordLevel, Long> {

    List<WordLevel> findByWord(String word);

    List<WordLevel> findByWordContainingIgnoreCase(String word);
}
