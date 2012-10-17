package br.com.vinyanalista.simulador.simulation;

import java.util.ArrayList;
import java.util.List;

import br.com.vinyanalista.simulador.hardware.ProgramMemory;
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
	}

	private void populateProgramMemory() {
		// TODO Implementar método que povoa a memória de programa com o
		// programa a ser simulado
	}

	public Simulation(Program program) {
		this.program = program;
		populateProgramMemory();
		generateSimulation();
	}
}