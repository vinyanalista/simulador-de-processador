package br.com.vinyanalista.simulador.hardware;

import br.com.vinyanalista.simulador.software.Instruction;

public class InstructionRegister extends Register {
	private static final String NAME = "IR";
	private static final String COMPLETE_NAME = "Instruction Register";
	private static final String DESCRIPTION = "Stores the instruction being processed.";

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
}