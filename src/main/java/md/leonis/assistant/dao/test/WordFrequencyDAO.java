package md.leonis.assistant.dao.test;

import md.leonis.assistant.domain.test.WordFrequency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordFrequencyDAO extends JpaRepository<WordFrequency, String> {

    List<WordFrequency> findByWord(String word);

    List<WordFrequency> findByWordContainingIgnoreCase(String word);
}
