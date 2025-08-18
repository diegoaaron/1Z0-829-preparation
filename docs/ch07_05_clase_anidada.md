# Cap. 07 - Beyond Classes

## Clases anidadas - Nested Classes

Una clase sellada es una clase que restringe qué otras clases pueden extenderla directamente. 

Antes de avanzar debemos entender el concepto de encapsulación, para eso debemos saber que es un **POJO "Plan Old Java Object"** el cual es una clase usada pra modelar y pasar datos sencillos.

```java
public class Crane {
    int numbersEggs;
    String name;
    
    public Crane(int numbersEggs, String name) {
        this.numbersEggs = numbersEggs;
        this.name = name;
    }
}
```