# Cap. 07 - Beyond Classes

## Entendiendo el polimorfismo

El polimorfismo es la propiedad de un objeto para adoptar muchas formas diferentes. Precisamente se puede acceder a un objeto Java mediante:

* Una referencia con el mismo tipo que el objeto.
* Una referencia que es una superclase del objeto.
* Una referencia que define una interfaz que el objeto implementa o hereda.

Además, no se requiere una conversión si el objeto se reasigna a un supertipo o interfaz del objeto. 

```java
// Lo más importante a tener en cuenta en este ejemplo es que solo se crea un objeto Lemur. 
// El polimorfismo permite reasignar una instancia de Lemur o pasarla a un método utilizando uno de sus supertipos, como Primate o HasTail. 
// Una vez que el objeto ha sido asignado a un nuevo tipo de referencia, solo se pueden llamar en el objeto a los métodos 
// y variables disponibles para ese tipo de referencia sin una conversión explícita.

public class Primate {
    public boolean hasHair() {
        return true;
    }
}

public interface HasTail {
    public abstract boolean isTailStriped();
}

public class Lemur extends Primate implements HasTail {
    public boolean isTailStriped() {
        return false;
    }
    
    public int age = 10;
    
    public static void main(String[] args) {
        Lemur lemur = new Lemur();
        System.out.println(lemur.age); // 10

        HasTail hasTail = lemur; // Reasignación a interfaz
        System.out.println(hasTail.isTailStriped()); // false
        
        Primate primate = lemur; // Reasignación a superclase
        System.out.println(primate.hasHair()); // true
    }
}
```


```java

```