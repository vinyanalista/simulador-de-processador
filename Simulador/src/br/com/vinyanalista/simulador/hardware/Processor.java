package br.com.vinyanalista.simulador.hardware;

import java.util.HashMap;

public class Processor {
	public static final String ACC = "ACC";
	public static final String PC = "PC";
	public static final String MAR = "MAR";
	public static final String MBR = "MBR";
	public static final String IR = "IR";

	private ALU alu = new ALU();
	private HashMap<String, Register> registers = new HashMap<String, Register>();

	public ALU getALU() {
		return alu;
	}

	public Register getRegister(String name) {
		return registers.get(name);
	}

	public Processor() {
		registers.put(ACC, new Register());
		registers.put(PC, new Register());
		registers.put(MAR, new Register());
		registers.put(MBR, new Register());
		registers.put(IR, new InstructionRegister());
	}
}