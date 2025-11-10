package util;

public class ScreenUtil {

	public static void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	public static void printSeparator(int length) {
		System.out.println("=".repeat(length));
	}

	public static void printSeparator() {
		printSeparator(40);
	}
}
