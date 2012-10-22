package br.com.vinyanalista.simulador.hardware;

import br.com.vinyanalista.simulador.data.Byte;
import br.com.vinyanalista.simulador.data.Data;
import br.com.vinyanalista.simulador.data.OutOfRangeException;

public abstract class Memory {
	public abstract int getMinAddress();

	public abstract int getMaxAddress();

	private Byte[] content;

	public Memory() {
		int numberOfCells = getMaxAddress() - getMinAddress() + 1;
		content = new Byte[numberOfCells];
		for (int address = getMinAddress(); address <= getMaxAddress(); address++) {
			content[address] = new Data();
		}
	}

	public void writeByte(int address, Byte byteToWrite) {
		if ((address >= getMinAddress()) && (address <= getMaxAddress())) {
			content[address] = byteToWrite;
		} else {
			throw new OutOfRangeException(address, getMinAddress(),
					getMaxAddress());
		}
	}

	public Byte readByte(int address) {
		if ((address >= getMinAddress()) && (address <= getMaxAddress())) {
			return content[address + getMinAddress() - getMaxAddress()];
		} else {
			throw new OutOfRangeException(address, getMinAddress(),
					getMaxAddress());
		}
	}
}