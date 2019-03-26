package md.leonis.assistant.dao.standard;

import md.leonis.assistant.domain.standard.Variance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VarianceDAO extends CrudRepository<Variance, Long> {

    //TODO optimize - store in one case
    List<Variance> findByVarianceEqualsIgnoreCase(String variance);
}
