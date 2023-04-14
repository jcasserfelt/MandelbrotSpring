package microservices.book.mandelbrot.service.util;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class Coordinate {

    double xVal;
    double yVal;

    public Coordinate(double xVal, double yVal) {
        this.xVal = xVal;
        this.yVal = yVal;
    }
}
