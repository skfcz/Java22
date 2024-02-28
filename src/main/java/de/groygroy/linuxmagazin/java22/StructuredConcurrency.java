package de.groygroy.linuxmagazin.java22;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.StructuredTaskScope;
import java.util.function.Supplier;

public class StructuredConcurrency {

    public static void main(String[] args) {

        // Läd die Daten für zwei Wetterstationen vom DWD,
        // nur wenn beide Downloads der Wetterdaten erfolgreich sind, wird die Weiterverarbeitung angestoßen

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Supplier<String> fuhlsbüttelSup = scope.fork(() -> download(new URL("https://opendata.dwd.de/climate_environment/CDC/observations_germany/climate/subdaily/standard_format/kl_10147_00_akt.txt")));
            Supplier<String> cuxhavenSup = scope.fork(() -> download(new URL("https://opendata.dwd.de/climate_environment/CDC/observations_germany/climate/subdaily/standard_format/kl_10131_00_akt.txt")));

            scope.join()                // Warte auf beide Unteraufgaben
                    .throwIfFailed();   // und wirf einen Fehler wenn das nicht klappt

            // Hier kann die Auswertung geschehen
            System.out.println("beide Stationen liegen vor");
            System.out.println("fuhlsbüttel " + fuhlsbüttelSup.get());
            System.out.println("cuxhaven " + cuxhavenSup.get());

        } catch (Exception exp) {
            System.err.println("da ging was schief " + exp);
            System.exit(1);
        }

    }


    private static String download(URL url) throws IOException {

        BufferedInputStream in = new BufferedInputStream(url.openStream());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte dataBuffer[] = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
            bos.write(dataBuffer, 0, bytesRead);
        }

        return bos.toString();

    }
}
