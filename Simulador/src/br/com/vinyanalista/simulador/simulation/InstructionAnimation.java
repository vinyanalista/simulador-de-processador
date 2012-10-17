package br.com.vinyanalista.simulador.simulation;

import java.util.ArrayList;
import java.util.List;

import br.com.vinyanalista.simulador.hardware.DataMemory;
import br.com.vinyanalista.simulador.hardware.Processor;
import br.com.vinyanalista.simulador.software.Instruction;

public class InstructionAnimation {
	private Instruction instruction;
	private Processor initialProcessorState;
	private Processor finalProcessorState;
	private DataMemory initialDataMemoryState;
	private List<SingleAnimation> animations = new ArrayList<SingleAnimation>();

	public List<SingleAnimation> getAnimations() {
		return animations;
	}

	public Instruction getInstruction() {
		return instruction;
	}

	public Processor getInitialProcessorState() {
		return initialProcessorState;
	}

	public Processor getFinalProcessorState() {
		return finalProcessorState;
	}

	public DataMemory getInitialDataMemoryState() {
		return initialDataMemoryState;
	}

	private void generateAnimations() {
		// TODO Implementar geracao de animações conforme instrução a ser
		// animada
	}

	public InstructionAnimation(Instruction instruction,
			Processor initialProcessorState, DataMemory initialDataMemoryState) {
		this.instruction = instruction;
		this.initialProcessorState = initialProcessorState;
		this.initialDataMemoryState = initialDataMemoryState;
		generateAnimations();
	}
}