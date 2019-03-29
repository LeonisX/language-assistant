package md.leonis.assistant.dao.user;

import md.leonis.assistant.domain.user.UserWordBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserWordBankDAO extends JpaRepository<UserWordBank, String> {

}
