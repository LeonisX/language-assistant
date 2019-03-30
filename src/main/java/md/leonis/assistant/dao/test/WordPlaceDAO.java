package md.leonis.assistant.dao.test;

import md.leonis.assistant.domain.test.WordPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordPlaceDAO extends JpaRepository<WordPlace, String> {

}
