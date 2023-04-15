package microservices.book.mandelbrot.service.util.parallel;

import microservices.book.mandelbrot.service.util.parallel.AbstractParallelFractalCalculation;

public class ParallelJuliaCalculation extends AbstractParallelFractalCalculation {
    @Override
    protected int calculatePoint(double x, double y, int iterations) {
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
}
