package md.leonis.assistant.source.dsl.dao;

import md.leonis.assistant.source.dsl.domain.DslRawAbbr;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DslRawAbbrDAO extends CrudRepository<DslRawAbbr, Long> {

}
