package br.com.vinyanalista.simulador.simulation;

import java.util.ArrayList;
import java.util.List;

import br.com.vinyanalista.simulador.data.OpCode;
import br.com.vinyanalista.simulador.hardware.DataMemory;
import br.com.vinyanalista.simulador.hardware.Processor;
import br.com.vinyanalista.simulador.hardware.ProgramMemory;
import br.com.vinyanalista.simulador.software.Instruction;
import br.com.vinyanalista.simulador.software.Program;

public class Simulation {
	private Program program;
	private ProgramMemory programMemory;
	private List<InstructionAnimation> instructions = new ArrayList<InstructionAnimation>();

	public Program getProgram() {
		return program;
	}

	public ProgramMemory getProgramMemory() {
		return programMemory;
	}

	public List<InstructionAnimation> getInstructions() {
		return instructions;
	}

	private void generateSimulation() {
		// TODO Implementar geração de animações conforme programa a ser
		// simulado
		InstructionAnimation lastAnimation = null;
		InstructionAnimation animation;
		for (Instruction instruction : program.getInstructions()) {
			if (lastAnimation == null)
				animation = new InstructionAnimation(instruction,
						new Processor(), new DataMemory());
			else
				animation = new InstructionAnimation(instruction,
						lastAnimation.getFinalProcessorState(),
						lastAnimation.getFinalDataMemoryState());
			switch (instruction.getOpCode().getValue()) {
			case OpCode.LDA_OPCODE:

				break;

			default:
				break;
			}
		}
	}

	private void populateProgramMemory() {
		int address = programMemory.getMinAddress();
		for (Instruction instruction : program.getInstructions()) {
			programMemory.writeByte(address, instruction.getOpCode());
			address++;
			programMemory.writeByte(address, instruction.getOperator());
			address++;
		}
	}

	public Simulation(Program program) {
		this.program = program;
		programMemory = new ProgramMemory();
		populateProgramMemory();
		generateSimulation();
	}
}