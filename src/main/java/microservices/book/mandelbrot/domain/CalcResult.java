package microservices.book.mandelbrot.domain;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

@Data
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Entity
public final class CalcResult {

    @Id
    @GeneratedValue
    @Column(name = "CALCRESULT_ID")
    private Long id;

    public final int[] resultData;
    public final long calculationTime;
    public final long totalIterations;

    // these constructor annotations..
    public CalcResult() {
        resultData = new int[10];
        calculationTime = 0l;
        totalIterations = 0l;
    }

}
