# Cap. 07 - Beyond Classes

## Registros - Records

Una clase sellada es una clase que restringe qué otras clases pueden extenderla directamente. 

Antes de avanzar debemos entender el concepto de encapsulación, para eso debemos saber que es un **POJO "Plan Old Java Object"** el cual es una clase usada pra modelar y pasar datos sencillos.

```java
public class Crane {
    int numbersEggs;
    String name;
    
    public Crane(int numbersEggs, String name) {
        this.numbersEggs = numbersEggs;
        this.name = name;
    }
}

// Los campos son de acceso al paquete, lo que signfica que desde otra clase del mismo paquete podrían cambiar valores y crear datos no válidos como: 

public class Poacher {
    public void badActor() {
        var mother = new Crane(5, "Cathy");
        mother.numbersEggs = -100;
    }
}

// Esto claramente no es bueno por lo que podemos recurrir a la encapsulación, es una forma de proteger a los miembros de 
// la clase al restringir el acceso a ellos. la encapsulación nos permite modificar los métodos y el comportamiento de la 
// clase posteriormente sin que alguien tenga acceso directo a una variable de instancia dentro de la clase. Por ejemplo, 
// podemos cambiar el tipo de datos de una variable de instancia, pero manteenr las mismas firmas del método con lo 
// cual mantenemos el control total sobre el funcionamiento interno de la clase. Por ejemplo la clase Crane podría quedar así:

public final class Crane {
    private final int numbersEggs;
    private final String name;

    public Crane(int numbersEggs, String name) {
        if (numbersEggs >= 0) this.numbersEggs = numbersEggs;
        else throw new IllegalArgumentException();
        this.name = name;
    }

    public int getNumbersEggs() {
        return numbersEggs;
    }

    public String getName() {
        return name;
    }
}

// Esto nos asegura tener una clase inmutable (gracias a la definición final en la clase y variables, la ausencia de 
// setter y tipos de datos int y string) y encapsulada (gracias a la definición private que no deja un acceso directo 
// a las vairables desde afuera, final para que no sean reasiganda  una valor despues de inicializarlo, 
// metodos getter que dan un acceso controlado a las variables, final en la clase que protege la implementación de herencia) 


// En el siguiente ejemplo, podemos ver como siempre que hagamos privadas las variables de instancia la clase se encapsulara bien

public class Vet {
    private String name = "Dr. Smith";
    private int yearsOfExperience = 10;
}
```

#### Definiendo un registro

```java
public record Crane(int numbersEggs, String name) {}
```

La línea anterior crea un registro con el cual el compilador inserta la implementación de las variables asi como los métodos de objeto equals(), hashCode() y toString().

Ya que por defecto el compilador inyecta un constructor con el mismo orden de los parámetros al crear el registro, si se crea una instancia sin respetar ese orden u omitiendo un valor se genera un error de compilación.

```java
var mommy = new Crane(4, "Cathy"); // Correcto
System.out.println(mommy.numbersEggs()); // 4
System.out.println(mommy.name()); // Cathy

var mommy = new Crane("Luis", 5); // Error
var mommy = new Crane(5); // Error
```

Elementos agregados automáticamente por el compilador al definir un registro:

* Constructor: un constructor público con los parámetros en el mismo orden que se definieron en el registro.
* Métodos de acceso: métodos públicos con el mismo nombre que los componentes del registro (para cada variable).
* equals(): un método que compara dos instancias del registro para ver si son iguales.
* hashCode(): un método que devuelve un valor hash para la instancia del registro.
* toString(): un método que devuelve una representación de cadena de la instancia del registro.

```java
var father = new Crane(3, "Bob");
System.out.println(father); // Crane[numbersEggs=3, name=Bob]

var copy = new Crane(3, "Bob");
System.out.println(copy); // Crane[numbersEggs=3, name=Bob]

System.out.println(father.equals(copy)); // true (porque son records y tienen el mismo contenido inmutable)
System.out.println(father.hashCode() + "," + copy.hashCode()); // 1007, 1007
```

Es permitido crear un registro sin ningún componente, aunque no es muy útil.

```java
public record Empty() {}
```

#### Entendiendo la inmutabilidad de los registros

Ya que los registros no tienen setters y no se pueden modificar de ninguna forma después de su creación, la unica opción es crear otro objeto.

```java
var cusin = new Crane(2, "Anna");
var friend = new Crane(cousin.numberEggs(), "Anna");
```

Teniendo como analogía que las interfaces son implícitamente abstractas, los registros son implícitamente finales. El modificador final es opcional.

```java
public final record Crane(int numbersEggs, String name) {}
```

