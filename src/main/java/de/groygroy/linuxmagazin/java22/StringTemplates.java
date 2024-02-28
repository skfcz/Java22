package de.groygroy.linuxmagazin.java22;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;


public class StringTemplates {

    public static void main(String... args) {
        // Code bis Java 15 -- 21
        var name = "Erika Mustermann";
        var tag = new Date();

        var grussFmt = "Hallo %s";
        System.out.println(grussFmt.formatted(name));
        // Code mit Java 22
        System.out.println(STR."Hallo \{name}");

        // Code bis Java 15 -- 21
        var jsonFmt = "{\n  \"name\" : \"%s\",\n  \"tag\" : \"%tA %te. %tB\"\n}";
        System.out.println(jsonFmt.formatted(name, tag, tag, tag));

        // Code mit Java 22 und FMT
        var fp = FormatProcessor.create(Locale.GERMAN);
        System.out.println(fp."""
                {
                  "name": "\{name}",
                  "tag" : "%tA\{tag} %te\{tag} %tB\{tag}"                      
                }
                """);

        // Ein spezieller Processor, der direkt JSON zurückgibt und sich um Anführungszeichen kümmer
        StringTemplate.Processor<JSONObject, JSONException> JSON = template -> {
            String quote = "\"";
            List<Object> ersatz = new ArrayList<>();

            for (Object value : template.values()) {
                String roh = value != null ? value.toString() : "null";
                String formatiert = roh.replace("\"", "\\\"");
                System.out.println(roh + " -> " + formatiert);

                ersatz.add(formatiert);
            }

            var result = StringTemplate.interpolate(template.fragments(), ersatz);
            return new JSONObject(result);
        };
        String test = "\"abc\"";
        System.out.println(JSON."""
                {
                  "test": "\{test}",                                    
                }
                """);
    }
}
