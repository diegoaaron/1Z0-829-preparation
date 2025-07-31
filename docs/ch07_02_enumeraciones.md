# Cap. 07 - Beyond Classes

## Enumeraciones

En programación, es común tener variables con un grupo específico de valores como días de la semana o estaciones del año, una enumeración es un conjunto fijo de constantes.

_En una enumeración simple el `;` al final es opcional._

_Las enumeraciones se consideran constantes por lo que se escriben usando el formato SNAKE_CASE._

* Las enumeraciones son como constantes estáticas y finales, ya que se inicializan solo una vez en la JVM
* No se puede extender una enumeración, sus valores son fijos. 

```java
public enum Season {
    VERANO, OTONO, INVIERNO, PRIMAVERA;
}

var s = Season.VERANO;
System.out.println(s); // Imprime: VERANO
System.out.println(Season.VERANO); // Imprime: VERANO
System.out.println(s == Season.VERANO); // Imprime: true

public enum NuevasSeason extends Season {} // No compila, ya que no se puede extender una enumeración
```

Las enumeraciones tienen métodos implícitos como `values()`, `name()`, `ordinal()` y  `valueOf()`

_No se puede comparar un entero con un valor de enumeración, ya que las enumeraciones son un propio tipo y los enteros son primitivos._

_Al trabajar con código antiguo podemos obtener el valor de una enumeración pasando una cadena como parámetro al método `valueOf()`, el cual devuelve una instancia de la enumeración correspondiente._

```java
for (var season : Season.values()) {
    System.out.println(season.name() + " - " + season.ordinal());
}

// Imprime: VERANO - 0
// Imprime: OTONO - 1
// Imprime: INVIERNO - 2
// Imprime: PRIMAVERA - 3

if(Season.VERANO == 2) {} // No compila, ya que no se puede comparar un valor de enumeración con un entero

Season s = Season.valueOf("VERANO");  // Devuelve el objeto Season.VERANO
Season t = Season.valueOf("verano"); // Devuelve un error IllegalArgumentException ya que no existe un valor de enumeración "verano" en minusculas 
```

#### Usando enumeraciones con sentencias SWITCH

En un switch con enumeraciones, Java va a inferir que estás usando un tipo de enumeración por lo que se puede usar directamente el nombre de la enumeración en un switch.

```java
Season summer = Season.VERANO;

switch (summer) {
    case VERANO:
        System.out.println("Es verano");
        break;
    case OTONO:
        System.out.println("Es otoño");
        break;
    default:
        System.out.println("No es verano ni otoño");
        break;
        }
        
Season summer = Season.VERANO;
var message = switch (summer) {
    case Season.VERANO -> "Es verano"; // no compila, ya que no es necesario el nombre de la enumeración
    case 0 -> "Es otoño"; // No compila, ya que no se puede comparar un valor de enumeración con un entero
    default -> "No es verano ni otoño";
};
```

#### Agregando constructores, campos y métodos a las enumeraciones


