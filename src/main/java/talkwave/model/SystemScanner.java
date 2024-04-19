package talkwave.model;

import java.util.Scanner;

public class SystemScanner {

    public static String get(String label) {
        if (label != null) {
            System.out.println(label);
        }

        return get();
    }

    public static String get() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
