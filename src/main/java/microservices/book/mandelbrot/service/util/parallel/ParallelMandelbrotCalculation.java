package microservices.book.mandelbrot.service.util.parallel;

import microservices.book.mandelbrot.service.util.parallel.AbstractParallelFractalCalculation;

public class ParallelMandelbrotCalculation extends AbstractParallelFractalCalculation {

    public ParallelMandelbrotCalculation(){
        super(); // todo necessary?
    }

    @Override
    protected int calculatePoint(double x, double y, int iterations) {
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
}
