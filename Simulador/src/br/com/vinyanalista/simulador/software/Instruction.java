package br.com.vinyanalista.simulador.software;

import br.com.vinyanalista.simulador.data.Byte;
import br.com.vinyanalista.simulador.data.OpCode;

public class Instruction {
	OpCode opCode;
	Byte operand;

	public Instruction(OpCode opCode, Byte operand) {
		this.opCode = opCode;
		this.operand = operand;
	}

	public OpCode getOpCode() {
		return opCode;
	}

	public Byte getOperand() {
		return operand;
	}
	
	@Override
	public String toString() {
		return opCode.toString() + " " + operand;
	}

}