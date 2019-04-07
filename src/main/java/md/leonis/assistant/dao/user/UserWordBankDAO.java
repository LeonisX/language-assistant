package md.leonis.assistant.dao.user;

import md.leonis.assistant.domain.LanguageLevel;
import md.leonis.assistant.domain.user.UserWordBank;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserWordBankDAO extends JpaRepository<UserWordBank, String> {

    List<UserWordBank> findByLevelOrderByRepeatTimeAsc(int level);

    List<UserWordBank> findByLevel(int level, Pageable page);

    List<UserWordBank> findByLevelGreaterThan(int level);

    List<UserWordBank> findByLevelGreaterThanAndWordLevelIn(int level, Set<LanguageLevel> languageLevels);

    List<UserWordBank> findByLevelGreaterThan(int level, Pageable page);

    List<UserWordBank> findByLevelGreaterThanAndWordLevelIn(int level, Set<LanguageLevel> languageLevels, Pageable page);

    Long countByLevelGreaterThan(int level);

}
