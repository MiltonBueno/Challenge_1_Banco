package enums;

public enum CsvDelimiter {
    COMMA(1, ','),
    SEMICOLON(2, ';');
	
	private int code;
	private char delimiter;

	private CsvDelimiter(int code, char delimiter) {
		this.code = code;
		this.delimiter = delimiter;
	}
	
	public int getCode() {
		return code;
	}
	
	public char getDelimiter() {
		return delimiter;
	}
	
	public static CsvDelimiter valueOf(int code) {
		for (CsvDelimiter value : CsvDelimiter.values()) {
			if (value.getCode() == code) {
				return value;
			}
		}
		throw new IllegalArgumentException("Invalid CsvDelimiter code");
	}
}
