package md.leonis.assistant.dao.user;

import md.leonis.assistant.domain.user.UserWordBank;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserWordBankDAO extends JpaRepository<UserWordBank, String> {

    List<UserWordBank> findByLevelOrderByRepeatTimeAsc(int level);

    List<UserWordBank> findByLevel(int level, Pageable page);

    List<UserWordBank> findByLevelGreaterThan(int level, Pageable page);
}
