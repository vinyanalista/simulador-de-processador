package br.com.vinyanalista.simulador.software;

import br.com.vinyanalista.simulador.data.Data;
import br.com.vinyanalista.simulador.data.DataAddress;
import br.com.vinyanalista.simulador.data.OpCode;

public class ProgramParser {
	public static Program parseFrom(String sourceCode) {
		// TODO Implementar parser

		//Programa exemplo da soma
		// LDI 2
		// STA 127 ;A = 2
		// LDI 3
		// STA 128 ;B = 3
		// LDA 127
		// ADD 128
		// STA 129 ;C = A + B = 5
		// OUT 129;

		Program program = new Program();
		program.instructions.add(new Instruction(new OpCode(OpCode.LDI_OPCODE),
				new Data(2)));
		program.instructions.add(new Instruction(new OpCode(OpCode.STA_OPCODE),
				new DataAddress(127)));
		program.instructions.add(new Instruction(new OpCode(OpCode.LDI_OPCODE),
				new Data(3)));
		program.instructions.add(new Instruction(new OpCode(OpCode.STA_OPCODE),
				new DataAddress(128)));
		program.instructions.add(new Instruction(new OpCode(OpCode.LDA_OPCODE),
				new DataAddress(127)));
		program.instructions.add(new Instruction(new OpCode(OpCode.ADD_OPCODE),
				new DataAddress(128)));
		program.instructions.add(new Instruction(new OpCode(OpCode.STA_OPCODE),
				new DataAddress(129)));
		program.instructions.add(new Instruction(new OpCode(OpCode.OUT_OPCODE),
				new DataAddress(129)));
		return null;
	}
}