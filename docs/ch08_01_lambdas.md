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

Los parentesis alrededor de los parámetros lambda se puede omitir solo si hay un solo parámetro y su tipo no se indica explicitamente.

Se puede omitir una sentencia `return` y un `;` cuando no se utilizan llaves, pero esto no aplica cuando se tiene dos o más declaraciones. 

Las siguientes sentencias también son posibles: 

`a -> {return a.canHop();}`

`(Animal a) -> a.canHop()`

La primera fila toma cero parámetros y siempre devuelve el valor booleano verdadero. 

La segunda fila toma un parámetro y llama a un método sobre él, devolviendo el resultado. 

La tercera fila hace lo mismo, excepto que define explicitamente el tipo de la variable. 

Las dos últimas filas toman 2 parámetros e ignoran uno de ellos.

| Lambda                                         | # de parametros |
|------------------------------------------------|-----------------|
| `() -> true`                                   | 0               |
| `x -> x.startsWith("test")`                    | 1               |
| `(String x) -> x.startsWith("test")`           | 1               |
| `(x,y) -> {return x.startsWith("test");}`      | 2               |
| `(String x, String y) -> x.startsWith("test")` | 2               |

### Codificando interfaces funcionales

En las secciones anteriores declaramos la interfaz CheckTrait, que tiene un método que debe ser implementado. 

Las lambdas tienen una relación con dichas interfaces. Una interfaz funcional es una interfaz que contiene un solo método abstracto. 

Se le denomina **single abstract method** (SAM). 

```java
// En este ejemplo la interfaz Sprint es de tipo funcional (me aseguro al definir la anotación) porque solo tiene un método abstracto
@FunctionalInterface
public interface Sprint {
    public void sprint(int speed);
}

public class Tiger implements Sprint{
    public void sprint(int speed){
        System.out.println("Animals is sprinting fast!" + speed);
    }
}
```

La anotación `@FunctionalInterface` le dice al compilador que el codigo es para una interfaz funcional y se validara para que esté alineado con estas reglas.

Pero tener en cuenta que lo que hace a una interfaz funcional no es la anotación, sino el solo tener un método abstracto definido. 

Todas las clases heredan ciertos métodos de Object los cuales son:

* `public String toString()`
* `public boolean equals(Object)`
* `public int hashCode()`

Esto se menciona porque existe una excepción a la regla del método abstracto. 

Si una interfaz funcional incluye un método abstracto con la misma firma que un método público que se encuentra en `Object`, esos métodos no cuentan para la prueba del método abstracto único. 

Esto porque cualquier clase que implemente la interfaz heredara de `Object` esos métodos.

```java
// No es una interfaz funcional  ya que toString() es un método público de Object por lo que no cuenta para la prueba de método abstracto único.

public interface Soar {
    abstract String toString();
}

// Dive si es una interfaz funcional.
// Dive si es una interfaz funcional ya que el método dive() es el único método abstracto, mientras que los demas no se cuentan ya que son métodos públicos definidos en la clase Object. 

public interface Dive {
    String toString();
    public boolean equals(Object o);
    public abstract int hashCode();
    pubic void dive();
}

// Esta interfaz arroja un error ya que 'equals' no recibe un tipo 'Object' sino 'Hibernate' por lo que se convierte en un método abstracto y junto a 'void' ya serían 2.
// Por lo que esta no podría ser una interfaz funcial
public interface Hibernate {
    String toString();
    public boolean equals(Hibernate o);
    public abstract int hashCode();
    public void rest();
}
```
## Usando referencia de métodos

La referencia a métodos es otra forma de hacer más facil la lectura de código al solo mencionar el nombre del método.

Supongamos que estamos codificando un patito que intenta aprender a graznar. Primero tenemos una interfaz funcional:

```java
public interface LearnToSpeak {
    void speak(String sound);
}
```
A continuación, descubrimos que hay una clase auxiliar con la que el patito puede trabajar la cual se define de la siguiente forma

```java
public class DuckHelper {
    public static void teacher(String name, LearnToSpeak trainer){
        trainer.speak(name);
    }
}
```

Finalmente, juntamos todo y conocemos la implementación de la interfaz funcional usando lambda

