package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    private static final String PRODUCT_FILE = System.getProperty("user.dir") + "\\src\\utils\\products.txt";

    public static void writeToFile(String content) throws IOException {
        try (FileWriter writer = new FileWriter(PRODUCT_FILE, true)) {
            writer.write(content + System.lineSeparator());
        }
    }

    public static List<String> readFromFile() throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }
        }
        return lines;
    }

    public static void cleanFile() throws IOException {
        try (FileWriter writer = new FileWriter(PRODUCT_FILE, false)) {
            writer.write("");
        }
    }
}
