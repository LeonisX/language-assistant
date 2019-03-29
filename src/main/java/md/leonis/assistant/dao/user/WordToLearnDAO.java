package md.leonis.assistant.dao.user;

import md.leonis.assistant.domain.user.WordToLearn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordToLearnDAO extends JpaRepository<WordToLearn, String> {

}
