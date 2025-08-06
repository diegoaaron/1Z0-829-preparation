# Cap. 07 - Beyond Classes

## Clases Selladas  - Sealing Classes

Una clase sellada es una clase que restringe qué otras clases pueden extenderla directamente. 

```java
public sealed class Bear permits Kodiak, Panda {} // solo Kodiak y Panda pueden extender Bear

public final class Kodiak extends Bear {} // Kodiak es una clase final, no puede ser extendida por otras clases

public non-sealed class Panda extends Bear {} //Otras clases sí pueden extender de Panda sin restricciones
```

#### Palabras clave asociadas

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

_Las clases selladas son declaradas comúnmente con el modificador `abastract` aunque no es obligatorio_

#### Compilación de clases selladas

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

#### Especificaciones para el modificador de las sub-clases

Mientras que en las interfaces se puede tener un cierto número de modificadores implícitos, en las clases selladas cada subclase debe tener uno de los siguientes modificadores: final, non-sealed o abstract. 

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

Hasta ahora todos los ejemplos que has visto han requerido una cláusula permits al declarar una clase sellada, pero no siempre será necesario

```java
// Snake.java
public sealed class Snake {}

final class Cobra extends Snake {} // no se necesita porque estan en el mismo archivo

// Snake.java
public sealed class Snake {
    final class Cobra extends Snake {} // no se necesita porque es una clase anidada 
}

// Si de forma explicitamo en una clase sellada con sus sub-clases anidadas usamos permits, deberemos referenciar el espacio de nombres de la clase principal

public sealed class Snake permits Snake.Cobra {
    final class Cobra extends Snake {} 
}
```

