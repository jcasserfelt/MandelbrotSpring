package microservices.book.mandelbrot.domain;

import lombok.*;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

// @NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
public class Calculation {

    @Id
    @GeneratedValue
    @Column(name = "CALCULATION_ID")
    private Long id;

//    private final String name;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "USER_ID")
    private final User user;


    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "CALCPARAMETERS_ID")
    private final CalcParameters calcParameters;


//    @Lob
    private final int[] resultData;

    private final Timestamp timestamp;


//    private final long calcTime;
//    private final String timeStamp;
//    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(new Date().getTime()))


    // Empty constructor for JSON/JPA
    public Calculation() {
//        name = null;
        user = null;
        calcParameters = null;
        resultData = null;
        timestamp = null;
    }

    public void resetResultdata(){
        return;
    }
}
