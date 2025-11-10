package util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import enums.LocaleFormat;

public class NumberFormatter {
	
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
	private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Formats a BigDecimal as currency string using the global locale configuration.
     * Examples: BR → "R$ 1.234,56", US → "$1,234.56"
     * 
     * @param value the amount to format
     * @return formatted currency string
     */
    public static String formatAmount(BigDecimal value) {
        if (value == null) return "";

        Locale locale = toLocale(BankingConfig.getCurrentLocale());
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        BigDecimal scaled = value.setScale(2, RoundingMode.HALF_EVEN);
        return nf.format(scaled);
    }
    
    /**
     * Formats a LocalTime as HH:mm string.
     * 
     * @param time the time to format
     * @return formatted time string
     */
    public static String formatTime(LocalTime time) {
        return (time != null) ? time.format(TIME_FORMATTER) : "";
    }
    
    /**
     * Formats a LocalDateTime as yyyy-MM-dd HH:mm:ss string.
     * 
     * @param dateTime the date-time to format
     * @return formatted date-time string
     */
    public static String formatDateTime(java.time.LocalDateTime dateTime) {
        return (dateTime != null) ? dateTime.format(DATETIME_FORMATTER) : "";
    }

    private static Locale toLocale(LocaleFormat format) {
        return (format == LocaleFormat.BR)
                ? Locale.forLanguageTag("pt-BR")
                : Locale.forLanguageTag("en-US");
    }
}

