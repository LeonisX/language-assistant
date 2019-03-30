package md.leonis.assistant.dao.test;

import md.leonis.assistant.domain.test.Variance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VarianceDAO extends JpaRepository<Variance, Long> {

    //TODO optimize - store in one case
    List<Variance> findByVarianceEqualsIgnoreCase(String variance);
}
