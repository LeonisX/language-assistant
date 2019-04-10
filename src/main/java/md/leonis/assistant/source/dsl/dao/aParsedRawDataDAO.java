package md.leonis.assistant.source.dsl.dao;

import md.leonis.assistant.source.dsl.domain.ParsedRawData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface aParsedRawDataDAO extends JpaRepository<ParsedRawData, String> {

    @Query("SELECT raw.expression FROM ParsedRawData raw")
    List<String> findAllWords();
}
