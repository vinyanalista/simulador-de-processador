package br.com.vinyanalista.simulador.hardware;

public class DataMemory extends Memory {
	@Override
	public int getMaxAddress() {
		return 255;
	}

	@Override
	public int getMinAddress() {
		return 128;
	}
}