package de.groygroy.linuxmagazin.java22;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Array;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Gatherer;
import java.util.stream.Gatherers;
import java.util.stream.Stream;

public class StreamGatherer {

    public static void main(String... args) {

        var urstrom = ströme().toList();

        // berechnet den Mittelwert über alle Werte [0.1, 0.9]
        var mittel = ströme().
                filter(d -> d >= 0.1).filter(d -> d < 0.9). // intermediär
                mapToDouble(d -> d). // intermediär
                average(). // intermediär
                getAsDouble();
        System.out.println("mittel " + mittel);

        var mittelwerte =
                ströme().gather(Gatherers.windowFixed(25)).
                        map( bereich -> bereich.stream().mapToDouble(d -> d).average().getAsDouble()).toList();

        System.out.println(mittelwerte);

        // ein einfacher Integrator, der Werte vor der Weitergabe ändert
        Gatherer.Integrator<Void, Double, Double> elementFunktion =
                (status, eingabe, downstream) -> {
                    var neu = Math.pow(eingabe, 2);
                    downstream.push(neu);
                    return eingabe > -0.95;
                };

        Gatherer<Double, Void, Double> gatherer = Gatherer.of(elementFunktion);
        var quadriert = ströme().gather(gatherer).toList();

        System.out.println(quadriert);

        // Berechnung eines gleitenden Mittels mit einem
        // aufwändigeren Gatherer
        Gatherer<Double, List<Double>, Double> gmg= Gatherer.ofSequential(
                // Initializer
                () -> new ArrayList<Double>(),
                // Integrator
                Gatherer.Integrator.of((liste, element, downstream) -> {
                    liste.add(element);
                    if (liste.size() > 10) {
                        liste.removeFirst();
                    }

                    double mw = 0.0;
                    for (double d : liste) {
                        mw += d;
                    }
                    mw /= liste.size();


                    downstream.push(mw);
                    return true;
                })
        );

        var gleitendesMittel = ströme().gather(gmg).toList();

        System.out.println(gleitendesMittel);


        schreiben( urstrom, mittelwerte, quadriert, gleitendesMittel);

    }


    private static void schreiben(List<Double> ... listen) {

        NumberFormat decform = NumberFormat.getInstance(Locale.ENGLISH);
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("test.csv"))) {

            int index = 0;
            boolean amEnde;
            do {
                amEnde =true;

                for(int spalte = 0; spalte < listen.length;spalte++) {
                    var liste = listen[spalte];
                    if ( liste.size() > index) {
                        amEnde=false;
                        writer.write( decform.format( liste.get(index)));
                        writer.write(" ");

                    } else {
                        writer.write("? ");
                    }
                }
                writer.write("\n");
                index++;

            } while(!amEnde);


        } catch (Exception exp) {
            System.err.println("da lief was schief :" + exp);
        }
    }

    /**
     * Erzeugt einen Stream, der 314 doubles
     * in einer verrauschten Cosinus-Kurve
     * enthält
     */
    private static Stream<Double> ströme() {

        return Stream.iterate(0, i -> i + 1)
                .limit(314)
                .map(i -> Math.cos(i * 0.005 * Math.PI + 0.3 * Math.random()));

    }
}
