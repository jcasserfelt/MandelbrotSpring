package microservices.book.mandelbrot.service;

import microservices.book.mandelbrot.domain.CalcParameters;
import microservices.book.mandelbrot.domain.CalcResult;
import microservices.book.mandelbrot.domain.Calculation;
import microservices.book.mandelbrot.domain.Coordinate;

import java.util.List;

public interface CalculationService {


    /**
     * @param x          real value
     * @param y          imaginary value
     * @param iterations max value of iterations
     * @return number of times it takes for
     * the kind-of recursive function to reach and abs
     * value greater than 2.
     */
//    int calculatePoint(double x, double y, int iterations);

    byte calculatePoint(double x, double y, int iterations);


    int calculateIntPoint(double x, double y, int iterations);

    void convertToRGBA(CalcResult calcResult);

    /**
     * @param parameters object containing boundary values needed
     *                   to calculate a representation of the mandelbrot set.
     * @return
     */
    List<Byte> calculateArea(CalcParameters parameters);

    CalcResult calculateIntArea(CalcParameters parameters);

    List<Coordinate> makeCoordinates(CalcParameters parameters);

    String saveCalculationToDatabase(Calculation calculation);

    Calculation getCalculationById(Long id);

    List<Calculation> getAllCalculations();

    List<CalculationServiceImp.CalculationsRepresentation> getAllCalculationsRepresentations();

    Calculation performParallelCalculation(CalcParameters parameters);

    List<Coordinate> pickOutSubSetOfCoordinates(int order, int divider, List<Coordinate> allCoords);

}


