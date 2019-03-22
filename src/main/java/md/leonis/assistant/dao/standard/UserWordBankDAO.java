package md.leonis.assistant.dao.standard;

import md.leonis.assistant.domain.standard.UserWordBank;
import md.leonis.assistant.domain.standard.WordFrequency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserWordBankDAO extends JpaRepository<UserWordBank, String> {

}
