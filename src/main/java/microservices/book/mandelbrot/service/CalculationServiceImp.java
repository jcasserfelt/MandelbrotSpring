package microservices.book.mandelbrot.service;

import microservices.book.mandelbrot.domain.*;
import microservices.book.mandelbrot.repository.CalculationRepository;
import microservices.book.mandelbrot.service.util.CalcSubResult;
import microservices.book.mandelbrot.service.util.CalcTask;
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

    @Override
    public int calculateIntPoint(double x, double y, int iterations) {
        double cx = x;
        double cy = y;

        // (x + yi)^2 = x^2 + 2*x*y*i - y^2 => Zr = x*x + y*y, Zi = 2*x*y
        for (int i = 1; i <= iterations; i++) {
            double nx = x * x - y * y + cx;
            double ny = 2 * x * y + cy;
            x = nx;
            y = ny;

            if (x * x + y * y > 2) {
                return i;
            }
        }
        return iterations;
    }

    @Override
    public CalcResult calculateIntArea(CalcParameters parameters) {
        List<Coordinate> coordinates = makeCoordinates(parameters);
        int[] resultArray = new int[coordinates.size()];

        long totalIterations = 0;
        int counter = 0;
        long startTime = System.currentTimeMillis();
        try {
            for (Coordinate tempCoordinate : coordinates) {
                int temp = calculateIntPoint(tempCoordinate.getXVal(), tempCoordinate.getYVal(), parameters.getInf_n());

                resultArray[counter] = temp;
                totalIterations += temp;
                counter++;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index vid outofbounce: " + counter);
            e.printStackTrace();
        }

        long finishTime = System.currentTimeMillis();
        long calcTime = finishTime - startTime;
        return new CalcResult(resultArray, calcTime, totalIterations);
    }

    // todo remove
    @Override
    public void convertToRGBA(CalcResult calcResult) {
    }

    // todo change name, byte something
    @Override
    public byte calculatePoint(double x, double y, int iterations) {
        double cx = x;
        double cy = y;

        int i = 0;
        for (i = 1; i <= iterations; i++) {
            double nx = x * x - y * y + cx;
            double ny = 2 * x * y + cy;
            x = nx;
            y = ny;

            if (x * x + y * y > 2) {
                return (byte) i;
            }
        }

        return (byte) iterations;
    }

    // todo change name, byte something
    @Override
    public List<Byte> calculateArea(CalcParameters parameters) {
        List<Coordinate> coordinates = makeCoordinates(parameters);
        List<Byte> resultList = new ArrayList<>();

        int counter = 0;
        try {
            for (Coordinate tempCoordinate : coordinates) {
                byte temp = calculatePoint(tempCoordinate.getXVal(), tempCoordinate.getYVal(), parameters.getInf_n());
                resultList.add(temp);
                counter++;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index vid outofbounce: " + counter);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }

    @Override
    public List<Coordinate> makeCoordinates(CalcParameters p) {

        ArrayList<Coordinate> coordinates = new ArrayList<>();

        double xInterval = Math.abs(p.getMax_c_re() - p.getMin_c_re());
        double yInterval = Math.abs(p.getMax_c_im() - p.getMin_c_im());

        double tempX = p.getMin_c_re();
        double tempY = p.getMax_c_im();

        double xAdd;
        double ySub;

        int x = p.getX();
        int y = p.getY();

        // create x*y coordinates
        for (int i = 0; i < y; i++) {
            if (i != 0) {
                ySub = (yInterval / (x - 1));
                tempY = tempY - ySub;
            }
            tempX = p.getMin_c_re();           // restart x value for the next row
            for (int j = 0; j < x; j++) {
                coordinates.add(new Coordinate(tempX, tempY));
                xAdd = (xInterval / (y - 1));
                tempX = tempX + xAdd;
            }
        }
        return coordinates;
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
        List<Coordinate> allCoords = makeCoordinates(p);

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