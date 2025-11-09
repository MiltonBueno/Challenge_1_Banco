package util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import enums.LocaleFormat;
import exceptions.ValidationException;

public class NumberFormatter {
	
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static BigDecimal parse(String value, LocaleFormat format) {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Value cannot be null or empty");
        }

        Locale locale = toLocale(format);
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(locale);
        char grouping = symbols.getGroupingSeparator();
        char decimal = symbols.getDecimalSeparator();

        String cleaned = value.trim();

        cleaned = cleaned.replace(String.valueOf(grouping), "");
        cleaned = cleaned.replace(String.valueOf(decimal), ".");

        cleaned = cleaned.replaceAll("[^0-9.\\-]", "");

        if (cleaned.isBlank() || cleaned.equals(".") || cleaned.equals("-") || cleaned.equals("-.") ) {
            throw new ValidationException("Invalid monetary value: " + value);
        }

        try {
            BigDecimal bd = new BigDecimal(cleaned);
            return bd.setScale(2, RoundingMode.HALF_EVEN);
        } catch (NumberFormatException ex) {
            throw new ValidationException("Invalid monetary value: " + value);
        }
    }

    public static String formatAmount(BigDecimal value, LocaleFormat format) {
        if (value == null) return "";

        Locale locale = toLocale(format);
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        BigDecimal scaled = value.setScale(2, RoundingMode.HALF_EVEN);
        return nf.format(scaled);
    }
    
    public static String formatTime(LocalTime time) {
        return time.format(TIME_FORMATTER);
    }

    private static Locale toLocale(LocaleFormat format) {
        return (format == LocaleFormat.BR)
                ? Locale.forLanguageTag("pt-BR")
                : Locale.forLanguageTag("en-US");
    }
}

