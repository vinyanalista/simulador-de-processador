package br.com.vinyanalista.simulador.examples;

import br.com.vinyanalista.simulador.data.Data;
import br.com.vinyanalista.simulador.data.DataAddress;
import br.com.vinyanalista.simulador.data.InstructionAddress;
import br.com.vinyanalista.simulador.data.OpCode;
import br.com.vinyanalista.simulador.software.Instruction;
import br.com.vinyanalista.simulador.software.Program;

public class Examples {

	private static final Program add() {
		// LDI 2
		// STA 128 ;A = 2
		// LDI 3
		// STA 129 ;B = 3
		// LDA 128
		// ADD 129
		// STA 130 ;C = A + B = 5
		// OUT 130
		// HLT

		Program program = new Program();
		program.setSourceCode(null);
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.LDI_OPCODE), new Data(2)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.STA_OPCODE), new DataAddress(
						128)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.LDI_OPCODE), new Data(3)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.STA_OPCODE), new DataAddress(
						129)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.LDA_OPCODE), new DataAddress(
						128)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.ADD_OPCODE), new DataAddress(
						129)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.STA_OPCODE), new DataAddress(
						130)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.OUT_OPCODE), new DataAddress(
						130)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.HLT_OPCODE), null));
		return program;
	}

	private static final Program negative() {
		// LDI 2
		// STA 128 ;A = 2
		// LDI 3
		// STA 129 ;B = 3
		// LDA 128
		// SUB 129
		// JN 16
		// HLT
		// LDI 1
		// STA 130 ;C = 1
		// OUT 130
		// HLT

		Program program = new Program();
		program.setSourceCode(null);
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.LDI_OPCODE), new Data(2)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.STA_OPCODE), new DataAddress(
						128)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.LDI_OPCODE), new Data(3)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.STA_OPCODE), new DataAddress(
						129)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.LDA_OPCODE), new DataAddress(
						128)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.SUB_OPCODE), new DataAddress(
						129)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.JN_OPCODE),
						new InstructionAddress(16)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.HLT_OPCODE), null));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.LDI_OPCODE), new Data(1)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.STA_OPCODE), new DataAddress(
						130)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.OUT_OPCODE), new DataAddress(
						130)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.HLT_OPCODE), null));
		return program;
	}

	private static final Program overflow() {
		// LDI 127
		// STA 128 ;A = 127
		// LDI 1
		// ADD 128
		// STA 129 ;B = A + 1 = -128
		// OUT 129
		// HLT

		Program program = new Program();
		program.setSourceCode(null);
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.LDI_OPCODE), new Data(127)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.STA_OPCODE), new DataAddress(
						128)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.LDI_OPCODE), new Data(1)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.ADD_OPCODE), new DataAddress(
						128)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.STA_OPCODE), new DataAddress(
						129)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.OUT_OPCODE), new DataAddress(
						129)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.HLT_OPCODE), null));
		return program;
	}

	private static final Program not() {
		// LDI 255
		// NOT
		// STA 128 ;A = NOT(11111111) = 00000000
		// OUT 128
		// HLT

		Program program = new Program();
		program.setSourceCode(null);
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.LDI_OPCODE), new Data(-128)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.NOT_OPCODE), null));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.STA_OPCODE), new DataAddress(
						128)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.OUT_OPCODE), new DataAddress(
						128)));
		// program.getInstructions().add(
		// new Instruction(new OpCode(OpCode.HLT_OPCODE), null));
		return program;
	}

	private static final Program crash() {
		// NOP

		Program program = new Program();
		program.setSourceCode(null);
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.NOP_OPCODE), null));
		return program;
	}

	private static final Program and() {
		// LDI 3
		// STA 128 ;A = 00000011
		// LDI 5
		// STA 129 ;B = 00000101
		// LDA 128
		// AND 129
		// STA 130 ;C = A and B = 00000111
		// OUT 130
		// HLT

		Program program = new Program();
		program.setSourceCode(null);
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.LDI_OPCODE), new Data(3)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.STA_OPCODE), new DataAddress(
						128)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.LDI_OPCODE), new Data(5)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.STA_OPCODE), new DataAddress(
						129)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.LDA_OPCODE), new DataAddress(
						128)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.AND_OPCODE), new DataAddress(
						129)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.STA_OPCODE), new DataAddress(
						130)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.OUT_OPCODE), new DataAddress(
						130)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.HLT_OPCODE), null));
		return program;
	}

	private static final Program or() {
		// LDI 3
		// STA 128 ;A = 00000011
		// LDI 5
		// STA 129 ;B = 00000101
		// LDA 128
		// AND 129
		// STA 130 ;C = A or B = 00000001
		// OUT 130
		// HLT

		Program program = new Program();
		program.setSourceCode(null);
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.LDI_OPCODE), new Data(3)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.STA_OPCODE), new DataAddress(
						128)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.LDI_OPCODE), new Data(5)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.STA_OPCODE), new DataAddress(
						129)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.LDA_OPCODE), new DataAddress(
						128)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.OR_OPCODE), new DataAddress(
						129)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.STA_OPCODE), new DataAddress(
						130)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.OUT_OPCODE), new DataAddress(
						130)));
		program.getInstructions().add(
				new Instruction(new OpCode(OpCode.HLT_OPCODE), null));
		return program;
	}

	public static final Program getExample(Example name) {
		switch (name) {
		case ADD:
			return add();
		case NEGATIVE:
			return negative();
		case OVERFLOW:
			return overflow();
		case NOT:
			return not();
		case CRASH:
			return crash();
		case AND:
			return and();
		case OR:
			return or();
		default:
			return null;
		}
	}

}