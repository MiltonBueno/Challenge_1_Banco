package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import domain.account.Account;
import domain.transaction.Transaction;
import enums.CsvDelimiter;
import enums.LocaleFormat;

public class CsvExporter {

    public static void exportTransactionsToCsv(List<Transaction> transactions, File file, LocaleFormat localeFormat, CsvDelimiter delimiter) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String sep = String.valueOf(delimiter.getDelimiter());

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
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
                        quoteIfNeeded(t.getTransactionTime().format(dtf)),
                        quoteIfNeeded(t.getType().name()),
                        quoteIfNeeded(NumberFormatter.formatAmount(t.getAmount(), localeFormat)),
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

