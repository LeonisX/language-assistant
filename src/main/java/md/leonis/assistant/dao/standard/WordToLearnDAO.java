package md.leonis.assistant.dao.standard;

import md.leonis.assistant.domain.standard.WordToLearn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordToLearnDAO extends JpaRepository<WordToLearn, String> {

}
