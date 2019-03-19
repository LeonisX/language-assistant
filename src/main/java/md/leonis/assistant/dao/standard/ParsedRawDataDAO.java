package md.leonis.assistant.dao.standard;

import md.leonis.assistant.domain.standard.ParsedRawData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParsedRawDataDAO extends CrudRepository<ParsedRawData, String> {

}
