# Cap. 07 - Beyond Classes

## Enumeraciones

En programación, es común tener variables con un grupo específico de valores como días de la semana o estaciones del año, una enumeración es un conjunto fijo de constantes.

En una enumeración simple el `;` al final es opcional.

Las enumeraciones se consideran constantes por lo que se escriben usando el formato SNAKE_CASE.

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

No se puede comparar un entero con un valor de enumeración, ya que las enumeraciones son un propio tipo y los enteros son primitivos.

Al trabajar con código antiguo podemos obtener el valor de una enumeración pasando una cadena como parámetro al método `valueOf()`, el cual devuelve una instancia de la enumeración correspondiente.

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

### Usando enumeraciones con sentencias SWITCH

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

### Agregando constructores, campos y métodos a las enumeraciones

Una enumeración básica solo tiene valores, una compleja puede tener elementos adicionales.

```java
// La siguiente enumeración se crea porque se quiere tener un seguimiento de los patrones de tráfico por estación.

public enum Season {
    WINTER("Low"), SPRING("Medium"), SUMMER("High"), AUTUMN("Medium");
    
    private final String expectedVisitors;
    
    private Season(String expectedVisitors){
        this.expectedVisitors = expectedVisitors;
    }
    
    public void printExpectedVisitors() {
        System.out.println(expectedVisitors);
    }
}

// La línea 2 define los valores de enumeración y termina con `;` ya que hay más información.

// La línea 4 tiene la variable de instancia como `private` y `final` para que la propiedad no se pueda modificar, 
// ya que es lo correcto porque las enumeraciones son inmutables y solo debe haber una en la JVM.

// Todos los constructores de enumeración son implícitamente privados (ya que no se puede extender una enumeración, 
// los constructores solo se pueden llamar desde dentro y si no es privado no se compilará).

// Los parentesis en la línea 2 son llamadas al constructor pero sin la palabra clave `new`. La primera vez que solicitamos 
// cualquiera de los valores de la enumeración, Java construye todos los valores, después simplemente devuelve los valores construidos.
```

```java
// ¿Por qué no se imprime nada para o2?
public enum OnlyOne {
    ONCE(true);
    private OnlyOne(boolean b) {
        System.out.println("construccion");
    }
}

public class PrintTheOne {
    public static void main(String[] args) {
        System.out.println("inicio");
        OnlyOne o1 = OnlyOne.ONCE; // Imprime: "construccion" porque se ejecuto el constructor de la enumeración
        OnlyOne o2 = OnlyOne.ONCE; // No imprime nada, ya que solo se hace referencia al valor ya creado y no se vuelve a ejecutar el constructor
        System.out.println("fin");
    }
}
```
Como llamamos a un método de una enumeración, se puede usar el nombre de la enumeración seguido del método.

```java
Season.SUMMER.printExpectedVisitors(); // Imprime: High
```
También es posible definir diferentes métodos para cada enumeración

```java
// Que paso, parece una clase abstracta y un montón de subclases. La enumeración tiene definido un método abstracto. 
// Esto significa que cada valor de enumeración debe implementar este método sino se tiene un error de compilación.

public enum Season {
    WINTER {
        public String getHours() {
            return "9am - 5pm";
        }
    },
    SPRING {
        public String getHours() {
            return "8am - 6pm";
        }
    },
    SUMMER {
        public String getHours() {
            return "10am - 8pm";
        }
    },
    FALL {
        public String getHours() {
            return "9am - 7pm";
        }
    };
    public abstract String getHours(); // Método abstracto que cada enumeración debe implementar
}

// Que pasa si queremos que todos y cada uno de los valores de la enumeración tengan un método, 
// podemos crear el método para el que se quieran. Solo codificamos los casos especiales y dejamos que los demás 
// usen la implementación proporcionada por la enumeración. 

public enum Season {
    WINTER {
        public String getHours() {
            return "9am - 5pm";
        }
    },
    SUMMER {
        public String getHours() {
            return "8am - 6pm";
        }
    },
    SPRING, FALL {
        public String getHours() {
            return "10am - 8pm";
        }
    }
}

// Una enumeración puede incluso implementar una interfaz, ya que esto solo requiere sobrecargar métodos abstractos.

public interface Weather { int getAverageTemperature(); }
public enum Season implements Weather {
    WINTER, SPRING, SUMMER, FALL;
    public int getAverageTemperature() {
        return 30;
    }
}

// El hecho de que una enumeración pueda tener muchos métodos nos signifiica que deba tenerlos. 
// Intenta que tus enumeraciones sean simples.
```

La lista de valores de enumeración siempre van la inicio.
