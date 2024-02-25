package de.groygroy.linuxmagazin.java22;

import java.util.List;

public class Unnamed {

    public static void main(String... args) {

        // Exception wird nicht benötigt,
        // muss aber im catch Statement stehen
        try {
            throw new IllegalArgumentException("demo");
        } catch (Exception _) {
            System.err.println("es trat ein Fehler auf");
        }

        // Pattern Matching for switch
        // Objekt wird nicht benötigt
        Form form = new Rechteck(1, 2);
        switch (form) {
            case Rechteck _ -> System.out.println("ja");
            case Kreis _ -> System.out.println("nein");
            case Form _ -> System.out.println("vielleicht");
        }

        // Record Patterns
        if (form instanceof Rechteck(_, int b)) {
            System.out.println("breite " + b);
        }

        // statische Codeanalyse ruhigstellen
        List<String> werte = List.of("a", "b", "c");
        var iterator = werte.iterator();
        // der erste Wert soll übersprungen werden
        var _ = iterator.next();
        while (iterator.hasNext()) {
            var wirdBenötigt = iterator.next();
        }

    }

    interface Form {
    }
    record Rechteck(int länge, int breite) implements Form {
    }
    record Kreis(int radius) implements Form {
    }

}
