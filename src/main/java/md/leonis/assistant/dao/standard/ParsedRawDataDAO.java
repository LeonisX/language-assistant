package md.leonis.assistant.dao.standard;

import md.leonis.assistant.domain.standard.ParsedRawData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParsedRawDataDAO extends CrudRepository<ParsedRawData, String> {

    @Query("SELECT raw.expression FROM ParsedRawData raw")
    List<String> findAllWords();
}
