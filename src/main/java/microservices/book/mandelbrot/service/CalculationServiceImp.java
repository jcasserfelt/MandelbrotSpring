package microservices.book.mandelbrot.service;

import lombok.*;
import microservices.book.mandelbrot.domain.*;
import microservices.book.mandelbrot.repository.CalculationRepository;
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

        int i = 0; // remove
        for (i = 1; i <= iterations; i++) {
            double nx = x * x - y * y + cx;
            double ny = 2 * x * y + cy;
            x = nx;
            y = ny;

//            double resultValue = x * x + y * y;
            if (x * x + y * y > 2) {
                return i;
            }
        }
        // remove
//        if (i == iterations) {
//            return iterations;
//        }
        return iterations;
    }

    @Override
    public CalcResult calculateIntArea(CalcParameters parameters) {


//        for (int i = 0; i < 10; i++) {
//            long startTimeMakeCoords = System.currentTimeMillis();
//            List<InnerCoords> coordinates = makeCoordinates(parameters);
//            long endTimeMakeCoords = System.currentTimeMillis();
//            long timeCoords = endTimeMakeCoords - startTimeMakeCoords;
//
//            System.out.println(i + ": " + timeCoords);
//        }
//        System.out.println("-------------");
        List<Coordinate> coordinates = makeCoordinates(parameters);
        long totalIterations = 0;
        int[] resultArray = new int[coordinates.size()];
//        List<Integer> resultList = new ArrayList<>();

        int counter = 0;
        long startTime = System.currentTimeMillis();
        try {
            for (Coordinate tempCoordinate : coordinates) {
                int temp = calculateIntPoint(tempCoordinate.getXVal(), tempCoordinate.getYVal(), parameters.getInf_n());

                resultArray[counter] = temp;
//                resultList.add(temp);
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

    @Override
    public void convertToRGBA(CalcResult calcResult) {

    }

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

//            double resultValue = x * x + y * y;
            if (x * x + y * y > 2) {
                return (byte) i;
            }
        }
        if (i == iterations) {
            return (byte) iterations;
        }
        return (byte) iterations;
    }

    @Override
    public List<Byte> calculateArea(CalcParameters parameters) {
        // list med inner cords
        // skapa inner coords mha makeCoords()
        List<Coordinate> coordinates = makeCoordinates(parameters);
        List<Byte> resultList = new ArrayList<>();

        // använd calculatesubarea(inner coords lista)
        int counter = 0;
        try {
            for (Coordinate tempCoordinate : coordinates) {
                byte temp = calculatePoint(tempCoordinate.getXVal(), tempCoordinate.getYVal(), parameters.getInf_n());
                resultList.add(temp);
//                subResultArray[counter] = this.convertToPGMRangeByte(calculatePoint2(tempCoordinate.x, tempCoordinate.y, inf_n), inf_n);
//                subResultArray[counter] = this.convertToPGMRangeByte(temp, inf_n);
                counter++;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index vid outofbounce: " + counter);
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
//        Iterable<Calculation> lista = calculationRepository.findAll();
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
        Iterable<Calculation> resultList = calculationRepository.findAll();
        List<CalculationsRepresentation> calculationsRepresentations = new ArrayList<>();
        for (Calculation c : calculationRepository.findAll()) {
            calculationsRepresentations.add(new CalculationsRepresentation(c));
        }
        return calculationsRepresentations;
    }

//    @ToString
//    @Getter
//    public final class Coordinate {
//
//        double xVal;
//        double yVal;
//
//        public Coordinate(double xVal, double yVal) {
//            this.xVal = xVal;
//            this.yVal = yVal;
//        }
//
//
//    }

    @Getter
    @Setter
    public final class CalculationsRepresentation {
        Long id;
        User user;
        CalcParameters calcParameters;
        int[] resultData;
        Timestamp timestamp;


        public CalculationsRepresentation(Calculation calculation) {
            this.id = calculation.getId();
            this.user = calculation.getUser();
            this.calcParameters = calculation.getCalcParameters();
            this.resultData = null;
            this.timestamp = calculation.getTimestamp();
        }
    }


    @Override
    public Calculation performParallelCalculation(CalcParameters p) {
        long startTimeParallel = System.currentTimeMillis();

        int amountOfCoordinates = p.getX() * p.getY();
        int[] resultArray = new int[amountOfCoordinates];
        int coordsPerSubArea = Math.floorDiv(amountOfCoordinates, p.getDivider());
        List<Coordinate> allCoords = makeCoordinates(p);

        // testning av metod
//        List<InnerCoords> subArea = pickOutSubSetOfCoordinates(1, p.getDivider(), allCoords);
//        CalcTask testTask = new CalcTask(1, p, allCoords);
//        testTask.calcArea(allCoords);

//        List<List<InnerCoords>> tempList = new ArrayList<>();
//        for (int i = 0; i < 4; i++) {
//            tempList.add(pickOutSubSetOfCoordinates(i, 4, allCoords));
//        }

        int coreCount = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(coreCount);

        List<CalcTask> calcTasks = new ArrayList<>();
        int calcTime = 0;
        for (int i = 0; i < p.getDivider(); i++) {
            long startTimeTask = System.currentTimeMillis();
            CalcTask task = new CalcTask(i, p, allCoords);
//            System.out.println("skapa CalcTask" + i + ": " + (System.currentTimeMillis() - startTimeTask));
//            Future<CalcSubResult> future = service.submit(task);
            calcTasks.add(task);
        }
        try {
            long startTime = System.currentTimeMillis();
//            System.out.println("invoke all starts");
            List<Future<CalcSubResult>> futures = service.invokeAll(calcTasks);
//            System.out.println("invoke all: " + (System.currentTimeMillis() - startTime));
            long startTimeFuture = System.currentTimeMillis();
            for (Future<CalcSubResult> future : futures) {
//                calcTime += future.get().calcTime;
                int subResultLength = future.get().getSubResultArray().length;
                int subResultOrder = future.get().getOrder();
                int index = (coordsPerSubArea * (future.get().getOrder()));

                for (int j = 0; j < future.get().getSubResultArray().length; j++) {
                    resultArray[j + index] = future.get().getSubResultArray()[j];
                }
            }
//            System.out.println("sort out futures: " + (System.currentTimeMillis() - startTimeFuture));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        calcTime = (int) (System.currentTimeMillis() - startTimeParallel);
//        System.out.println("calcTime varable: " + calcTime);
        // return new CalcResult(resultArray, calcTime, totalIterations);
        CalcResult calcResult = new CalcResult(resultArray, calcTime, 0);
        // Calculation calculation = new Calculation(user, calcParameters, calcResult, new Timestamp(new Date().getTime()));
        return new Calculation(new User(), p, calcResult, new Timestamp(new Date().getTime()));
    }


    public final class CalcTask implements Callable<CalcSubResult> {

        int order;
        CalcParameters parameters;
        List<Coordinate> allCoords;

        public CalcTask(int order, CalcParameters parameters, List<Coordinate> allCoords) {

            this.order = order;
            this.parameters = parameters;
            this.allCoords = allCoords;
        }

        public int[] calcArea(List<Coordinate> coords) {
            int[] resultArray = new int[coords.size()];

            int counter = 0;
            int tempResult = 0;
            int totalIterations = 0;
            for (Coordinate c : coords) {
                tempResult = calculateIntPoint(c.getXVal(), c.getYVal(), this.parameters.getInf_n());
                resultArray[counter] = tempResult;
                totalIterations += tempResult;
                counter++;
            }
            return resultArray;
        }

        public int calculateIntPoint(double x, double y, int iterations) {
            double cx = x;
            double cy = y;

            int i = 0; // remove
            for (i = 1; i <= iterations; i++) {
                double nx = x * x - y * y + cx;
                double ny = 2 * x * y + cy;
                x = nx;
                y = ny;

//            double resultValue = x * x + y * y;
                if (x * x + y * y > 2) {
                    return i;
                }
            }
            return iterations;
        }

        @Override
        public CalcSubResult call() throws Exception {
            int testApa = 20;
            int[] subResult = new int[0];
            long calcTime = 0;
            try {
//                System.out.println("inside callable");
                List<Coordinate> subArea = pickOutSubSetOfCoordinates(this.order, parameters.getDivider(), allCoords);
//                System.out.println("subArea: " + subArea.toString());
                long startTime = System.currentTimeMillis();
                subResult = calcArea(subArea);
                long finishTime = System.currentTimeMillis();
//                System.out.println("subResult: " + subResult.toString());
                calcTime = finishTime - startTime;
            } catch (Exception e) {
                e.printStackTrace();
            }
//            System.out.println("order:" + this.order);
//            System.out.println("calcTime:" + calcTime);
//            System.out.println("call() is done");
            return new CalcSubResult(this.order, subResult, calcTime, 0);
        }
    }


    @Override
    public List<Coordinate> pickOutSubSetOfCoordinates(int order, int divider, List<Coordinate> allCoords) {
        int totalCoords = allCoords.size();
        int coordsPerSubarea = Math.floorDiv(totalCoords, divider);
        List<Coordinate> subArea = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < coordsPerSubarea; i++) {
            index = i + (coordsPerSubarea * order);
            subArea.add(allCoords.get(index));
        }
        return subArea;
    }
}