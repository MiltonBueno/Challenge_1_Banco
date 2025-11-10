package ui;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Handles console input validation and type conversion with automatic retry on errors.
 */
public class InputHandler {

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Reads string from console with retry on empty input.
     */
    public String getString(String message) {
        System.out.print(message + ": ");
        String input = scanner.nextLine().trim();
        while (input.isEmpty()) {
            System.out.print("Input cannot be empty. Try again: ");
            input = scanner.nextLine().trim();
        }
        return input;
    }

    /**
     * Reads BigDecimal from console with retry on invalid format.
     * Accepts currency symbols (R$, $) and both comma and dot as decimal separators.
     */
    public BigDecimal getBigDecimal(String message) {
        while (true) {
            System.out.print(message + ": ");
            try {
                String input = scanner.nextLine().trim();
                input = input.replace("R$", "").replace("$", "").replace(" ", "");
                input = input.replace(',', '.');
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    /**
     * Reads integer from console with retry on invalid format.
     */
    public int getInt(String message) {
        while (true) {
            System.out.print(message + ": ");
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid integer. Try again.");
            }
        }
    }

    /**
     * Reads yes/no confirmation ('y' or 'n') with retry on invalid input.
     */
    public boolean getConfirmation(String message) {
        while (true) {
            System.out.print(message + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y")) return true;
            if (input.equals("n")) return false;
            System.out.println("Please type 'y' or 'n'.");
        }
    }
    
    public void waitForEnter() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}


