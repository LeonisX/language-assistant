package md.leonis.assistant.dao.standard;

import md.leonis.assistant.domain.standard.Dictionary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryDAO extends CrudRepository<Dictionary, Long> {

}
