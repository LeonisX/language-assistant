package md.leonis.assistant.source.dsl.dao;

import md.leonis.assistant.source.dsl.domain.parsed.DslRawAbbrParsed;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DslRawAbbrParsedDAO extends CrudRepository<DslRawAbbrParsed, Long> {

}
