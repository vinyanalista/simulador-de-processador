package br.com.vinyanalista.simulador.software;

import br.com.vinyanalista.simulador.data.Byte;
import br.com.vinyanalista.simulador.data.OpCode;

public class Instruction {
	OpCode opCode;
	Byte operator;

	public Instruction(OpCode opCode, Byte operator) {
		this.opCode = opCode;
		this.operator = operator;
	}

	public OpCode getOpCode() {
		return opCode;
	}

	public Byte getOperator() {
		return operator;
	}

}