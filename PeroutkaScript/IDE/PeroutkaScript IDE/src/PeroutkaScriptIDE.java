import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class PeroutkaScriptIDE {
    private JFrame frame;
    private JTextArea codeTextArea;
    private JTextArea consoleTextArea;
    private JButton runButton;
    private JButton debugButton;
    private JButton buildButton;

    public PeroutkaScriptIDE() {
        // Vytvoření hlavního okna
        frame = new JFrame("PeroutkaScript IDE");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Vytvoření textové oblasti pro kód
        codeTextArea = new JTextArea();
        codeTextArea.setForeground(Color.YELLOW);
        codeTextArea.setBackground(Color.DARK_GRAY);
        codeTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        // Přidání textové oblasti pro kód do rolovacího panelu
        JScrollPane codeScrollPane = new JScrollPane(codeTextArea);

        // Vytvoření textové oblasti pro konzoli
        consoleTextArea = new JTextArea();
        consoleTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        consoleTextArea.setBackground(Color.BLACK);
        consoleTextArea.setForeground(Color.GREEN);
        consoleTextArea.setEditable(false);

        // Přidání textové oblasti pro konzoli do rolovacího panelu
        JScrollPane consoleScrollPane = new JScrollPane(consoleTextArea);

        // Vytvoření panelu pro tlačítka
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 0));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Vytvoření tlačítka Spustit
        runButton = new JButton("\u25B6\uFE0F"); // Emoji ikona pro spuštění programu
        runButton.setToolTipText("Spustit program");
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runProgram();
            }
        });
        buttonPanel.add(runButton);

        // Vytvoření tlačítka Debug
        debugButton = new JButton("\uD83D\uDD0E"); // Emoji ikona pro debug
        debugButton.setToolTipText("Debugovat program");
        debugButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                debugProgram();
            }
        });
        buttonPanel.add(debugButton);

        // Vytvoření tlačítka Sestavit .jar
        buildButton = new JButton("\uD83D\uDEE0"); // Emoji ikona pro sestavení .jar
        buildButton.setToolTipText("Sestavit .jar");
        buildButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buildJar();
            }
        });
        buttonPanel.add(buildButton);

        // Vytvoření panelu pro kód a konzoli
        JPanel codeConsolePanel = new JPanel(new BorderLayout());
        codeConsolePanel.add(codeScrollPane, BorderLayout.CENTER);
        codeConsolePanel.add(consoleScrollPane, BorderLayout.EAST);

        // Přidání panelu s kódem a konzolí do hlavního okna
        frame.add(codeConsolePanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.NORTH);

        // Zobrazení hlavního okna
        frame.setVisible(true);
    }

    private void runProgram() {
        // Kompilace a spuštění programu
        String code = codeTextArea.getText();
        PeroutkaCompiler compiler = new PeroutkaCompiler();
        String compiledCode = compiler.compile(code);

        // Nastavení výstupu konzole
        PrintStream originalOut = System.out;
        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        PrintStream newOut = new PrintStream(consoleOutput);
        System.setOut(newOut);

        // Spuštění programu
        System.out.println("Spouštím program:");
        System.out.println(compiledCode);

        // Obnovení původního výstupu konzole
        System.setOut(originalOut);

        // Vypsání pouze výstupu do konzole v IDE
        consoleTextArea.setText(consoleOutput.toString());
    }

    private void debugProgram() {
        // Implementace debugování programu
        // ...
    }

    private void buildJar() {
        // Sestavení .jar
        String code = codeTextArea.getText();
        String fileName = "PeroutkaScript.jar";
        try {
            FileWriter fileWriter = new FileWriter("Main.java");
            fileWriter.write("public class Main {\n");
            fileWriter.write("    public static void main(String[] args) {\n");
            fileWriter.write(code + "\n");
            fileWriter.write("    }\n");
            fileWriter.write("}\n");
            fileWriter.close();

            ProcessBuilder processBuilder = new ProcessBuilder("javac", "Main.java");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            if (process.waitFor() == 0) {
                ProcessBuilder jarBuilder = new ProcessBuilder("jar", "cfm", fileName, "Manifest.txt", "Main.class");
                jarBuilder.redirectErrorStream(true);
                Process jarProcess = jarBuilder.start();
                InputStream jarInputStream = jarProcess.getInputStream();
                BufferedReader jarReader = new BufferedReader(new InputStreamReader(jarInputStream));
                String jarLine;
                while ((jarLine = jarReader.readLine()) != null) {
                    System.out.println(jarLine);
                }

                if (jarProcess.waitFor() == 0) {
                    System.out.println("Soubor " + fileName + " byl úspěšně vytvořen.");
                } else {
                    System.out.println("Chyba při sestavování .jar souboru.");
                }
            } else {
                System.out.println("Chyba při kompilaci Java souboru.");
            }

            File mainFile = new File("Main.java");
            mainFile.delete();
            File classFile = new File("Main.class");
            classFile.delete();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PeroutkaScriptIDE();
            }
        });
    }
}
