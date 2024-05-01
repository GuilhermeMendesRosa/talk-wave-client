package talkwave.integration;

import talkwave.ui.ConsoleColors;
import talkwave.ui.MessagePrinter;

import java.io.*;
import java.util.Base64;

public class FileHelper {

    public static String encodeFileToBase64(String filePath) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            byte[] bytes = new byte[(int) new File(filePath).length()];
            fileInputStream.read(bytes);
            String base64Encoded = Base64.getEncoder().encodeToString(bytes);

            return base64Encoded;
        } catch (IOException e) {
            MessagePrinter.println(ConsoleColors.RED, "Erro ao enviar arquivo");
        } finally {
            try {
                if (fileInputStream != null) fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
