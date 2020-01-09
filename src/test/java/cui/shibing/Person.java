package cui.shibing;

import lombok.Data;

@Data
public class Person {
    private String name;
    private Double age;

    private Person aa;
}
