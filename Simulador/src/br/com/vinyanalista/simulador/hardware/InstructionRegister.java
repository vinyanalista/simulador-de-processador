package br.com.vinyanalista.simulador.hardware;

import br.com.vinyanalista.simulador.data.Byte;
import br.com.vinyanalista.simulador.software.Instruction;

public class InstructionRegister extends Register {
	static final String NAME = "IR";
	static final String COMPLETE_NAME = "Instruction Register";
	static final String DESCRIPTION = "Stores the instruction being processed.";

	private Instruction instruction;

	public Instruction getInstruction() {
		return instruction;
	}

	public void setInstruction(Instruction instruction) {
		this.instruction = instruction;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getCompleteName() {
		return COMPLETE_NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public Byte getValue() {
		return null;
	}
	
	@Override
	public Register setValue(Byte value) {
		return null;
	}
}