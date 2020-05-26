package microservices.book.mandelbrot.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Entity
public class CalcParameters {

    @Id
    @GeneratedValue
    @Column(name = "CALCPARAMETERS_ID")
    private Long id;


    private final double min_c_re;              // min real value
    private final double min_c_im;              // min imaginary value
    private final double max_c_re;              // max real value
    private final double max_c_im;              // max imaginary value
    private final int x;                        // width resolution of total area
    private final int y;                        // height resolution of total area
    private final int inf_n;                    // iteration limit
    private final int divider;                  // number of height and width divisions of total area

    public CalcParameters() {
        this(0f, 0f, 0f, 0f, 0, 0, 0, 0);
    }

}
