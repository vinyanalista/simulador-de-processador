package br.com.vinyanalista.simulador.data;

public class Data extends Byte {

	public static final int MIN_VALUE = -128;
	public static final int MAX_VALUE = 127;

	public Data() {
		super(MIN_VALUE, MAX_VALUE);
	}

	public Data(int value) {
		super(value);
	}

	@Override
	public String getValueAsBinary() {
		if (value < 0) {
			// http://geekexplains.blogspot.com.br/2009/05/binary-rep-of-negative-numbers-in-java.html
			int aux = value * -1;
			return toBinary((255 - aux) + 1);
		} else {
			return "0" + toBinary(value);
		}
	}

	@Override
	public void setValue(int value) {
		setValue(value, MIN_VALUE, MAX_VALUE);
	}

	// @Override
	// public void setValueAsBinary(String value) {
	// setValueAsBinary(value, MIN_VALUE, MIN_VALUE);
	// }
	//
	// @Override
	// public void setValueAsHex(String value) {
	// setValueAsHex(value, MIN_VALUE, MIN_VALUE);
	// }

	public static final String toBinary(int value) {
		String result = Integer.toBinaryString(value);
		for (int i = result.length(); i < 7; i++) {
			result = "0" + result;
		}
		return result;
	}

}