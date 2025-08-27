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
// En este ejemplo, la referencia hasTail solo tiene acceso directo a los métodos definidos con la interfaz HasTail; por lo tanto, 
// no sabe que la variable age es parte del objeto. Del mismo modo, la referencia primate solo tiene acceso a los 
// métodos definidos en la clase Primate y no tiene acceso directo al método isTailStriped()

HasTail hasTail = new Lemur();
System.out.println(hasTail.age); // Error de compilación

Primate primate = new Lemur();
System.out.println(primate.isTailStriped()); // Error de compilación
```

#### Objeto vs Referencia

En Java, todos los objetos se acceden por referencia, por lo que como desarrollador nunca tienes acceso directo al objeto mismo. Conceptualmente, sin embargo, deberías considerar el objeto como la entidad que existe en memoria, asignada por el Java Runtime Environment. Independientemente del tipo de referencia que tengas para el objeto en memoria, el objeto en sí mismo no cambia. Por ejemplo, dado que todos los objetos heredan de `java.lang.Object`, todos pueden ser referenciados a `java.lang.Object`, como se muestra en el siguiente ejemplo:

```java
Lemur lemur = new Lemur();
Object lemurAsObject = lemur;
```

Aunque el objeto Lemur se haya asignado a una referencia con un tipo diferente, el objeto en sí no ha cambiado y sigue existiendo como un objeto Lemur en memoria. Lo que ha cambiado, entonces, es nuestra capacidad para acceder a los métodos dentro de la clase Lemur con la referencia lemurAsObject. Sin una conversión explicita a Lemur, como se ve en la siguiente sección, ya no tenemos acceso a las propiedades del objeto Lemur.

