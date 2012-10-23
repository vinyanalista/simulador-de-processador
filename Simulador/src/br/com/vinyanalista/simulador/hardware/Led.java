package br.com.vinyanalista.simulador.hardware;

import br.com.vinyanalista.simulador.data.Byte;
import br.com.vinyanalista.simulador.data.Data;

public class Led {
	private Byte value;

	public Led() {
		value = new Data(0);
	}

	public Byte getValue() {
		return value;
	}

	public void setValue(Byte value) {
		this.value = value;
	}
}