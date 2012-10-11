package br.com.vinyanalista.simulador.hardware;

import java.util.HashMap;

public class Processor {
	public static final String ACC = "ACC";
	public static final String PC = "PC";
	public static final String MAR = "MAR";
	public static final String MBR = "MBR";
	public static final String IR = "IR";
	
	private HashMap<String, Register> registers;
	
	public Register getRegister(String name) {
		return registers.get(name);
	}
	
}