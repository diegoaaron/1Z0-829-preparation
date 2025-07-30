**Interfaces** 

````java
public interface ZooTrainTour {
    abstract int getTrainName();
    private static void ride(){}
    default void playHorn() {
        getTrainName();
        ride();
    }
    public static void slowDown() {
        playHorn();
    }
    static void speedUp() {
        ride();
    }
}

// El método ride() es privado y estático, por lo que se puede acceder mediante cualquier método predeterminado o estático dentro de la interfaz.
// El método getTrainName() es abstracto, por lo que se puede acceder a él mediante un método predeterminado asociado con la instancia.
// El método slowDown() es estático y no puede llamar a un método predeterminado o privado como playHorn(), sin un objeto de referencia expicito por lo cual no compilara.  
````

