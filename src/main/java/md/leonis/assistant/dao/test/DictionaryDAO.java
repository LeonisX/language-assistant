package md.leonis.assistant.dao.test;

import md.leonis.assistant.domain.test.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryDAO extends JpaRepository<Dictionary, Long> {

}
