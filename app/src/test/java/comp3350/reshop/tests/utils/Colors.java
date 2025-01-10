package comp3350.reshop.tests.utils;

public enum Colors {
	RED ("\u001B[31m"),
	GREEN ("\u001B[32m"),
	RESET("\u001B[0m");

	private final String code;

	Colors(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return code;
	}
}