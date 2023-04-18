package microservices.book.mandelbrot.service.util.parallel;

import microservices.book.mandelbrot.service.util.parallel.AbstractParallelFractalCalculation;

public class ParallelJuliaCalculation extends AbstractParallelFractalCalculation {
    @Override
    protected int calculatePoint(double x, double y, int iterations) {
    // cx = -0.7, cy = 0.27015 - this seed value generates the famous "seahorse" fractal, which has a distinctive shape with intricate curves and spirals.
    // cx = 0.285, cy = 0.01 - this seed value generates a fractal with a similar shape to the seahorse, but with a more compact and symmetrical structure.
    // cx = -0.8, cy = 0.156 - this seed value generates a fractal with a more elongated and spiky shape, with sharp edges and thin tendrils.

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