También al igual que las enumeraciones eso significa que no puedes extender ni heredar un registro.

```java
public record BlueCrane() extends Crane {} // No compila
```

Al igual que las enumeraciones, un registro puede implementar una interfaz regular o sellada siempre que implemente todos los métodos abstractos.

```java
public interface Bird {}
public record Crane(int numbersEggs, String name) implements Bird {}
```

#### Creando constructores 

Podemos definir la creación de un constructor dentro del registro de forma denominado;

**El constructor largo:**

El cual el compilador lo inserta automáticamente, ya que utiliza todos los campos del registro:

```java
public record Crane(int numbersEggs, String name) {
    public Crane(int numbersEggs, String name) {
        if (numbersEggs < 0) {
            throw new IllegalArgumentException("Number of eggs cannot be negative");
        }
        this.numbersEggs = numbersEggs;
        this.name = name;
    }
}
```

El compilador no insertará un constructor si define uno con la misma lista de parámetros, pero internamente no se incluyen todos los campos del registro.

```java
public record Crane(int numbersEggs, String name) {
    public Crane(int numbersEggs, String name) {} // No compila, ya que no se incluyen todos los campos del registro
}
```

**El constructor corto:**

Este tipo es util, ya que permite que los registros procesen la validación y las transformaciones. No requiere que nombremos a los parámetros. 

```java
public record Crane(int numbersEggs, String name) {
    public Crane {
        if(numbersEggs <0) {
            throw new IllegalArgumentException("Number of eggs cannot be negative");
        }
        name = name.toUpperCase();
    }
}
```

Si bien los constructores compactos pueden modificar parámetros del constructor, no pueden sobreescribirlos los campos finales (ya que aún no existen al momento de la ejecución del constructor, sino después de completar esta parte).

```java
public record Crane(int numbersEggs, String name) {
    public Crane {
        this.numbersEggs =  10; // No compila
    }
}

public record Crane(int numbersEggs, String name) {
    public Crane {
        numbersEggs =  10; // Si compila
    }
}
```

**Sobrecarga de constructores**

También podemos crear constructores sobrecargados que tomen una lista de parámetros diferentes, esto se relaciona directamente con los constructores de forma larga.

```java
// La primera línea de un constructor sobrecargado deb ser una llamada explicita a otro constructor a través de this() 
// si no hay otro se llama al constructor largo.

public record Crane(int numbersEggs, String name) {
    public Crane(String firstName, String lastName) {
        this(0, firstName + " " + lastName); // Llama al constructor largo
    }
}

// A diferencia de los constructores compactos, solo se puede transformar los datos en la primera línea despues todos los campos ya estaran asignados y el objeto es inmutable. 

public record Crane(int numbersEggs, String name) {
    public Crane(String firstName, String lastName) {
        this(0, firstName + " " + lastName); // Llama al constructor largo
        numbersEggs = 10; // no afecta (aplica al parametro pero no al campo de instancia) 
        this.numbersEggs = 20; // No compila, ya que no se puede reasignar un campo final
    }
}

// Tampoco se pueden declarar dos constructores que se llame infinitamente entre si. 

public record Crane(int numbersEggs, String name) {
    public Crane(String name) {
        this(1); // no compila
    }
    public Crane(int numbersEggs) {
        this(""); // no compila
    }
}
```

#### Personalizando registros

Los registros aceptan muchos de los modificadores que se pueden aplicar a las clases como: 

* Constructores sobrecargados y compactos
* Métodos de instancia, incluyendo la anulación de cualquier método proporcionado (equals(), hashCode(), toString())
* Clases anidadas, interfaces, anotaciones, enumeraciones y registros

```java
// Como ejemplo lo siguiente anula 2 metodos de instancia utilizando la anotación opcional @Override

public record Crane(int numbersEggs, String name) {
    @Override
    public String numbersEggs() {
        return 10;
    }

    @Override
    public String toString() {
        return name;
    }
}
```

Si bien puedo agregar métodos, campos estáticos y otros tipos de datos, no se puede agregar campos de instancia fuera de la declaración de registro incluso si son privados, ya que esto anulan el propósito de crear un registro y podría romper la inmutabilidad que lo caracteriza.

```java
// Como ejemplo lo siguiente anula 2 metodos de instancia utilizando la anotación opcional @Override

public record Crane(int numbersEggs, String name) {
    private static int type = 10;
    public int size; // No compila, ya que no se pueden agregar campos de instancia fuera de la declaración del registro
    private boolean friendly; // No compila, ya que no se pueden agregar campos de instancia fuera de la declaración del registro
}
```