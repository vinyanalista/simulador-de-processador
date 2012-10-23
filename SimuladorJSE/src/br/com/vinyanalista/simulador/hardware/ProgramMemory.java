package br.com.vinyanalista.simulador.hardware;

public class ProgramMemory extends Memory {

	@Override
	public int getMaxAddress() {
		return 127;
	}

	@Override
	public int getMinAddress() {
		return 0;
	}

}
