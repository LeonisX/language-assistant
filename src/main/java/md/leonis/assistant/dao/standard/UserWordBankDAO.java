package md.leonis.assistant.dao.standard;

import md.leonis.assistant.domain.standard.UserWordBank;
import md.leonis.assistant.domain.standard.WordFrequency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserWordBankDAO extends CrudRepository<UserWordBank, String> {

}
