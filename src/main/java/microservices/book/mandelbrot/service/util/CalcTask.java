package microservices.book.mandelbrot.service.util;

import microservices.book.mandelbrot.domain.CalcParameters;

import java.util.List;
import java.util.concurrent.Callable;

public final class CalcTask implements Callable<CalcSubResult> {

    int order;
    CalcParameters parameters;
    List<Coordinate> allCoords;

    public CalcTask(int order, CalcParameters parameters, List<Coordinate> allCoords) {
        this.order = order;
        this.parameters = parameters;
        this.allCoords = allCoords;
    }

    @Override
    public CalcSubResult call() {
        int[] subResult = new int[0];
        long calcTime = 0;
        try {
            List<Coordinate> subArea = CalcUtils.pickOutSubSetOfCoordinates(order, parameters.getDivider(), allCoords);
            long startTime = System.currentTimeMillis();
            subResult = CalcUtils.calcArea(subArea, parameters.getInf_n());
            long finishTime = System.currentTimeMillis();
            calcTime = finishTime - startTime;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new CalcSubResult(order, subResult, calcTime, 0);
    }
}
