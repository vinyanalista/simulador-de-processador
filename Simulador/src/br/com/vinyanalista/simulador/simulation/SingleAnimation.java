package br.com.vinyanalista.simulador.simulation;

public class SingleAnimation {
	private AnimationType type;
	private Byte value;

	public AnimationType getType() {
		return type;
	}

	public Byte getValue() {
		return value;
	}

	public SingleAnimation(AnimationType type, Byte value) {
		this.type = type;
		this.value = value;
	}
}