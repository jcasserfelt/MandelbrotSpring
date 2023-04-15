package microservices.book.mandelbrot.service.util;

import microservices.book.mandelbrot.domain.CalcParameters;
import microservices.book.mandelbrot.domain.CalcResult;

import java.util.ArrayList;
import java.util.List;

public final class CalcUtils {

    public static int calculateIntPoint(double x, double y, int iterations) {
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


    public static int calculateJuliaPoint(double x, double y, int iterations){
        // hard coded for values for the famous "seahorse" fractal
        double cx = -0.7;
        double cy = 0.27015;

        for (int i = 1; i <= iterations; i++) {
            double nx = x * x - y * y + cx;
            double ny = 2 * x * y + cy;
            x = nx;
            y = ny;

            if (x * x + y * y > 4) { // the escape condition is different for the Julia set
                return i;
            }
        }
        return iterations;
    }

    public static CalcResult calculateIntArea(CalcParameters parameters) {
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

    public static int[] calcArea(List<Coordinate> coords, int maxIterations) {
        int[] resultArray = new int[coords.size()];

        int counter = 0;
        int tempResult = 0;
        int totalIterations = 0;
        for (Coordinate c : coords) {
            tempResult = CalcUtils.calculateIntPoint(c.getXVal(), c.getYVal(), maxIterations);
//            tempResult = CalcUtils.calculateJuliaPoint(c.getXVal(), c.getYVal(), maxIterations);
            resultArray[counter] = tempResult;
            totalIterations += tempResult;
            counter++;
        }
        return resultArray;
    }

    public static List<Coordinate> pickOutSubSetOfCoordinates(int order, int divider, List<Coordinate> allCoords) {
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


    public static List<Coordinate> makeCoordinates(CalcParameters p) {

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

    public static byte calculatePoint(double x, double y, int iterations) {
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

    public static List<Byte> calculateArea(CalcParameters parameters) {
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
}