```java
public class Duckling {
    public static void makeSound(String sound){
        LearnToSpeak learner = s -> System.out.println(s);
        DuckHelper.teacher(sound,learner);
    }
}
```

Está bien la implementación, pero tiene una redundancia. La lambda declara un parámetro llamado `s`. 

Sin embargo, ese parámetro solo se pasa a otro método. 

Una referencia a un método nos permite eliminar esa redundancia, y en su lugar, escribir esto: 

```java
LearnToSpeak learner = System.out::println;
```

El operador `::` le indica a Java que llame al método println() más tarde. Este operador crea una referencia al método `println`, para eso: 

1. Reconoce que `LearnToSpeak.speak()` recibe un `String`
2. Ve que `System.out.println()` también puede recibir un `String`
3. Conecta ambos directamente sin necesidad de declarar parámetros intermedios

**Explicación mas resumida**

```java
// 1. La interfaz funcional
public interface LearnToSpeak {
    void speak(String sound);
}

// 2. El helper que usa la estrategia
public class DuckHelper {
    public static void teacher(String name, LearnToSpeak trainer){
        trainer.speak(name);
    }
}

// 3. Duckling que junta todo
public class Duckling {
    public static void makeSound(String sound){
        // Aquí defines CÓMO quieres que hable el patito
        LearnToSpeak learner = System.out::println;

        // Y se lo pasas al helper para que lo ejecute
        DuckHelper.teacher(sound, learner);
    }
}

// 4. Uso final
public class Main {
    public static void main(String[] args) {
        Duckling.makeSound("Quack!");  // Imprime: Quack!
    }
}
```

Una **referencia a un método** y **una lambda** se comportan de la misma manera en tiempo de ejecución. 

Puedes imaginar que el compilador convierte tus referencias a métodos en lambdas por ti. 

Hay cuatro formas para las referencias a métodos:

### Llamando a métodos estaticos

```java
interface Converter{
    long round(double num);
}
```

La interfaz anterior podemos implementarla con el método `round()` en Math. Aquí asignamos una referencia a un método y una lambda a esta interfaz funcional:

```java
Converter methodRef = Math::round;
Converter lambda = x -> Math.round(x);

System.out.println(methodRef.round(100.1)); // 100
```

La primera linea usando referencia a un método con un parámetro, Java pasara el parámetro a ese método.

La segunda línea usa un método lambda con un parámetro que al final se pasa a la función.

Para este ejemplo el método `round()` está sobrecargado por la definición de interfaz ¿Cómo Java sabe si usar la versión con double o float como parámetro?

Esto lo hace por la información del contexto. Java busca un método que coincida con lo implementado. Si no encuentro o hay varias coincidencias, se arroja un error de tipo ambiguo.

### Llamando a métodos de instancia en un objeto específico

```java
interface StringStart{
    boolean beginningCheck(String prefix);
}
```

Ahora usaremos el método `startsWith()` de la clase String que toma un parámetro y devuelve un booleano.

```java
var str = "Zoo";
StringStart methodRef = str::startsWith;
StringStart lambda = s -> str.startsWith(s);

System.out.println(methodRef.beginningCheck("A")); // false
```
La segunda línea muestra que queremos llamar a  `str:startsWith()` y pasar un parametro en tiempo de ejecución 

En el siguiente ejemplo, creamos una interfaz funcional con un método que no acepta ningún parámetro, pero devuelve un valor.

```java
interface StringChecker{
    boolean check();
}
```

Lo implementamos de la siguiente forma

```java
var str = "";
StringChecker methodRef = str::isEmpty;
StringStart lambda = s -> str.isEmpty();

System.out.println(methodRef.check()); // true
```

Dado que el método en String es un método de instancia, llamamos a la referencia, al método en una instancia de la clase String. 

Si bien todas las **referencias a métodos** se pueden **convertir a lambdas**, lo contrario no siempre se cumple. 

### Llamando a métodos de instancia en un parámetro

```java
interface StringParameterChecker{
    boolean check(String text);
}
```

Implementamos la funcionalidad de la siguiente manera

```java
StringParameterChecker methodRef = str::isEmpty;
StringParameterChecker lambda = s -> str.isEmpty();

System.out.println(methodRef.check("Zoo")); // false
```

