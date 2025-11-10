package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import domain.account.Account;
import domain.transaction.Transaction;
import enums.CsvDelimiter;

/**
 * Exports transaction data to CSV with configurable delimiter.
 * Currency format is determined by the global locale configuration.
 * Groups transactions by account with blank line separators.
 */
public class CsvExporter {

    /**
     * Exports transactions to CSV file.
     * Columns: Account, Transaction ID, Time, Type, Amount, Source, Target
     * 
     * @param transactions list of transactions to export
     * @param file target CSV file
     * @param delimiter CSV delimiter
     * @throws RuntimeException if I/O error occurs
     */
    public static void exportTransactionsToCsv(List<Transaction> transactions, File file, CsvDelimiter delimiter) {
        String sep = String.valueOf(delimiter.getDelimiter());

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {

            bw.write('\ufeff');
            bw.write(String.join(sep, "Account", "Transaction ID", "Time", "Type", "Amount", "Source", "Target"));
            bw.newLine();

            String lastAccount = null;

            for (Transaction t : transactions) {
                String currentAccount = getAccountNumber(t);

                if (lastAccount != null && !lastAccount.equals(currentAccount)) {
                    bw.newLine();
                }

                String line = String.join(sep,
                        quoteIfNeeded(currentAccount),
                        quoteIfNeeded(t.getId().toString()),
                        quoteIfNeeded(NumberFormatter.formatDateTime(t.getTransactionTime())),
                        quoteIfNeeded(t.getType().name()),
                        quoteIfNeeded(NumberFormatter.formatAmount(t.getAmount())),
                        quoteIfNeeded(getAccountNumber(t.getSourceAccount())),
                        quoteIfNeeded(getAccountNumber(t.getTargetAccount()))
                );

                bw.write(line);
                bw.newLine();
                lastAccount = currentAccount;
            }

        } catch (IOException e) {
            throw new RuntimeException("Error while writing CSV: " + e.getMessage());
        }
    }

    private static String getAccountNumber(Transaction t) {
        if (t.getSourceAccount() != null) return t.getSourceAccount().getAccountNumber();
        if (t.getTargetAccount() != null) return t.getTargetAccount().getAccountNumber();
        return "N/A";
    }

    private static String getAccountNumber(Account account) {
        return (account != null) ? account.getAccountNumber() : "";
    }

    private static String quoteIfNeeded(String s) {
        if (s == null) return "";
        String escaped = s.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}

