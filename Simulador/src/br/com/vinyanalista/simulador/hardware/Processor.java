package br.com.vinyanalista.simulador.hardware;

import java.util.HashMap;

import br.com.vinyanalista.simulador.data.InstructionAddress;

public class Processor {
	public static final String ACC = Register.ACC_NAME;
	public static final String PC = Register.PC_NAME;
	public static final String MAR = Register.MAR_NAME;
	public static final String MBR = Register.MBR_NAME;
	public static final String IR = InstructionRegister.NAME;

	private ALU alu = new ALU();
	private HashMap<String, Register> registers = new HashMap<String, Register>();

	public ALU getALU() {
		return alu;
	}

	public Register getRegister(String name) {
		return registers.get(name);
	}

	public Processor() {
		registers.put(
				ACC,
				new Register().setName(Register.ACC_NAME)
						.setCompleteName(Register.ACC_COMPLETE_NAME)
						.setDescription(Register.ACC_DESCRIPTION));
		registers.put(
				PC,
				new Register().setName(Register.PC_NAME)
						.setCompleteName(Register.PC_COMPLETE_NAME)
						.setDescription(Register.PC_DESCRIPTION));
		registers.put(
				MAR,
				new Register().setName(Register.MAR_NAME)
						.setCompleteName(Register.MAR_COMPLETE_NAME)
						.setDescription(Register.MAR_DESCRIPTION));
		registers.put(
				MBR,
				new Register().setName(Register.MBR_NAME)
						.setCompleteName(Register.MBR_COMPLETE_NAME)
						.setDescription(Register.MBR_DESCRIPTION));
		registers.put(IR, new InstructionRegister());
	}
}