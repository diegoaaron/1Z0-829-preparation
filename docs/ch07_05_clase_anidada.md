# Cap. 07 - Beyond Classes

## Clases anidadas - Nested Classes

Una clase anidada es una clase que se define dentro de otra clase y puede ser de cuatro tipos.

* Clase interna: Un tipo no estático definido en el nivel de miembro de una clase
* Clase anidada estática: Un tipo estático definido en el nivel de miembro de una clase
* Clase local: Una clase definida dentro del cuerpo de un método
* Clase anónima: Un caso especial de una clase local que no tiene nombre.

Entre sus beneficios está la capacidad de definir clases auxiliares y restringir su acceso, mejora la encapsulación, limpieza y legibilidad de código.

#### Definiendo una clase interna

Una clase interna "inner class" o clase interna miembro "member inner class" es un tipo no estático definido en el nivel de miembro de una clase (el mismo nivel que los métodos, las variables de instancia y los constructores). Debido a que no son tipos de nivel superior, pueden usar cualquiera de los cuatro niveles de acceso.

* Se pueden declarar públicas, protegidas, de paquete o privadas
* Pueden extender una clase e implementar interfaces
* Pueden marcarse como abstracto o final
* Puede acceder a los miembros de la clase externa, incluyendo los miembros privados

```java
// La linea "greet(greeting);" muestra que la clase interna simplemente se refiere a un "greeting" como si estuviera disponible en la clase Room. 
// Esto funciona porque, de hecho, esta disponible. Aunque la variabl es privada, se accede a ella dentro de esa misma clase.

// Dado que una clase interna no es estática, debe llamarse utilizando una instancia de clase. Esto significa que debe crear dos objetos. 
// La linea 45 crea un objeto externo Home, mientras que la 40 crea una instancia de Room, hay que entender 
// que la linea 40 no requiere una instancia explicita de Home porque es un método de instancia dentro de Home. 
//  Esto funciona porque enterRoom() es un método de instancia dentro de la clase Home. Tanto Room como enterRoom() son miembros de Home.

public class Home {
    private Strring greeting = "Hi"; // variable de clase externa
    
    protected class Room { // declaración de clase interna
        public int repeat = 3;
        public void enter() {
            for(int i = 0; i < repeat; i++) {
                greet(greeting);
            }
        }
        private static void greet(String message) {
            System.out.println(message);
        }
    }
    
    public void enterRoom() {
        var roomm = new Room(); // Instancia de clase interna
        room.enter();
    }
    
    public static void main(String[] args) {
        var home = new Home(); // crea una instancia de la clase externa
        home.enterRoom();
    }
}
```

Hay otra forma de crear una instancia de Room, esta sintaxis no se usa con frecuencia.

```java
public static void main(String[] args) {
    var home = new Home(); // crea una instancia de la clase externa
    var room = home.new Room(); // Instancia de clase interna
    room.enter();
}
```

Desde una instancia de Home podemos directamente crear la instancia de Room, la línea 64 se puede reducir más usando solo la 63 `var home = new Home().new Room().enter();`

**Referencia de miembros en una clase interna**

Las clases internas pueden tener los mismos nombres de variable que las clases externas, lo que hace que el alcance sea un poco confuso. Hay una forma especial de llamar a estos para indicar a qué variable quieres acceder.

```java
// Línea 102: Instancia la clase más externa de forma normal

// Línea 103: Usa la sintaxis "awkward" (incómoda) para instanciar B
// Punto clave: Podrías haber escrito simplemente B como tipo porque está disponible a nivel de miembro de A
// Java sabe dónde buscar la clase B
//
// Línea 104: Aquí SÍ es necesario especificar el tipo completo A.B.C
// C está "demasiado profunda" para que Java sepa dónde buscarla sin la ruta completa
//
// Las líneas del método allTheX() demuestran diferentes formas de acceder a variables

public class A {
    private int x = 10;
    class B {
        private int x = 20;
        class C {
            private int x = 30;
            public void allTheX() {
                System.out.println(x); // 30
                System.out.println(this.x); // 30
                System.out.println(B.this.x); // 20
                System.out.println(A.this.x); // 10
            }
        }
    }
    public static void main(String[] args) {
        A a = new A();
        A.B b = a.new B();
        A.B.C c = b.new C();
        c.allTheX();
    } 
}

// Primera llamada al constructor - SÍ compila:
// ¿Por qué compila?
// goHome() es un método de instancia (no static)
// Cuando se ejecuta, existe un objeto Fox (referenciado por this)
// La clase interna Den puede asociarse con esa instancia de Fox

// Segunda llamada al constructor - NO compila:
// ¿Por qué NO compila?
// visitFriend() es un método static
// Los métodos static no tienen acceso a this (no hay instancia)
// Den es una clase interna que necesita una instancia de Fox para existir
// Solución: Tendrías que crear explícitamente una instancia: new Fox().new Den()

// Tercera llamada al constructor - NO compila (doble problema):
// ¿Por qué NO compila?
// Problema de instancia: Aunque visitFox() es un método de instancia, es de la clase Squirrel, no de Fox
// Problema de visibilidad: Den es private en la clase Fox, por lo que no es accesible desde Squirrel

public class Fox {
    private class Den {}

    public void goHome() {
        new Den();
    }

    public static void visitFriend() {
        new Den(); // DOES NOT COMPILE
    }
}

public class Squirrel {
    public void visitFox() {
        new Den(); // DOES NOT COMPILE
    }
}
```

