package talkwave.integration;

import talkwave.ui.ConsoleColors;
import talkwave.ui.MessagePrinter;

import java.io.*;
import java.util.Base64;

public class FileHelper {

    public static String encodeFileToBase64(File file) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];

            fileInputStream.read(bytes);

            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            MessagePrinter.println(ConsoleColors.RED, "Erro ao enviar arquivo");
        } finally {
            try {
                if (fileInputStream != null) fileInputStream.close();
            } catch (IOException ignored) {
            }
        }

        return null;
    }

    public static byte[] decodeFromBase64(String base64) {
        return Base64.getDecoder().decode(base64);
    }
}
