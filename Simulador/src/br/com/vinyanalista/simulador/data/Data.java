package br.com.vinyanalista.simulador.data;

public class Data extends Byte {

	public Data(int value) {
		super(value);
	}

	@Override
	public int getMinValue() {
		return -128;
	}

	@Override
	public int getMaxValue() {
		return 127;
	}

}