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


    //    @CollectionTable(name="listOfUsers")
//    @Column(columnDefinition = "org.hibernate.type.BlobType")

    //    @Type(type = "org.hibernate.type.BlobType")
    private final int[] resultData;

    private final long calculationTime;
    private final long totalIterations;


    //    public CalcResult(){
//        this(resultData = null, calculationTime = null, totalIterations = null);
//    }
    public CalcResult() {
        resultData = new int[10000];
        calculationTime = 0l;
        totalIterations = 0l;
    }

//    public CalcResult(int[] resultData, long calculationTime, long totalIterations) {
//        this.resultData = resultData;
//        this.calculationTime = calculationTime;
//        this.totalIterations = totalIterations;
//    }


}
