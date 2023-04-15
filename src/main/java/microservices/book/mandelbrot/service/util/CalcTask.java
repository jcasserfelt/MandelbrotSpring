package microservices.book.mandelbrot.service.util;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class CalcTask implements Callable<CalcSubResult> {

    private final int order;
    private final int divider; // todo remove
    private final List<Coordinate> coordinates; // todo remove
    private final Supplier<int[]> calculationJob;

    public CalcTask(int order,
                    int parameters,
                    List<Coordinate> coordinates,
                    Supplier<int[]> calculationJob) {
        this.order = order;
        this.divider = parameters;
        this.coordinates = coordinates;
        this.calculationJob = calculationJob;
    }

    @Override
    public CalcSubResult call() {
        int[] subResult = new int[0];
        long calcTime = 0;
        try {
            long startTime = System.currentTimeMillis();
            subResult = calculationJob.get();
            long finishTime = System.currentTimeMillis();
            calcTime = finishTime - startTime;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new CalcSubResult(order, subResult, calcTime, 0);
    }
}
