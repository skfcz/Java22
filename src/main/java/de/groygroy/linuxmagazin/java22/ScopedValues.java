package de.groygroy.linuxmagazin.java22;

public class ScopedValues {

    public final static ScopedValue<String> SCOPED_VALUE = ScopedValue.newInstance();

    public static void main(String... args) {

        System.out.println("-- Zugriff mit Scope --");
        String[] anwendende={"Lu-Tze", "Lobsang Ludd", "Susan Sto Helit"};

        for (var anwender : anwendende) {
            // Variable im neuen Scope setzen und Code starten
            ScopedValue.where(SCOPED_VALUE, anwender).run(new Aufgabe());
        }
        System.out.println("-- Zugriff ohne Scope --");
        new Thread(new Aufgabe()).start();

    }

    static class Aufgabe implements Runnable {
        @Override
        public void run() {
            try {
                // ... diverse Layer
                System.out.println("ScopedValue : " + ScopedValues.SCOPED_VALUE.get());
            } catch (Exception exp) {
                System.err.println("Fehler beim Zugriff :" + exp);
            }
        }
    }
}
