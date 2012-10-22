package br.com.vinyanalista.simulador.data;

public abstract class Byte {
	public static final int REPRESENTATION_DECIMAL = 0;
	public static final int REPRESENTATION_BINARY = 1;
	public static final int REPRESENTATION_HEX = 2;

	protected static int representation = REPRESENTATION_DECIMAL;

	protected static void setRepresentation(int representation) {
		switch (representation) {
		case REPRESENTATION_DECIMAL:
		case REPRESENTATION_BINARY:
		case REPRESENTATION_HEX:
			Byte.representation = representation;
		default:
			throw new IllegalArgumentException();
		}
	}

	int value;

	public Byte() {
	}

	public Byte(int value) {
		setValue(value);
	}

	public int getValue() {
		return value;
	}

	public String getValueAsBinary() {
		return toBinary(value);
	}

	public String getValueAsHex() {
		return toHex(value);
	}

	public abstract void setValue(int value);

	protected void setValue(int value, int minValue, int maxValue) {
		if ((value >= minValue) && (value <= maxValue)) {
			this.value = value;
		} else {
			throw new OutOfRangeException(value, minValue, maxValue);
		}
	}

	public abstract void setValueAsBinary(String value);

	protected void setValueAsBinary(String value, int minValue, int maxValue) {
		if (validateBinary(value)) {
			try {
				setValue(Integer.parseInt(value, 2));
			} catch (OutOfRangeException e) {
				throw new OutOfRangeException("Value " + value
						+ " is out of range [" + toBinary(minValue) + ".."
						+ toBinary(maxValue) + "]", e);
			}
		} else {
			throw new IllegalArgumentException("Value " + value
					+ " is not a valid binary!");
		}
	}

	public abstract void setValueAsHex(String value);

	protected void setValueAsHex(String value, int minValue, int maxValue) {
		if (validateHex(value)) {
			try {
				setValue(Integer.parseInt(value, 16));
			} catch (OutOfRangeException e) {
				throw new OutOfRangeException("Value " + value
						+ " is out of range [" + Integer.toHexString(minValue)
						+ ".." + Integer.toHexString(maxValue) + "]", e);
			}
		} else {
			throw new IllegalArgumentException("Value " + value
					+ " is not a valid hex!");
		}
	}

	@Override
	public boolean equals(Object obj) {
		Byte anotherByte = (Byte) obj;
		if (anotherByte != null) {
			return (anotherByte.value == this.value);
		} else {
			return false;
		}
	}

	public static final String toBinary(int value) {
		String result = Integer.toBinaryString(value);
		for (int i = result.length(); i < 8; i++) {
			result = "0" + result;
		}
		return result;
	}

	public static final String toHex(int value) {
		return Integer.toHexString(value);
	}

	public static final boolean validateBinary(String value) {
		return value.matches("[01]{8}");
	}

	public static final boolean validateHex(String value) {
		return value.matches("[0-9A-Fa-f]+");
	}

}