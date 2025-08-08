# Cap. 07 - Beyond Classes

## Registros - Records

Una clase sellada es una clase que restringe qué otras clases pueden extenderla directamente. 

Antes de avanzar debemos entender el concepto de encapsulación, para eso debemos saber que es un **POJO "Plan Old Java Object"** el cual es una clase usada pra modelar y pasar datos sencillos (también es asociado con un JavaBean que es un POJO con reglas adicionales)

```java
public class Crane {
    int numbersEggs;
    String name;
    
    public Crane(int numbersEggs, String name) {
        this.numbersEggs = numbersEggs;
        this.name = name;
    }
}

// Los campos son de acceso al paquete, lo que signfica que desde otra clase del mismo paquete podrían cambiar valores y crear datos no válidos como: 

public class Poacher {
    public void badActor() {
        var mother = new Crane(5, "Cathy");
        mother.numbersEggs = -100;
    }
}

// Esto claramente no es bueno por lo que podemos recurrir a la encapsulación, es una forma de proteger a los miembros de 
// la clase al restringir el acceso a ellos. la encapsulación nos permite modificar los métodos y el comportamiento de la 
// clase posteriormente sin que alguien tenga acceso directo a una variable de instancia dentro de la clase. Por ejemplo, 
// podemos cambiar el tipo de datos de una variable de instancia, pero manteenr las mismas firmas del método con lo 
// cual mantenemos el control total sobre el funcionamiento interno de la clase. Por ejemplo la clase Crane podría quedar así:

public final class Crane {
    private final int numbersEggs;
    private final String name;

    public Crane(int numbersEggs, String name) {
        if (numbersEggs >= 0) this.setNumbersEggs(numbersEggs);
        else throw new IllegalArgumentException();
        this.name = name;
    }

    public int getNumbersEggs() {
        return numbersEggs;
    }

    public int getName() {
        return name;
    }
}


```

#### Palabras clave asociadas

