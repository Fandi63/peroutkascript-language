import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PeroutkaCompiler {
    public static void main(String[] args) {
        // Načtení kódu ze vstupu
        Scanner scanner = new Scanner(System.in);
        StringBuilder codeBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            codeBuilder.append(line).append("\n");
        }
        String code = codeBuilder.toString();

        // Kompilace a vykonání kódu
        String compiledCode = compile(code);
        if (compiledCode != null) {
            System.out.println("Kompilace úspěšná. Výsledný kód:");
            System.out.println(compiledCode);
            execute(compiledCode);
        } else {
            System.out.println("Chyba při kompilaci.");
        }
    }

    public static String compile(String code) {
        StringBuilder compiledCodeBuilder = new StringBuilder();

        // Rozdělení kódu na řádky
        String[] lines = code.split("\n");

        // Procházení řádků kódu
        for (String line : lines) {
            // Rozdělení řádku na instrukci a argumenty
            String[] parts = line.split(" ");
            String instruction = parts[0];
            String[] arguments = new String[parts.length - 1];
            System.arraycopy(parts, 1, arguments, 0, arguments.length);

            // Zpracování instrukce
            if (instruction.equals("add")) {
                String result = processAdd(arguments);
                if (result != null) {
                    compiledCodeBuilder.append(result).append("\n");
                } else {
                    return null;
                }
            } else if (instruction.equals("print")) {
                String result = processPrint(arguments);
                if (result != null) {
                    compiledCodeBuilder.append(result).append("\n");
                } else {
                    return null;
                }
            } else if (instruction.equals("input")) {
                String result = processInput();
                if (result != null) {
                    compiledCodeBuilder.append(result).append("\n");
                } else {
                    return null;
                }
            } else if (instruction.equals("output")) {
                String result = processOutput(arguments);
                if (result != null) {
                    compiledCodeBuilder.append(result).append("\n");
                } else {
                    return null;
                }
            } else {
                System.out.println("CHYBA: Neznámá instrukce!"); // Neznámá instrukce
            }
        }

        return compiledCodeBuilder.toString();
    }

    public static void execute(String code) {
        // V tomto příkladu jen vypíšeme zkompilovaný kód
        System.out.println("Výstup:");
        System.out.println(code);
    }

    public static String processAdd(String[] arguments) {
        List<Integer> numbers = new ArrayList<>();
        for (String arg : arguments) {
            try {
                int number = Integer.parseInt(arg);
                numbers.add(number);
            } catch (NumberFormatException e) {
                return null; // Chybný argument
            }
        }

        int result = 0;
        for (int number : numbers) {
            result += number;
        }

        return "Výsledek: " + result;
    }

    public static String processPrint(String[] arguments) {
        return String.join(" ", arguments);
    }

    public static String processInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Vstup: ");
        return scanner.nextLine();
    }

    public static String processOutput(String[] arguments) {
        if (arguments.length == 1) {
            try {
                int value = Integer.parseInt(arguments[0]);
                if (value == 0) {
                    return "Výstup je 0";
                } else if (value == 1) {
                    return "Výstup je 1";
                } else {
                    return "Neznámý výstup";
                }
            } catch (NumberFormatException e) {
                return null; // Chybný argument
            }
        } else {
            return null; // Nesprávný počet argumentů
        }
    }
}
