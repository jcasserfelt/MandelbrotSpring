package microservices.book.mandelbrot.service;

import microservices.book.mandelbrot.domain.*;
import microservices.book.mandelbrot.service.util.Coordinate;

import java.util.List;

public interface CalculationService {

    String saveCalculationToDatabase(Calculation calculation);

    Calculation getCalculationById(Long id);

    List<Calculation> getAllCalculations();

    List<CalculationsRepresentation> getAllCalculationsRepresentations();

    Calculation performParallelCalculation(CalcParameters parameters);

}


