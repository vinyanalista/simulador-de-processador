package br.com.vinyanalista.simulador.software;

import java.util.ArrayList;
import java.util.List;

public class Program {
	List<Instruction> instructions = new ArrayList<Instruction>();
	String sourceCode;
	
	public List<Instruction> getInstructions() {
		return instructions;
	}

	public String getSourceCode() {
		return sourceCode;
	}
	
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	
}