package md.leonis.assistant.source.dsl.dao;

import md.leonis.assistant.source.dsl.domain.parsed.DslRawParsed;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DslRawParsedDAO extends CrudRepository<DslRawParsed, Long> {

}
