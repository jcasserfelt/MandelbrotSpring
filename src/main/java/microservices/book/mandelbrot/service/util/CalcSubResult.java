package microservices.book.mandelbrot.service.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public final class CalcSubResult {
    int order;
    int[] subResultArray;
    long calcTime;
    int iterations;
}
