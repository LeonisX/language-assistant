package md.leonis.assistant.dao.standard;

import md.leonis.assistant.domain.standard.Variance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VarianceDAO extends CrudRepository<Variance, String> {

    //TODO optimize - store in one case
    Optional<Variance> findByVarianceEqualsIgnoreCase(String variance);
}
