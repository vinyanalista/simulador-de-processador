package br.com.vinyanalista.simulador.data;

public class DataAddress extends Byte {

	public static final int MIN_VALUE = 128;
	public static final int MAX_VALUE = 255;
	
	public DataAddress() {
	}

	public DataAddress(int value) {
		setValue(value);
	}

	@Override
	public int getMinValue() {
		return 128;
	}

	@Override
	public int getMaxValue() {
		return 255;
	}

}