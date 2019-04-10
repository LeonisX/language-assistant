package md.leonis.assistant.source.dsl.dao;

import md.leonis.assistant.source.dsl.domain.DslRaw;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DslRawDAO extends CrudRepository<DslRaw, Long> {

}
