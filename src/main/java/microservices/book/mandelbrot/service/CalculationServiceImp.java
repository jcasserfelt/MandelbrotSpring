package microservices.book.mandelbrot.service;


import microservices.book.mandelbrot.domain.CalcParameters;
import microservices.book.mandelbrot.domain.CalcResult;
import microservices.book.mandelbrot.domain.Calculation;
import microservices.book.mandelbrot.domain.CalculationsRepresentation;
import microservices.book.mandelbrot.repository.CalculationRepository;
import microservices.book.mandelbrot.service.util.CalcSubResult;
import microservices.book.mandelbrot.service.util.CalcTask;
import microservices.book.mandelbrot.service.util.CalcUtils;
import microservices.book.mandelbrot.service.util.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

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
    public Calculation performParallelCalculation(CalcParameters p) {
        long startTimeParallel = System.currentTimeMillis();

        int amountOfCoordinates = p.getX() * p.getY();
        int[] resultArray = new int[amountOfCoordinates];
        int coordsPerSubArea = Math.floorDiv(amountOfCoordinates, p.getDivider());
        List<Coordinate> allCoords = CalcUtils.makeCoordinates(p);

        int coreCount = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(coreCount);

        List<CalcTask> calcTasks = new ArrayList<>(p.getDivider());
        int calcTime = 0;
        for (int i = 0; i < p.getDivider(); i++) {
            CalcTask task = new CalcTask(i, p, allCoords);
            calcTasks.add(task);
        }
        try {
            List<Future<CalcSubResult>> futures = service.invokeAll(calcTasks);
            for (Future<CalcSubResult> future : futures) {
                int index = (coordsPerSubArea * (future.get().getOrder()));
                for (int j = 0; j < future.get().getSubResultArray().length; j++) {
                    resultArray[j + index] = future.get().getSubResultArray()[j];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        calcTime = (int) (System.currentTimeMillis() - startTimeParallel);
        CalcResult calcResult = new CalcResult(resultArray, calcTime, 0);
        return new Calculation(p, calcResult, new Timestamp(new Date().getTime()));
    }
}