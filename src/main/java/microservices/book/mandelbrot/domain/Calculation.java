package microservices.book.mandelbrot.domain;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

// @NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
public final class Calculation {

    @Id
    @GeneratedValue
    @Column(name = "CALCULATION_ID")
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "CALCPARAMETERS_ID")
    private final CalcParameters calcParameters;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "CALCRESULT_ID")
    private final CalcResult resultObj;

    private final Timestamp timestamp;

    // Empty constructor for JSON/JPA
    public Calculation() {
        calcParameters = null;
        resultObj = null;
        timestamp = null;
    }

    public void resetResultdata() {
        return;
    }
}
