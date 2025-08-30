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
// Aunque el objeto Lemur se haya asignado a una referencia con un tipo diferente, el objeto en sí no ha cambiado 
// y sigue existiendo como un objeto Lemur en memoria. Lo que ha cambiado, entonces, es nuestra capacidad para 
// acceder a los métodos dentro de la clase Lemur con la referencia lemurAsObject. Sin una conversión explicita a Lemur, 
// ya no tenemos acceso a las propiedades del objeto Lemur.

Lemur lemur = new Lemur();
Object lemurAsObject = lemur;
```

Podemos resumir este principio con las siguientes dos reglas:

1. El tipo de objeto determina qué propiedades existen dentro del objeto en memoria.
2. El tipo de referencia al objeto determina qué métodos y variables son accesibles para el programa Java. 

Por lo tanto, se deduce que cambiar correctamente una referencia de un objeto a un nuevo tipo de referencia puede dar acceso a nuevas propiedades del objeto; pero recuerde que esas propiedades existían antes de que ocurriera el cambio de referencia. 

#### Casting (conversión) de objetos

En el ejemplo anterior creamos una única instancia de un objeto Lemur y accedimos a él mediante referencias de superclase e interfaz. Sin embargo, una vez que cambiamos el tipo de referencia, perdimos el acceso a miembros más específicos definidos en la subclase que aún existen dentro del objeto. Podemos recuperar esas referencias volviendo a convertir el objeto a la subclase específica de la que proviene:

```java
Lemur lemur = new Lemur();

Primate primate = lemur; // es implicito la conversión a la superclase

Lemur lemur2 = (Lemur) primate; // explícito la conversión al subtipo

Lemur lemur3 = primate; // No compila (falta el casteo)
```

Al convertir objetos, no se necesita un operador de conversión si se convierte a un supertipo heredado (primer ejemplo). Esto se conoce como conversión implícita y se aplica a las clases o interfaces que hereda el objeto. Alternativamente, si se desea acceder a un subtipo de la referencia actual (segundo ejemplo), se debe realizar una conversión explícita con un tipo compatible. Si el objeto subyacente no es compatible con el tipo (tercer ejemplo), se lanzará una `ClassCastException` en tiempo de ejecución.

Resumiendo las reglas para el examen:

* Convertir una referencia de un subtipo a un supertipo no requiere una conversión explícita.
* Convertir una referencia de un supertipo a un subtipo requiere una conversión explícita.
* En tiempo de ejecución, una conversión no válida de una referencia a un tipo incompatible da como resultado que se lance una `ClassCastExcpetion`.
* El compilador no permite conversiones a tipos no relacionados

#### Casting (conversión) no permitida

Esta última regla es un poco más complicada. En el ejemplo anterior, pudimos convertir una referencia `Primate` a una referencia `Lemur` porque `Lemur` es una subclase de `Primate` y, por lo tanto, está relacionada. 

```java
// En este ejemplo, las clases Fish y Bird no están relacionadas a través de ninguna 
// jerarquía de clases que el compilador conozca; por lo tanto, el código no se compilará. 

public class Bird {}

public class Fish {
    public static void main(String[] args) {
        Fish fish = new Fish();
        Bird bird = (Bird) fish; // No compila: tipos no relacionados
    }
}
```

#### Casting (conversión) de interfaces

Si bien el compilador puede aplicar reglas sobre la conversión a tipos no relacionados para las clases, no siempre puede hacer lo mismo para las interfaces. Recuerde que las instancias admiten herencia múltiple, lo que limita lo que el compilador puede razonar sobre ellas. Si bien una clase determinada puede no implementar una interfaz, es posible que alguna subclase si la implemente. Cuando se mantiene una referencia a una clase en particular, el compilador no sabe qué subtipo específico está manteniendo. 

```java
// En este programa, se creó un objeto Wolf y luego se asigna a un tipo de referencia Wolf en la línea 140. 
// Con las interfaces, el compilador tiene una capacidad limitada para aplicar muchas reglas porque, aunque un tipo 
// de referencia no implemente una interfaz, una de sus subclases si podría. Por lo tanto, permite la conversión no 
// válida al tipo de referencia Dog en la línea 141, aunque Dog y Wolf no estén relacionados. Aunque el código compile, 
// todavía lanza una ClassCastException en tiempo de ejecución. 

interface Canine {}
interface Dogo {}
class Wolf implements Canine {}

public class BadCats{
    public static void main(String[] args) {
        Wolf wolfy = new Wolf();
        Dogo badWolf = (Dogo) wolfy;
    }
}
```

Dejando de lado esta limitación, el compilador puede aplicar una regla sobre la conversión de interfaces. El compilador no permite una conversión de una referencia de interfaz a una referencia de objeto si el tipo de objeto no puede implementar la interfaz (por ejemplo la clase está marcada como final). 

Por ejemplo, si la clase `Wolf` está marcada como `final` en la línea 136, la línea 141 ya no se compila. El compilador reconoce que no hay subclases posibles de Wolf capaces de implementar la interfaz Dog.

#### El operador `intanceof`

El operador `instanceof` se puede usar para verificar si un objeto pertenece a una clase o interfaz en particular y para evitar una `ClassCastException` en tiempo de ejecución.

```java
class Rodent {}

public class Capybara extends  Rodent {
    public static void main(String[] args) {
        Rodent rodent = new Rodent();
        var capybara = (Capybara) rodent; // Lanzará ClassCastException en tiempo de ejecución
    }
}

