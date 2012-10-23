package br.com.vinyanalista.simulador.hardware;

import br.com.vinyanalista.simulador.data.Data;

public class ALU {
	private Data in1;
	private Data in2;
	private Data out;

	public Data getIn1() {
		return in1;
	}

	public void setIn1(Data in1) {
		this.in1 = in1;
	}

	public Data getIn2() {
		return in2;
	}

	public void setIn2(Data in2) {
		this.in2 = in2;
	}

	public Data getOut() {
		return out;
	}

	public void setOut(Data out) {
		this.out = out;
	}

	public void add() {
		// TODO implementar overflow
		out = new Data(in1.getValue() + in2.getValue());
	}
}