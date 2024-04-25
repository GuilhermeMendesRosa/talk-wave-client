package talkwave.model;

public class SystemPrinter {

    public static void println(String txt) {
        String blueColorCode = "\u001B[34m";
        String resetColorCode = "\u001B[0m";
        String blueText = String.format("\uD83C\uDF0A %s \uD83C\uDF0A", txt);

        System.out.println(blueColorCode + blueText + resetColorCode);
    }

}
