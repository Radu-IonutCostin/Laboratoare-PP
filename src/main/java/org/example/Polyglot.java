package org.example;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

public class Polyglot {

    // Metoda pentru conversia la upper case folosind JavaScript (R nu mai este suportat in GraalVM modern)
    private static String RToUpper(String token) {
        Context polyglot = Context.newBuilder().allowAllAccess(true).build();
        Value result = polyglot.eval("js", "'" + token + "'.toUpperCase()");
        return result.asString();
    }

    // Metoda pentru calculul sumei de control folosind Python - formula polinomiala rang 5
    // Problema 2: se elimina primul si ultimul caracter folosind substring inainte de calcul
    private static int SumCRC(String token) {
        Context polyglot = Context.newBuilder().allowAllAccess(true).build();

        // Coeficientii polinomului (numere prime pentru dispersie mai buna)
        // P(x) = c0 + c1*x + c2*x^2 + c3*x^3 + c4*x^4 + c5*x^5
        // unde x = valoarea ASCII a fiecarui caracter
        polyglot.eval("python",
                "def poly_crc(token):\n" +
                        "    # Problema 2: eliminam primul si ultimul caracter folosind substring\n" +
                        "    if len(token) > 2:\n" +
                        "        token = token[1:-1]\n" +
                        "    elif len(token) <= 2:\n" +
                        "        return 0\n" +
                        "    c = [1, 3, 5, 7, 11, 13]\n" +
                        "    result = 0\n" +
                        "    for ch in token:\n" +
                        "        x = ord(ch)\n" +
                        "        result += c[0] + c[1]*x + c[2]*(x**2) + c[3]*(x**3) + c[4]*(x**4) + c[5]*(x**5)\n" +
                        "    return result % 65536\n"
        );

        Value result = polyglot.eval("python", "poly_crc('" + token + "')");
        return result.asInt();
    }

    // Functia MAIN
    public static void main(String[] args) {
        // Construim un context pentru evaluare elemente JS
        Context polyglot = Context.create();

        // Construim un array de string-uri folosind cuvinte din pagina web
        // https://chrisseaton.com/truffleruby/tenthings
        Value array = polyglot.eval("js",
                "[\"If\", \"we\", \"run\", \"the\", \"java\", \"command\", \"we\", \"just\"]"
        );

        // Pentru fiecare cuvant, convertim la upCase folosind JS si calculam suma de control folosind Python
        for (int i = 0; i < array.getArraySize(); i++) {
            String element = array.getArrayElement(i).asString();
            String upper = RToUpper(element);
            int crc = SumCRC(upper);
            System.out.println(upper + " -> " + crc);
        }
    }
}