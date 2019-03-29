package md.leonis.assistant.dao.test;

import md.leonis.assistant.domain.test.WordLevel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordLevelDAO extends CrudRepository<WordLevel, Long> {

    List<WordLevel> findByWord(String word);

    List<WordLevel> findByWordContainingIgnoreCase(String word);
}
