package microservices.book.mandelbrot.repository;

import microservices.book.mandelbrot.domain.Calculation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Transactional
public interface CalculationRepository extends CrudRepository<Calculation, Long> {

    Optional<Calculation> findFirstById(Long id);

}
