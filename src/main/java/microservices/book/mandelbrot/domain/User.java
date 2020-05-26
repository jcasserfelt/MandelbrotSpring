package microservices.book.mandelbrot.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "User")
public final class User {
    @Id
    @GeneratedValue
    @Column(name = "USER_ID")
    private Long id;

    private final String name;
    private final String password;



//    @ElementCollection
//    private final List<Calculation> calculationsList;


    // Empty constructor for JSON/JPA
    public User() {
        name = null;
        password = null;
//        calculationsList = null;
    }
}

