package org.example;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

public class Polyglot3 {

    // Functie Python: genereaza aleatoriu o lista de 20 de numere intregi (intre 1 si 100)
    private static int[] generateNumbers() {
        Context polyglot = Context.newBuilder().allowAllAccess(true).build();

        polyglot.eval("python",
                "import random\n" +
                        "def generate_list():\n" +
                        "    return [random.randint(1, 100) for _ in range(20)]\n"
        );

        Value pyList = polyglot.eval("python", "generate_list()");

        int[] numbers = new int[(int) pyList.getArraySize()];
        for (int i = 0; i < pyList.getArraySize(); i++) {
            numbers[i] = pyList.getArrayElement(i).asInt();
        }
        return numbers;
    }

    // Functie JavaScript: afiseaza lista de numere intregi
    private static void displayNumbers(int[] numbers) {
        StringBuilder jsArray = new StringBuilder("[");
        for (int i = 0; i < numbers.length; i++) {
            jsArray.append(numbers[i]);
            if (i < numbers.length - 1) jsArray.append(", ");
        }
        jsArray.append("]");

        Context polyglot = Context.newBuilder().allowAllAccess(true).build();
        polyglot.eval("js",
                "var arr = " + jsArray + ";\n" +
                        "console.log('Lista generata de Python (' + arr.length + ' numere): ' + arr.join(', '));\n"
        );
    }

    // Functie Python: sorteaza lista, elimina primele si ultimele 20%, calculeaza media aritmetica
    private static double processNumbers(int[] numbers) {
        StringBuilder pyArray = new StringBuilder("[");
        for (int i = 0; i < numbers.length; i++) {
            pyArray.append(numbers[i]);
            if (i < numbers.length - 1) pyArray.append(", ");
        }
        pyArray.append("]");

        Context polyglot = Context.newBuilder().allowAllAccess(true).build();

        polyglot.eval("python",
                "def process_list(arr):\n" +
                        "    sorted_arr = sorted(arr)\n" +
                        "    print('Lista sortata: ' + str(sorted_arr))\n" +
                        "    n = len(sorted_arr)\n" +
                        "    cut = int(n * 0.2)\n" +
                        "    trimmed = sorted_arr[cut:n - cut]\n" +
                        "    print('Dupa eliminarea primelor si ultimelor 20%: ' + str(trimmed))\n" +
                        "    mean = sum(trimmed) / len(trimmed)\n" +
                        "    print('Media aritmetica: ' + str(mean))\n" +
                        "    return mean\n"
        );

        Value result = polyglot.eval("python", "process_list(" + pyArray + ")");
        return result.asDouble();
    }

    public static void main(String[] args) {
        System.out.println("=== Aplicatie Polyglot - Problema 3 ===\n");

        // Pasul 1: Python genereaza 20 de numere aleatoare
        System.out.println("[Python] Generare lista de 20 numere aleatoare...");
        int[] numbers = generateNumbers();

        // Pasul 2: JavaScript afiseaza lista
        System.out.println("[JavaScript] Afisare lista:");
        displayNumbers(numbers);

        // Pasul 3: Python sorteaza, elimina 20% si calculeaza media
        System.out.println("\n[Python] Procesare statistica:");
        double mean = processNumbers(numbers);

        System.out.println("\n=== Rezultat final: media aritmetica = " + mean + " ===");
    }
}