// podemos cambiar la línea 160 por:
// Ahora el fragmento de código no lanza una excepción en tiempo de ejecución y realiza la conversión solo si el operador instanceof tiene éxito. 

if (rodent instanceof Capybara) {
    // Do stuff
}
```

Asi como el compilador no permite convertir un objeto a tipos no relacionados, tampoco permite que instanceof se use con tipos no relacionados. 

Podemos demostrar esto con nuestras clases `Bird` y `Fish` no relacionadas: 

```java
public class Bird {}

public class Fish {
    public static void main(String[] args) {
        Fish fish = new Fish();
        if (fish instanceof Bird) { // No compila
            // Do stuff
        }
    }
}
```

#### Polimorfismo y sobrecarga de métodos

En Java, el polimorfismo establece que cuando se anula un método, se reemplazan todas las llamadas a él, incluso las definidas en la clase padre. 

```java
class Penguin {
    public int getHeight() {
        return 3;
    }
    
    public void printInfo() {
        System.out.print(this.getHeight());
    }
}

public class EmperorPenguin extends Penguin {
    public int getHeight() {
        return 8;
    }
    
    public static void main(String[] args) {
        new EmperorPenguin().printInfo(); // 8
    }
}
```

Si dijiste 8, entonces estás en camino a entender el polimorfismo. Este ejemplo, el objeto sobre el que opera en memoria es un `EmperorPenguin`. El método `getHeight()` se anula en la subclase, lo que significa que todas ls llamadas a este se reemplazan en tiempo de ejecución. 

A pesar de que `printInfo()` está definido en la clase `Penguin`, llamar a `getHeight()` en el objeto, llama al método asociado con el objeto preciso en memoria, no al tipo de referencia actual donde se llama. Incluso usando la referencia `this`, que es opcional en este ejemplo, no se llama a la versión padre porque el método ha sido reemplazado.

La capacidad del polimorfismo para reemplazar métodos en tiempo de ejecución mediante la anulación es una de las propiedades más importantes de Java. Permite crear modelos de herencia complejos con subclases que tienen su propia implementación personalizada de métodos anulados. 

También significa que la clase padre no necesita actualizarse para usar el método personalizado o anulado. Si el método se anula correctamente, entonces la versión anulada se usara en todos los lugares donde se llame. Recuerda que puedes optar por limitar el comportamiento polimórfico marcando los métodos como finales, lo que evita que una subclase lo sobrescriba.

#### Sobrecarga vs. ocultación de miembros

Mientras que la anulación de métodos reemplaza el método en todos los lugares donde se llama, la ocultación de métodos estáticos y variables no lo hace. 

Estrictamente hablando, ocultar miembros no es una forma de polimorfismo, ya que los métodos y las variables mantienen sus propiedades individuales. 

A diferencia de la anulación de métodos, ocultar miembros es muy sensible al tipo de referencia y la ubicación donde se utiliza el miembro. 

```java
class Penguin {
    public static int getHeight() {
        return 3;
    }
    
    public void printInfo() {
        System.out.print(this.getHeight());
    }
}

public class CrestedPenguin extends Penguin {
    public static int getHeight() {
        return 8;
    }
    
    public static void main(String... fish) {
        new CrestedPenguin().printInfo(); // 3
    }
}
```

El ejemplo de `CrestedPenguin` es casi idéntico a nuestro ejemplo anterior de `EmperorPenguin`, aunque como probablemente ya habrás adivinado, imprime 3 en lugar de 8. 

El método `getHeight()` es estático y, por lo tanto, está oculto, no sobrescrito. El resultado es que llamar a `getHeight()` en `CrestedPenguin` devuelve un valor diferente que llamarlo en `Penguin`, incluso si el objeto subyacente es el mismo. 

Contrasta esto con la sobrescritura de un método, donde devuelve el mismo valor para un objeto independientemente de en qué clase se llame.

¿Qué pasa con el hecho de que usamos `this` para acceder a un método estático en `this.getHeight()`? 

Aunque se te permite usar una referencia de instancia para acceder a una variable o método estático, hacerlo a menudo se desaconseja. 

El compilador te advertirá cuando accedas a miembros estáticos de una manera no estática. En este caso, la referencia this no tuvo impacto en la salida del programa.

```java
class Marsupial {
    protected int age = 2;
    public static boolean isBiped() {
        return false;
    }
}

public class Kangaroo extends Marsupial {
    protected int age = 6;
    public static boolean isBiped() {
        return true;
    }
    
    public static void main(String[] args) {
        Kangaroo joey = new Kangaroo();
        Marsupial moey = joey;
        System.out.println(joey.isBiped());
        System.out.println(moey.isBiped());
        System.out.println(joey.age);
        System.out.println(moey.age);
    }
}

// true
// false
// 6
// 2
```

En este ejemplo, solo un objeto (de tipo Kangaroo) es creado y almacenado en memoria. 

Dado que los métodos estáticos solo pueden ser ocultos, no sobrescritos, Java usa el tipo de referencia para determinar qué versión de `isBiped()` debe ser llamada, resultando en que `joey.isBiped()` imprima true y `moey.isBiped()` imprima false.

Del mismo modo, la variable age está oculta, no sobrescrita, por lo que el tipo de referencia se usa para determinar qué valor mostrar. Esto resulta en que `joey.age` devuelva 6 y `moey.age` devuelva 2.
