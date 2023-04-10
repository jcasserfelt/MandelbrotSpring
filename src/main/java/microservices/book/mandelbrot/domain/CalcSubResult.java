package microservices.book.mandelbrot.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class CalcSubResult {
    int order;
    int[] subResultArray;
    long calcTime;
    int iterations;
}
