package talkwave.ui;

public class MessagePrinter {

    public static void println(ConsoleColors color, String text) {
        System.out.println(color.getCode() + text);
    }

}

