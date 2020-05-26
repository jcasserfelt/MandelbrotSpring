package microservices.book.mandelbrot.repository;

import microservices.book.mandelbrot.domain.CalcParameters;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface CalcParametersRepository extends CrudRepository<CalcParameters, Long> {

//    List<CalcParameters> getAll();


}
