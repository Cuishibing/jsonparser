package cui.shibing;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Person {
    private String name;
    private Double age;

    private Person aa;
}
