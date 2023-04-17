package microservices.book.mandelbrot.service;

import microservices.book.mandelbrot.domain.*;

import java.util.List;

public interface CalculationService {

    String saveCalculationToDatabase(Calculation calculation);

    void deleteCalculationById(Long id);

    Calculation getCalculationById(Long id);

    List<Calculation> getAllCalculations();

    List<CalculationsRepresentation> getAllCalculationsRepresentations();

    Calculation performParallelMandelbrotCalculation(CalcParameters parameters);

    Calculation performParallelJuliaSetCalculation(CalcParameters calcParameters);

    Calculation performParallelBurningShipCalculation(CalcParameters calcParameters);
}


