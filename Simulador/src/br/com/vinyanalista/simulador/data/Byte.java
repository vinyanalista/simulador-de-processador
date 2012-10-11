package br.com.vinyanalista.simulador.data;

public abstract class Byte {
	public static final int REPRESENTATION_DECIMAL = 0;
	public static final int REPRESENTATION_BINARY = 1;
	public static final int REPRESENTATION_HEX = 2;

	protected static int representation = REPRESENTATION_DECIMAL;
	
	public abstract int getMinValue();
	public abstract int getMaxValue();

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

	public void setValue(int value) {
		if ((value >= getMinValue()) && (value <= getMaxValue())) {
			this.value = value;
		} else {
			throw new OutOfRangeException(value, getMinValue(), getMaxValue());
		}
	}

	public void setValueAsBinary(String value) {
		if (validateBinary(value)) {
			try {
				setValue(Integer.parseInt(value, 2));
			} catch (OutOfRangeException e) {
				throw new OutOfRangeException("Value " + value
						+ " is out of range [" + toBinary(getMinValue()) + ".."
						+ toBinary(getMaxValue()) + "]", e);
			}
		} else {
			throw new IllegalArgumentException("Value " + value
					+ " is not a valid hex!");
		}
	}

	public void setValueAsHex(String value) {
		if (validateHex(value)) {
			try {
				setValue(Integer.parseInt(value, 16));
			} catch (OutOfRangeException e) {
				throw new OutOfRangeException("Value " + value
						+ " is out of range [" + Integer.toHexString(getMinValue())
						+ ".." + Integer.toHexString(getMaxValue()) + "]", e);
			}
		} else {
			throw new IllegalArgumentException("Value " + value
					+ " is not a valid binary!");
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