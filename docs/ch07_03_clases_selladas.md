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

