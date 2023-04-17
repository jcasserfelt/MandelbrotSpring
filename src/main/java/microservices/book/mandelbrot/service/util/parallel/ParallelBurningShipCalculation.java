package microservices.book.mandelbrot.service.util.parallel;

public class ParallelBurningShipCalculation extends AbstractParallelFractalCalculation{
    @Override
    protected int calculatePoint(double x, double y, int iterations) {
        double cx = x;
        double cy = y;

        for (int i = 1; i <= iterations; i++) {
            double nx = x * x - y * y + cx;
            double ny = 2 * Math.abs(x * y) + cy; // replace "y" with "Math.abs(y)"
            x = nx;
            y = ny;

            if (x * x + y * y > 4) {
                return i;
            }
        }
        return iterations;
    }
}