package br.com.vinyanalista.simulador.software;

import br.com.vinyanalista.simulador.data.Byte;
import br.com.vinyanalista.simulador.data.OpCode;

public class Instruction {
	
	
	private OpCode opCode;
	private Byte operator;
	
	public OpCode getOpCode() {
		return opCode;
	}
	
	public void setOpCode(OpCode opCode) {
		this.opCode = opCode;
	}
	
	public Byte getOperator() {
		return operator;
	}
	
	public void setOperator(Byte operator) {
		this.operator = operator;
	}

}