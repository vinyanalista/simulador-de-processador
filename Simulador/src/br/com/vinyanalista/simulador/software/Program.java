package br.com.vinyanalista.simulador.software;

import java.util.ArrayList;
import java.util.List;

public class Program {
	private List<Instruction> instructions;
	private String sourceCode;
	
	public Program(String sourceCode, List<Instruction> instructions) {
		this.sourceCode = sourceCode;
		this.instructions = instructions;
	}
	
	@Deprecated
	public Program() {
		instructions = new ArrayList<Instruction>();
		sourceCode = null;
	}

	public List<Instruction> getInstructions() {
		return instructions;
	}

	public String getSourceCode() {
		return sourceCode;
	}
	
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Instruction instruction : instructions) {
			builder.append(instruction).append('\n');
		}
		return builder.toString();
	}
	
}