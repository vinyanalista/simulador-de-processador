package br.com.vinyanalista.simulador.hardware;

import br.com.vinyanalista.simulador.data.Byte;
import br.com.vinyanalista.simulador.data.OutOfRangeException;

public abstract class Memory {
	public abstract int getMinAddress();

	public abstract int getMaxAddress();

	private Byte[] content = new Byte[getMaxAddress() - getMinAddress() + 1];

	public Byte getByte(int address) {
		if ((address >= getMinAddress()) && (address <= getMaxAddress())) {
			return content[address + getMinAddress() - getMaxAddress()];
		} else {
			throw new OutOfRangeException(address, getMinAddress(),
					getMaxAddress());
		}
	}
}