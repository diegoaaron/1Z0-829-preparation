# Cap. 07 - Beyond Classes

## Clases Selladas  - Sealing Classes

Una clase sellada es una clase que restringe qué otras clases pueden extenderla directamente. 

```java
public sealed class Bear permits Kodiak, Panda {} // solo Kodiak y Panda pueden extender Bear

public final class Kodiak extends Bear {} // Kodiak es una clase final, no puede ser extendida por otras clases

public non-sealed class Panda extends Bear {} //Otras clases sí pueden extender de Panda sin restricciones
```

#### Palabras clave asociadas

