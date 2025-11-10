package util;

import java.math.BigDecimal;
import java.time.LocalTime;

import enums.LocaleFormat;

/**
 * Centralized configuration for the banking system.
 * Contains business rules constants and global application settings.
 */
public class BankingConfig {
	
	private static LocaleFormat currentLocale = LocaleFormat.BR;
	
    public static final BigDecimal NIGHT_TRANSFER_LIMIT = new BigDecimal("1000");
    public static final LocalTime NIGHT_START = LocalTime.of(20, 0);
    public static final LocalTime NIGHT_END = LocalTime.of(6, 0);
    
    public static final BigDecimal DEFAULT_BUSINESS_ACCOUNT_LIMIT = new BigDecimal("50000");
    public static final BigDecimal BUSINESS_ACCOUNT_PERCENTUAL_FEE = new BigDecimal("0.0075");
    
    public static final BigDecimal DEFAULT_CHECKING_ACCOUNT_LIMIT = new BigDecimal("1000");
    
    public static final BigDecimal DEFAULT_SAVINGS_ACCOUNT_LIMIT = new BigDecimal("0");
    public static final BigDecimal SAVINGS_ACCOUNT_INTEREST_RATE = new BigDecimal("0.005");
    
    public static LocaleFormat getCurrentLocale() {
        return currentLocale;
    }
    
    public static void setCurrentLocale(LocaleFormat locale) {
        if (locale == null) {
            throw new IllegalArgumentException("Locale cannot be null");
        }
        currentLocale = locale;
    }
}
