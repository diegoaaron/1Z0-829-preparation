# Cap. 08 - Lambdas and Functional Interfaces

## Lambdas

La programación funcional es una forma de escribir código de forma más declarativa. Se especifica lo que se quiere hacer en lugar de lidiar con el estado de los objetos. Se centra más en las expresiones que en los bucles.

Una expresión lambda es como un método sin nombre que existe dentro de una clase anónima. Tiene parámetros y un cuerpo al igual que los métodos completos, pero no tiene un nombre.

Para mostrar la potencia de las lambdas, usaremos un ejemplo donde deseamos mostrar todos los animales de una lista según algunos criterios. Primero de forma normal:

```java
//  Comenzamos creando un registro con 3 campos.

public record Animal(String species, boolean canHop, boolean canSwim) {}

// Supongamos que tenemos una lista de animales y queremos procesar los datos en función de un atributo específico. 
// Por ejemplo los animales que pueden saltar. Para ese definimos una interfaz que generalice este concepto y admita una variedad de comprobaciones.

public interface CheckTrait {
    boolean test(Animal a);
}

// Lo primero que queremos comprobar es si el Animal puede saltar por lo que creamos una clase que implementa la interfaz

public class CheckIfHopper implements CheckTrait {
    public boolean test(Animal a) {
        return a.canHop();
    }
}

// La clase parece simple y lo es. esto es parte del problema que resuelven los lambdas.

import java.util.*;

public class TraditionalSearch {
    public static void main(String[] args) {

        // lista de animales
        var animals = new ArrayList<Animal>();
        animals.add(new Animal("fish", false, true));
        animals.add(new Animal("kangaroo", true, false));
        animals.add(new Animal("rabbit", true, false));
        animals.add(new Animal("turtle", false, true));
        
        // pass class that does check
        print(animals, new CheckIfHopper()); // (13)
    }

    private static void print(List<Animal> animals, CheckTrait checker) {
        for (Animal animal : animals) {
            // general check
            if(checker.test(animal)) {
                System.out.println(animal + "");
            }
            System.out.println();
        }
    }
}

```

¿Qué pasa si queremos imprimir los animales que nadan? Podriamos crear otra clase (CheckIfSwims). Si bien son solo unas pocas líneas, es carga. 

Después agregar la línea debajo de la línea 13. Todo esto se puede simplificar con lambdas. La línea 13 se puede cambiar por: 

`print(animals, a -> a.canHop());`

Esto le dice a Java que solo nos importa si una animal puede saltar.

Ahora si queremos los animales que pueden nadar sería agregar la línea:

`print(animals, a -> a.canSwim());`

Y si queremos los animales que no pueden nadar sería: 

`print(animals, a -> !a.canSwim());`

Las lambdas usan un concepto llamado ejecución diferida que significa que el código se especifica ahora, pero se ejecuta más tarde. 

En este caso "más tarde" está dentro del cuerpo del método `print()`, a diferencia de cuando se pasa al método de forma normal.

### Sintaxis de las Lambdas

La sintaxis mas basica de un lambda es:

`a -> a.canHop()`

Las lambdas funcionan con interfaces que tienen exactamente un método abstracto. En este caso, Java analiza la interfaz CheckTrait, que tiene un método. 

La lambda en nuestro ejemplo sugiere que Java debería llamar a un método con un parámetro Animal que devuelva un valor booleano que sea el resultado de `a.canHop()`

Java se basa en el contexto para determinar el significado de las expresiones lambda. Entendiendo contexto a donde y cómo se interpreta la lambda. 

Refiriéndonos a nuestro ejemplo anterior, pasamos la lambda como segundo parámetro del método `print()`:

`print(animals, a -> a.canHop());`

El método `print()` espera un objeto `CheckTrait` como segundo parámetro:

`private static void print(List<Animal> animals, CheckTrait checker) {}`

Como estamos pasando una lambda, Java intenta mapear nuestra lambda a la declaración del método abstracto en la interfaz CheckTrait:

`boolean test(Animal a);`

Dado que el método de esa interfaz toma un Animal, el parámetro lambada tiene que ser un Animal. Y como el método de esa interfaz devuelve un booleano, sabemos que la lambda devuelve un booleano.

La sintaxis de las lambdas son complicadas porque tiene valores opciones, por ejemplo las siguientes expresiones son iguales:

`a -> a.canHop()`

`(Animal a) -> { return a.canHop(); }`

