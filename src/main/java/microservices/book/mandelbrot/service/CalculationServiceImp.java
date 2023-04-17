package microservices.book.mandelbrot.service;


import microservices.book.mandelbrot.domain.CalcParameters;
import microservices.book.mandelbrot.domain.Calculation;
import microservices.book.mandelbrot.domain.CalculationsRepresentation;
import microservices.book.mandelbrot.repository.CalculationRepository;
import microservices.book.mandelbrot.service.util.parallel.ParallelBurningShipCalculation;
import microservices.book.mandelbrot.service.util.parallel.ParallelJuliaCalculation;
import microservices.book.mandelbrot.service.util.parallel.ParallelMandelbrotCalculation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalculationServiceImp implements CalculationService {

    private CalculationRepository calculationRepository;

    @Autowired
    public CalculationServiceImp(CalculationRepository calculationRepository) {
        this.calculationRepository = calculationRepository;
    }

    @Transactional
    @Override
    public String saveCalculationToDatabase(Calculation calculation) {
        calculationRepository.save(calculation);
        return "save successful";
    }

    @Transactional
    @Override
    public void deleteCalculationById(Long id) {
        calculationRepository.deleteById(id);
    }

    @Override
    public Calculation getCalculationById(Long id) {
        Calculation result = calculationRepository.findFirstById(id).get();
        return result;
    }

    @Override
    public List<Calculation> getAllCalculations() {
        Iterable<Calculation> resultList = calculationRepository.findAll();
        List<Calculation> calculationList = new ArrayList<>();
        resultList.forEach(calculationList::add);
        return calculationList;
    }

    @Override
    public List<CalculationsRepresentation> getAllCalculationsRepresentations() {
        List<CalculationsRepresentation> calculationsRepresentations = new ArrayList<>();
        for (Calculation c : calculationRepository.findAll()) {
            calculationsRepresentations.add(new CalculationsRepresentation(c));
        }
        return calculationsRepresentations;
    }


    // todo keep track of calc time for each subArea
    @Override
    public Calculation performParallelMandelbrotCalculation(CalcParameters calcParameters) {
        ParallelMandelbrotCalculation calculation = new ParallelMandelbrotCalculation();
        return calculation.performParallelCalculation(calcParameters);
    }

    @Override
    public Calculation performParallelJuliaSetCalculation(CalcParameters calcParameters) {
        ParallelJuliaCalculation calculation = new ParallelJuliaCalculation();
        return calculation.performParallelCalculation(calcParameters);
    }

    @Override
    public Calculation performParallelBurningShipCalculation(CalcParameters calcParameters) {
        ParallelBurningShipCalculation calculation = new ParallelBurningShipCalculation();
        return calculation.performParallelCalculation(calcParameters);
    }
}