import java.io.*;
import java.util.Scanner;

public class Practice13 {
    private static final String FILE_NAME = "document.txt";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n МЕНЮ");
            System.out.println("1. Записати декілька рядків\n2. Прочитати весь файл\n3. Вивести діапазон рядків\n4. * Вставити в обраний рядок\n5. Вихід");
            System.out.print("Ваш вибір: ");
            String choice = scanner.nextLine();

            if (choice.equals("5")) break;

            try {
                switch (choice) {
                    case "1": writeLines(); break;
                    case "2": readRange(1, Integer.MAX_VALUE); break;
                    case "3": printRangeSelection(); break;
                    case "4": insertAtPosition(); break;
                    default: System.out.println("Некоректний вибір!");
                }
            } catch (Exception e) {
                System.out.println("Помилка: " + e.getMessage());
            }
        }
    }

    private static void writeLines() throws IOException {
        System.out.print("Скільки рядків додати? ");
        int count = Integer.parseInt(scanner.nextLine());
        int totalLines = countLines();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            for (int i = 0; i < count; i++) {
                System.out.print("Введіть текст: ");
                String text = scanner.nextLine();
                writer.write((++totalLines) + ": " + text + "\n");
            }
        }
    }

    private static void printRangeSelection() throws IOException {
        System.out.print("Початковий рядок: ");
        int start = Integer.parseInt(scanner.nextLine());
        System.out.print("Кінцевий рядок: ");
        int end = Integer.parseInt(scanner.nextLine());
        readRange(start, end);
    }

    private static void readRange(int start, int end) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            int current = 1;
            while ((line = reader.readLine()) != null) {
                if (current >= start && current <= end) {
                    System.out.println(line);
                }
                current++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("[Файл ще не створено або порожній]");
        }
    }

    private static void insertAtPosition() throws IOException {
        int totalLines = countLines();
        System.out.print("Введіть номер рядка для заміни (1-" + totalLines + "): ");
        int pos = Integer.parseInt(scanner.nextLine());
        System.out.print("Новий текст: ");
        String text = scanner.nextLine();

        String[] lines = new String[totalLines];
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            for (int i = 0; i < totalLines; i++) {
                lines[i] = reader.readLine();
            }
        }

        String displacedLine = lines[pos - 1];
        lines[pos - 1] = pos + ": " + text;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, false))) {
            for (int i = 0; i < totalLines; i++) {
                writer.write((i + 1) + ": " + clean(lines[i], i + 1) + "\n");
            }
            writer.write((totalLines + 1) + ": " + clean(displacedLine, pos) + "\n");
        }
        System.out.println("Успішно змінено!");
    }

    private static int countLines() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            int lines = 0;
            while (reader.readLine() != null) lines++;
            return lines;
        } catch (FileNotFoundException e) {
            return 0;
        }
    }

    private static String clean(String rawLine, int expectedPrefix) {
        if (rawLine == null) return "";
        String prefix = expectedPrefix + ": ";
        if (rawLine.startsWith(prefix)) {
            return rawLine.substring(prefix.length());
        }
        int firstColon = rawLine.indexOf(":");
        if (firstColon != -1 && firstColon < 5) {
            boolean isNumeric = true;
            for (int i = 0; i < firstColon; i++) {
                if (!Character.isDigit(rawLine.charAt(i))) {
                    isNumeric = false;
                    break;
                }
            }
            if (isNumeric) {
                return rawLine.substring(firstColon + 1).trim();
            }
        }
        return rawLine;
    }
}