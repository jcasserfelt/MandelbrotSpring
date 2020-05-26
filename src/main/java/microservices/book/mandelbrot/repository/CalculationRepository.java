package microservices.book.mandelbrot.repository;

import microservices.book.mandelbrot.domain.Calculation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface CalculationRepository extends CrudRepository<Calculation, Long> {

    Optional<Calculation> findByUser_Name(final String user_name);

    Optional<Calculation> findFirstById(Long id);

}
