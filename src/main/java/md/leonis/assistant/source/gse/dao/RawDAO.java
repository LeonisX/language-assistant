package md.leonis.assistant.source.gse.dao;

import md.leonis.assistant.source.gse.domain.Raw;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RawDAO extends CrudRepository<Raw, Long> {

}
