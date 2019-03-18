package md.leonis.assistant.dao;

import md.leonis.assistant.domain.Raw;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RawDAO extends CrudRepository<Raw, Long> {

}
