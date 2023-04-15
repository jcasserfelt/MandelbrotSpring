package microservices.book.mandelbrot.service.util.parallel;

import microservices.book.mandelbrot.domain.CalcParameters;
import microservices.book.mandelbrot.domain.CalcResult;
import microservices.book.mandelbrot.domain.Calculation;
import microservices.book.mandelbrot.service.util.CalcSubResult;
import microservices.book.mandelbrot.service.util.CalcTask;
import microservices.book.mandelbrot.service.util.CalcUtils;
import microservices.book.mandelbrot.service.util.Coordinate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class AbstractParallelFractalCalculation {

    protected abstract int calculatePoint(double x, double y, int iterations);

    protected int[] calculateArea(List<Coordinate> areaCoordinates, int maxIteration) {
        int[] resultArray = new int[areaCoordinates.size()];

        long totalIterations = 0;
        int counter = 0;
        try {
            for (Coordinate tempCoordinate : areaCoordinates) {
                int temp = calculatePoint(tempCoordinate.getXVal(), tempCoordinate.getYVal(), maxIteration);

                resultArray[counter] = temp;
                totalIterations += temp;
                counter++;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index vid outofbounce: " + counter);
            e.printStackTrace();
        }

        return resultArray;
    }

    public Calculation performParallelCalculation(CalcParameters parameters) {
        long startTimeParallel = System.currentTimeMillis();

        int totalAmountOfCoordinates = parameters.getX() * parameters.getY();
        int[] resultArray = new int[totalAmountOfCoordinates];
        int coordinatesPerSubArea = Math.floorDiv(totalAmountOfCoordinates, parameters.getDivider());

        List<Coordinate> allCoordinates = CalcUtils.makeCoordinates(parameters);

        int coreCount = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(coreCount);

        List<CalcTask> calcTasksNew = new ArrayList<>(parameters.getDivider());
        int calcTime = 0;

        // create a list of calculation tasks that will be run in parallel
        for (int i = 0; i < parameters.getDivider(); i++) {
            List<Coordinate> currentSubArea = CalcUtils.pickOutSubSetOfCoordinates(i, parameters.getDivider(), allCoordinates);

            CalcTask task = new CalcTask(i, parameters.getDivider(), currentSubArea, () -> calculateArea(currentSubArea, parameters.getInf_n()));
            calcTasksNew.add(task);
        }

        // execute the calculation tasks 🎆🎇🌠💫
        try {
            List<Future<CalcSubResult>> futures = service.invokeAll(calcTasksNew);
            for (Future<CalcSubResult> future : futures) {
                int index = (coordinatesPerSubArea * (future.get().getOrder()));
                for (int j = 0; j < future.get().getSubResultArray().length; j++) {
                    resultArray[j + index] = future.get().getSubResultArray()[j];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        calcTime = (int) (System.currentTimeMillis() - startTimeParallel);
        CalcResult calcResult = new CalcResult(resultArray, calcTime, 0);
        return new Calculation(parameters, calcResult, new Timestamp(new Date().getTime()));
    }

}
