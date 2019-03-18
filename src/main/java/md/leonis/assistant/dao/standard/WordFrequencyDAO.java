package md.leonis.assistant.dao.standard;

import md.leonis.assistant.domain.standard.WordFrequency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordFrequencyDAO extends CrudRepository<WordFrequency, String> {

}
