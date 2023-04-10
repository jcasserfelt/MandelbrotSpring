package microservices.book.mandelbrot.service.util;

import microservices.book.mandelbrot.domain.CalcParameters;
import microservices.book.mandelbrot.domain.CalcSubResult;
import microservices.book.mandelbrot.domain.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class CalcTask  implements Callable<CalcSubResult> {

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