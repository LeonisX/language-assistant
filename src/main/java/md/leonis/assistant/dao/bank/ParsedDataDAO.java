package md.leonis.assistant.dao.bank;

import md.leonis.assistant.domain.bank.ParsedData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParsedDataDAO extends JpaRepository<ParsedData, String> {

    @Query("SELECT data.expression FROM ParsedData data")
    List<String> findAllWords();
}
