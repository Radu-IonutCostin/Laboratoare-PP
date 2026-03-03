package org.example;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

public class Main {

    // ---------------------------------------------------------------
    // Functie Python: citeste de la tastatura numarul de aruncari
    // si numarul x, le valideaza si le returneaza ca string "n|x"
    // ---------------------------------------------------------------
    private static int[] readInputViaPython(Context polyglot) {
        String pythonCode =
                "n = int(input('Introduceti numarul de aruncari ale monedei (n): '))\n" +
                        "while n <= 0:\n" +
                        "    print('Numarul de aruncari trebuie sa fie pozitiv!')\n" +
                        "    n = int(input('Introduceti numarul de aruncari ale monedei (n): '))\n" +
                        "\n" +
                        "x = int(input('Introduceti numarul x (1 <= x <= ' + str(n) + '): '))\n" +
                        "while x < 1 or x > n:\n" +
                        "    print('x trebuie sa fie intre 1 si', n)\n" +
                        "    x = int(input('Introduceti numarul x (1 <= x <= ' + str(n) + '): '))\n" +
                        "\n" +
                        "input_result = str(n) + '|' + str(x)\n";

        polyglot.eval("python", pythonCode);
        Value result = polyglot.eval("python", "input_result");
        String[] parts = result.asString().split("\\|");
        return new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
    }

    // ---------------------------------------------------------------
    // Functie Python: calculeaza distributia binomiala si afiseaza
    // probabilitatea P(X <= x) pentru aruncarea unei monede
    // ---------------------------------------------------------------
    private static void computeBinomialViaPython(Context polyglot, int n, int x) {
        String pythonCode =
                "import math\n" +
                        "\n" +
                        "n = " + n + "\n" +
                        "x = " + x + "\n" +
                        "p = 0.5  # probabilitatea de a obtine 'pajura' la o aruncare\n" +
                        "\n" +
                        "# Calcul P(X <= x) = suma P(X = k) pentru k de la 0 la x\n" +
                        "# unde P(X = k) = C(n, k) * p^k * (1-p)^(n-k)\n" +
                        "def binom_prob(n, k, p):\n" +
                        "    return math.comb(n, k) * (p ** k) * ((1 - p) ** (n - k))\n" +
                        "\n" +
                        "# Distributia completa (toate valorile posibile)\n" +
                        "print('\\n=== Distributia Binomiala B(n={}, p=0.5) ==='.format(n))\n" +
                        "print('{:<10} {:<20} {:<20}'.format('k', 'P(X = k)', 'P(X <= k)'))\n" +
                        "print('-' * 50)\n" +
                        "\n" +
                        "cumulative = 0.0\n" +
                        "for k in range(n + 1):\n" +
                        "    prob_k = binom_prob(n, k, p)\n" +
                        "    cumulative += prob_k\n" +
                        "    marker = ' <--- x' if k == x else ''\n" +
                        "    print('{:<10} {:<20.6f} {:<20.6f}{}'.format(k, prob_k, cumulative, marker))\n" +
                        "\n" +
                        "# Calculul final P(X <= x)\n" +
                        "prob_cumulative = sum(binom_prob(n, k, p) for k in range(x + 1))\n" +
                        "\n" +
                        "print('\\n=== REZULTAT ===')\n" +
                        "print('Probabilitatea de a obtine cel mult {} ori pajura'.format(x))\n" +
                        "print('din {} aruncari ale unei monede:'.format(n))\n" +
                        "print('P(X <= {}) = {:.6f} ({:.2f}%)'.format(x, prob_cumulative, prob_cumulative * 100))\n" +
                        "\n" +
                        "final_result = prob_cumulative\n";

        polyglot.eval("python", pythonCode);
    }

    // ---------------------------------------------------------------
    // Functia MAIN - orchestreaza fluxul (Java)
    // ---------------------------------------------------------------
    public static void main(String[] args) {

        System.out.println("=== Aplicatie Polyglot - Distributie Binomiala (Java + Python via GraalVM) ===\n");

        try (Context polyglot = Context.newBuilder("python")
                .allowAllAccess(true)
                .build()) {

            // Pasul 1: Python citeste datele de la tastatura
            System.out.println("[Java] Se citesc datele de intrare prin Python...\n");
            int[] input = readInputViaPython(polyglot);
            int n = input[0];
            int x = input[1];

            System.out.println("\n[Java] Date primite: n=" + n + ", x=" + x);

            // Pasul 2: Python calculeaza distributia binomiala
            System.out.println("[Java] Se calculeaza distributia binomiala prin Python...");
            computeBinomialViaPython(polyglot, n, x);

            System.out.println("\n[Java] Executie finalizata cu succes.");

        } catch (Exception e) {
            System.err.println("[EROARE] " + e.getMessage());
            e.printStackTrace();
        }
    }
}