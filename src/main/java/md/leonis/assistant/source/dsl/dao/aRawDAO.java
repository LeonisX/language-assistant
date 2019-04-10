package md.leonis.assistant.source.dsl.dao;

import md.leonis.assistant.source.dsl.domain.Raw;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface aRawDAO extends CrudRepository<Raw, Long> {

}
