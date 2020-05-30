package microservices.book.mandelbrot.repository;

import microservices.book.mandelbrot.domain.CalcResult;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CalcResultRepository extends CrudRepository<CalcResult, Long> {
}
