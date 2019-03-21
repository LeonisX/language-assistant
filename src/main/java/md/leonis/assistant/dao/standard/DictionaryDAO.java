package md.leonis.assistant.dao.standard;

import md.leonis.assistant.domain.standard.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryDAO extends JpaRepository<Dictionary, Long> {

}
