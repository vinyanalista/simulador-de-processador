package br.com.vinyanalista.simulador.data;

public class InstructionAddress extends Byte {

	public static final int MIN_VALUE = 0;
	public static final int MAX_VALUE = 127;

	public InstructionAddress() {
		super(MIN_VALUE, MAX_VALUE);
	}

	public InstructionAddress(int value) {
		super(value);
	}

	@Override
	public void setValue(int value) {
		setValue(value, MIN_VALUE, MAX_VALUE);
	}

//	@Override
//	public void setValueAsBinary(String value) {
//		setValueAsBinary(value, MIN_VALUE, MIN_VALUE);
//	}
//
//	@Override
//	public void setValueAsHex(String value) {
//		setValueAsHex(value, MIN_VALUE, MIN_VALUE);
//	}

}