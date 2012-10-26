package br.com.vinyanalista.simulador.data;

import java.util.Random;

public abstract class Byte {
	public static final int REPRESENTATION_RECOMMENDED = 0;
	public static final int REPRESENTATION_DECIMAL = 1;
	public static final int REPRESENTATION_HEX = 2;
	public static final int REPRESENTATION_BINARY = 3;

	protected static int preferredRepresentation = REPRESENTATION_DECIMAL;

	public static void setRepresentation(int representation) {
		switch (representation) {
		case REPRESENTATION_RECOMMENDED:
			preferredRepresentation = REPRESENTATION_DECIMAL;
		case REPRESENTATION_DECIMAL:
		case REPRESENTATION_HEX:
		case REPRESENTATION_BINARY:
			preferredRepresentation = representation;
		default:
			throw new IllegalArgumentException();
		}
	}

	int value;

	public Byte(int value) {
		setValue(value);
	}

	protected Byte(int minValue, int maxValue) {
		// http://java.about.com/od/javautil/a/randomnumbers.htm
		setValue(new Random().nextInt(maxValue - minValue + 1) + minValue);
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

	public String getValueAsPreferredRepresentation() {
		switch (preferredRepresentation) {
		case REPRESENTATION_DECIMAL:
			return Integer.toString(value);
		case REPRESENTATION_HEX:
			return getValueAsHex();
		case REPRESENTATION_BINARY:
			return getValueAsBinary();
		default:
			return null;
		}
	}

	public abstract void setValue(int value);

	protected void setValue(int value, int minValue, int maxValue) {
		if ((value >= minValue) && (value <= maxValue)) {
			this.value = value;
		} else {
			throw new OutOfRangeException(value, minValue, maxValue);
		}
	}

	// public abstract void setValueAsBinary(String value);
	//
	// protected void setValueAsBinary(String value, int minValue, int maxValue)
	// {
	// if (validateBinary(value)) {
	// try {
	// setValue(Integer.parseInt(value, 2));
	// } catch (OutOfRangeException e) {
	// throw new OutOfRangeException("Value " + value
	// + " is out of range [" + toBinary(minValue) + ".."
	// + toBinary(maxValue) + "]", e);
	// }
	// } else {
	// throw new IllegalArgumentException("Value " + value
	// + " is not a valid binary!");
	// }
	// }
	//
	// public abstract void setValueAsHex(String value);
	//
	// protected void setValueAsHex(String value, int minValue, int maxValue) {
	// if (validateHex(value)) {
	// try {
	// setValue(Integer.parseInt(value, 16));
	// } catch (OutOfRangeException e) {
	// throw new OutOfRangeException("Value " + value
	// + " is out of range [" + Integer.toHexString(minValue)
	// + ".." + Integer.toHexString(maxValue) + "]", e);
	// }
	// } else {
	// throw new IllegalArgumentException("Value " + value
	// + " is not a valid hex!");
	// }
	// }

	@Override
	public boolean equals(Object obj) {
		Byte anotherByte = (Byte) obj;
		if (anotherByte != null) {
			return (anotherByte.value == this.value);
		} else {
			return false;
		}
	}

	public static String toBinary(int value) {
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

	// public static final boolean validateHex(String value) {
	// return value.matches("[0-9A-Fa-f]+");
	// }

	public static void main(String[] args) {
		byte teste1 = (byte) 127;
		System.out.println(teste1);
		byte teste2 = (byte) 128;
		System.out.println(teste2);
		byte teste3 = (byte) 129;
		System.out.println(teste3);
		byte teste4 = (byte) -127;
		System.out.println(teste4);
		byte teste5 = (byte) -128;
		System.out.println(teste5);
		byte teste6 = (byte) -129;
		System.out.println(teste6);
		byte teste7 = (byte) (teste1 + 1);
		System.out.println(teste7);
	}

}