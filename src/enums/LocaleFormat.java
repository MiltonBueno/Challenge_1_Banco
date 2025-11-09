package enums;

public enum LocaleFormat {
    BR(1),
    US(2);
	
	private int code;

	private LocaleFormat(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
	public static LocaleFormat valueOf(int code) {
		for (LocaleFormat value: LocaleFormat.values()) {
			if(value.getCode() == code) {
				return value;
			}
		}
		throw new IllegalArgumentException("Invalid LocaleFormat code");
	}
}
