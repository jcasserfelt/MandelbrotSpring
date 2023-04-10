package microservices.book.mandelbrot.domain;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public final class CalculationsRepresentation {
    Long id;
    CalcParameters calcParameters;
    int[] resultData;
    Timestamp timestamp;


    public CalculationsRepresentation(Calculation calculation) {
        this.id = calculation.getId();
        this.calcParameters = calculation.getCalcParameters();
        this.resultData = null;
        this.timestamp = calculation.getTimestamp();
    }
}

