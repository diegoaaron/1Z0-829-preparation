# Cap. 07 - Beyond Classes

## Clases Selladas - Sealing Classes

Una clase sellada es una clase que restringe qué otras clases pueden extenderla directamente. 

```java
public sealed class Bear permits Kodiak, Panda {} // solo Kodiak y Panda pueden extender Bear

public final class Kodiak extends Bear {} // Kodiak es una clase final, no puede ser extendida por otras clases

public non-sealed class Panda extends Bear {} //Otras clases sí pueden extender de Panda sin restricciones
```

**Palabras clave asociadas:**

- `sealed`: Define una clase sellada.
- `non-sealed`: Permite que una clase sellada sea extendida sin restricciones.
- `permits`: Especifica qué clases pueden extender una clase sellada.

```java
public class sealed Frog permits GlassFrog {} // no compila porque sealed esta despues de class
public final class GlassFrog extends Frog {} 

public abstract sealed class Wolf permits Timber{}
public final class Timber extends Wolf {}
public final class MyWolf extends Wolf {} // no compila porque MyWolf no esta en la lista de clases permitidas
```

Las clases selladas son declaradas comúnmente con el modificador `abastract` aunque no es obligatorio

### Compilación de clases selladas

Si intentamos compilar la siguiente clase sellada sin ningún código:

```java
// Penguin.java
package zoo;
public sealed class Penguin permits Emperor {}

// No compilará ya que una clase sellada debe declararse y compilarse en el mismo paquete que sus sub-clases directas
```

Si intentamos compilar la sub-clase declarada y la clase sellada en el mismo paquete:

```java
// Penguin.java
package zoo;
public sealed class Penguin permits Emperor {}

// Emperor.java
package zoo;
public final class Emperor {}

// No compilara porque Emperor no extiende Penguin
```

### Especificaciones para el modificador de las sub-clases

Mientras que en las interfaces se puede tener un cierto número de modificadores implícitos, en las clases selladas cada subclase debe tener uno de los siguientes modificadores: `final`, `non-sealed` o `abstract`. 

**Una sub-clase `final`**

Este modificador se puede aplicar directamente a una sub-clase sellada

Al igual que una clase regular, el modificador `final` impide que la subclase Gazelle se extienda más

```java
public sealed class Antelope permits Gazelle {}

public final class Gazelle extends Antelope {} 

public class George extends Gazelle {} // no compila no se puede extender una clase final
```

**Una sub-clase `sealed`**

Al utilizar `sealed` en una sub-clase debemos seguir las reglas de sellado inicial que consiste en definir su propia lista de sub-clases permitidas.

```java
public sealed class Mammal permits Equine {}

public sealed class Equine extends Mammal permits Zebra {}

public final class Zebra extends  Equine {}

// El compilador permitira que Mammal acepte objetos de tipo Zebra a pesar de que no este en el permits de Mammal
```

**Una sub-clase `non-sealed`**

Esto permite que una clase esté abierta a cualquier clase y sub-clases

```java
public sealed class Wolf permits Timber {}

public non-sealed class Timber extends Wolf {}

public class MyWolf extends Timber {}

public class MyFurryWolf extends MyWolf {}

// Para este ejemplo, MyFurryWolf se considera una sub-clase de Wolf gracias al polimorfismo (una instancia de una subclase puede ser tratada como una instancia de su superclase) ya que no-sealed permite que Timber sea extendida sin restricciones.
```

**Omitiendo la palabra clave `permits`**

Hasta ahora todos los ejemplos que has visto han requerido una cláusula `permits` al declarar una clase sellada, pero no siempre será necesario

```java
// Snake.java
public sealed class Snake {}

final class Cobra extends Snake {} // no se necesita porque estan en el mismo archivo

// Snake.java
public sealed class Snake {
    final class Cobra extends Snake {} // no se necesita porque es una clase anidada 
}

// Si de forma explicita en una clase sellada como en sus sub-clases anidadas usamos permits, deberemos referenciar al espacio de nombres de la clase principal

public sealed class Snake permits Snake.Cobra {
    final class Cobra extends Snake {} 
}
```

Necesitamos saber que una clase anidada es una clase definida dentro de otra clase y que la regla de omisión también se aplican a estas.

| Ubicación de las sub-clases directas     | palabra `permits`            |
|------------------------------------------|------------------------------|
| Archivo diferente al de la clase sellada | Requerido                    |
| En el mismo archivo que la clase sellada | Permitido, pero no requerido |
| Anidado dentro de la clase sellada       | Permitido, pero no requerido |

### Sellado de interfaces

Además de las clases, las interfaces también se pueden sellar. La idea es análoga a la de las clases, y se aplican muchas de las mismas reglas. Por ejemplo, la interfaz sellada debe aparecer en el mismo paquete o módulo con nombre que las clases o interfaces que la extienden o implementan directamente.

Una característica distintiva de una interfaz sellada es que la lista de permisos puede aplicarse a una clase que implementa la interfaz o a una interfaz que la extiende. 

```java
// interfaz sellada
public sealed interface Swims permits Duck, Swan, Floats {}

// Clases que implementan la interfaz sellada
public final class Duck implements Swims {}
public final class Swan implements Swims {}

// Interfaz que extiende la interfaz sellada
public non-sealed interface Floats extends Swims {}
```

Hay que tener en cuenta que las interfaces son implícitamente abstractas y no se pueden marcar como finales por lo cual las interfaces que extienden una interfaz sellada solo pueden ser selladas o no selladas.

**Revisando las reglas de las clases selladas**

* Las clases selladas se declaran con los modificadores `sealed` y `permits`. 
* Las clases selladas deben declararse en el mismo paquete o módulo que sus subclases directas. 
* Las subclases directas de las clases selladas se deben marcarse como `sealed`, `no-sealed` o `final`.  
* La cláusula `permits` es opcional si la clase sellada y sus subclases directas se declaran dentro del mismo archivo o si las subclases están anidadas dentro de la clase sellada. 
* Las interfaces se pueden sellar para limitar las clases que las implementan o las interfaces que las extienden. 
