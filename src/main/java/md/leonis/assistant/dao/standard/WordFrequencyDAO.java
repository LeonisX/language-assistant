package md.leonis.assistant.dao.standard;

import md.leonis.assistant.domain.standard.WordFrequency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordFrequencyDAO extends CrudRepository<WordFrequency, String> {

    List<WordFrequency> findByWord(String word);

    List<WordFrequency> findByWordContainingIgnoreCase(String word);
}
