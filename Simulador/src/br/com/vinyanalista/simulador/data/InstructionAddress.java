package br.com.vinyanalista.simulador.data;

public class InstructionAddress extends Byte {
	
	public static final int MIN_VALUE = 0;
	public static final int MAX_VALUE = 127;
	
	public InstructionAddress() {
	}
	
	public InstructionAddress(int value) {
		setValue(value);
	}

	@Override
	public int getMinValue() {
		return 0;
	}

	@Override
	public int getMaxValue() {
		return 127;
	}
	
}