package br.com.vinyanalista.simulador.simulation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.com.vinyanalista.simulador.data.Byte;
import br.com.vinyanalista.simulador.data.Data;
import br.com.vinyanalista.simulador.data.DataAddress;
import br.com.vinyanalista.simulador.data.InstructionAddress;
import br.com.vinyanalista.simulador.data.OpCode;
import br.com.vinyanalista.simulador.hardware.DataMemory;
import br.com.vinyanalista.simulador.hardware.InstructionRegister;
import br.com.vinyanalista.simulador.hardware.Led;
import br.com.vinyanalista.simulador.hardware.Processor;
import br.com.vinyanalista.simulador.hardware.ProgramMemory;
import br.com.vinyanalista.simulador.hardware.Register;
import br.com.vinyanalista.simulador.simulation.Animator.AnimationListener;
import br.com.vinyanalista.simulador.software.Instruction;
import br.com.vinyanalista.simulador.software.Program;

public class Simulation implements AnimationListener {

	public interface SimulationListener {
		public void beforeStart();

		public void onProgramCrash();

		public void onProgramHalt();

		public void onRepresentationChange();

		public void onSimulationStop();
	}

	private List<SimulationListener> listeners = new ArrayList<SimulationListener>();

	public final void addSimulationListener(SimulationListener listener) {
		listeners.add(listener);
	}

	public final void removeSimulationListener(SimulationListener listener) {
		listeners.remove(listener);
	}

	private final static int STOPPED = -1;

	private Program program;
	private Processor processor;
	private DataMemory dataMemory;
	private ProgramMemory programMemory;
	private Led led;
	private List<Animation> animations;
	private Iterator<Animation> animationsIterator;

	private boolean stopped;

	private Animator animator;

	public int getInstructionIndex() {
		if (!isStopped())
			return (getInstructionAddress() / 2);
		else
			return STOPPED;
	}

	public Program getProgram() {
		return program;
	}

	public Processor getProcessor() {
		return processor;
	}

	public DataMemory getDataMemory() {
		return dataMemory;
	}

	public ProgramMemory getProgramMemory() {
		return programMemory;
	}

	public boolean isPaused() {
		return (stopped && animationsIterator != null);
	}

	public boolean isStopped() {
		return (stopped && animationsIterator == null);
	}

	public List<Animation> getAnimations() {
		return animations;
	}

	public Simulation(Program program, Animator animator) {
		this.program = program;
		this.animator = animator;
		animator.addAnimationListener(this);
		processor = new Processor();
		dataMemory = new DataMemory();
		programMemory = new ProgramMemory();
		populateProgramMemory();
		led = new Led();
		animations = new ArrayList<Animation>();
		animationsIterator = null;
		stop();
	}

	public void setRepresentation(int representation) {
		Byte.setRepresentation(representation);
		OpCode.setRepresentation(representation);
		for (SimulationListener listener : listeners)
			listener.onRepresentationChange();
	}

	public void start() {
		if (isStopped()) {
			processor.getRegister(Processor.PC).setValue(
					new InstructionAddress(0));
		}
		stopped = false;
		animate();
	}

	public void pause() {
		stopped = true;
	}

	public void stop() {
		stopped = true;
		animations.clear();
		animationsIterator = null;
		for (SimulationListener listener : listeners)
			listener.onSimulationStop();
	}

	private void populateProgramMemory() {
		int address = programMemory.getMinAddress();
		for (Instruction instruction : program.getInstructions()) {
			programMemory.writeByte(address, instruction.getOpCode());
			address++;
			programMemory.writeByte(address, instruction.getOperand());
			address++;
		}
	}

	private void process() {
		animations.clear();
		// Busca da próxima instrução
		fetchNextInstruction();
		// Incremento do CI
		incrementPC();
		// Decodificação do OpCode
		// Busca de operando
		fetchOperand();
		// Execução da instrução
		execute();
		animationsIterator = animations.iterator();
	}

	private void animate() {
		if (isPaused() || isStopped())
			return;
		else {
			if (animationsIterator == null || !animationsIterator.hasNext())
				process();
			animator.animate(animationsIterator.next());
		}
	}

	@Override
	public void onAnimationEnd() {
		animate();
	}

	private int getInstructionAddress() {
		return getRegister(Processor.PC).getValue().getValue();
	}

	private InstructionRegister getInstructionRegister() {
		return ((InstructionRegister) getRegister(Processor.IR));
	}

	private Instruction getInstruction() {
		return getInstructionRegister().getInstruction();
	}

	private Register getRegister(String name) {
		return processor.getRegister(name);
	}

	private void setMar(Byte address) {
		animations.add(new Animation(AnimationType.MAR_CHANGE, address));
		getRegister(Processor.MAR).setValue(address);
	}

	private void setMbr(Byte data) {
		animations.add(new Animation(AnimationType.MBR_CHANGE, data));
		getRegister(Processor.MBR).setValue(data);
	}

	private void readFromMemory(int addressToBeRead) {
		Byte address;
		Byte readByte;
		if ((addressToBeRead >= DataAddress.MIN_VALUE)
				&& (addressToBeRead <= DataAddress.MAX_VALUE)) {
			address = new DataAddress(addressToBeRead);
			readByte = dataMemory.readByte(addressToBeRead);
		} else {
			address = new InstructionAddress(addressToBeRead);
			readByte = programMemory.readByte(addressToBeRead);
		}
		setMar(address);
		animations.add(new Animation(AnimationType.MAR_TO_MEMORY, address));
		animations.add(new Animation(AnimationType.MEMORY_TO_MBR, readByte));
		setMbr(readByte);
	}

	private void incrementPC() {
		animations.add(new Animation(AnimationType.STATUS_PC_INCREMENT, null));
		getRegister(Processor.PC).setValue(
				new InstructionAddress(getRegister(Processor.PC).getValue()
						.getValue() + 1));
		animations.add(new Animation(AnimationType.PC_CHANGE, getRegister(
				Processor.PC).getValue()));
	}

	private void fetchNextInstruction() {
		animations.add(new Animation(AnimationType.STATUS_FETCH_INSTRUCTION,
				null));
		animations.add(new Animation(AnimationType.UPDATE_INSTRUCTION, null));
		animations.add(new Animation(AnimationType.PC_TO_MAR, getRegister(
				Processor.PC).getValue()));
		readFromMemory(getRegister(Processor.PC).getValue().getValue());
		animations.add(new Animation(AnimationType.MBR_TO_IR_OPCODE,
				getRegister(Processor.MBR).getValue()));
		animations.add(new Animation(AnimationType.IR_OPCODE_CHANGE,
				getRegister(Processor.MBR).getValue()));
		OpCode opCode = new OpCode(getRegister(Processor.MBR).getValue()
				.getValue());
		getRegister(Processor.PC).setValue(
				new InstructionAddress(getRegister(Processor.PC).getValue()
						.getValue() + 1));
		animations.add(new Animation(AnimationType.PC_CHANGE, getRegister(
				Processor.PC).getValue()));
		animations.add(new Animation(AnimationType.PC_TO_MAR, getRegister(
				Processor.PC).getValue()));
		readFromMemory(getRegister(Processor.PC).getValue().getValue());
		animations.add(new Animation(AnimationType.MBR_TO_IR_OPERAND,
				getRegister(Processor.MBR).getValue()));
		animations.add(new Animation(AnimationType.IR_OPERAND_CHANGE,
				getRegister(Processor.MBR).getValue()));
		Byte operand = getRegister(Processor.MBR).getValue();
		getInstructionRegister().setInstruction(
				new Instruction(opCode, operand));
	}

	private void setAcc(Data data) {
		animations.add(new Animation(AnimationType.ACC_CHANGE, data));
		getRegister(Processor.ACC).setValue(data);
	}

	private void setAlu1(Data data) {
		animations.add(new Animation(AnimationType.ALU_IN_1_CHANGE, data));
		processor.getALU().setIn1(data);
	}

	private void setAlu2(Data data) {
		animations.add(new Animation(AnimationType.ALU_IN_2_CHANGE, data));
		processor.getALU().setIn2(data);
	}

	private void fetchOperand() {
		animations.add(new Animation(AnimationType.STATUS_FETCH_OPERAND, null));
		switch (getInstruction().getOpCode().getValue()) {
		case OpCode.ADD_OPCODE:
			readFromMemory(getInstruction().getOperand().getValue());
			animations.add(new Animation(AnimationType.ACC_TO_ALU_IN_1,
					getRegister(Processor.ACC).getValue()));
			setAlu1((Data) getRegister(Processor.ACC).getValue());
			animations.add(new Animation(AnimationType.MBR_TO_ACC, getRegister(
					Processor.MBR).getValue()));
			setAcc((Data) getRegister(Processor.MBR).getValue());
			animations.add(new Animation(AnimationType.ACC_TO_ALU_IN_2,
					getRegister(Processor.ACC).getValue()));
			setAlu2((Data) getRegister(Processor.ACC).getValue());
			break;
		case OpCode.OUT_OPCODE:
			readFromMemory(getInstruction().getOperand().getValue());
			// TODO melhorar busca de operando da instrução OUT
		default:
			break;
		}
	}

	private void execute() {
		animations.add(new Animation(AnimationType.STATUS_EXECUTE, null));
		switch (getInstruction().getOpCode().getValue()) {
		case OpCode.LDI_OPCODE:
			animations.add(new Animation(AnimationType.IR_OPERAND_TO_ACC,
					getInstruction().getOperand()));
			setAcc((Data) getInstruction().getOperand());
			break;
		case OpCode.STA_OPCODE:
			animations.add(new Animation(AnimationType.IR_OPERAND_TO_MAR,
					getInstruction().getOperand()));
			setMar(getInstruction().getOperand());
			animations.add(new Animation(AnimationType.ACC_TO_MBR, getRegister(
					Processor.ACC).getValue()));
			setMbr((Data) getRegister(Processor.ACC).getValue());
			animations.add(new Animation(AnimationType.MAR_TO_MEMORY,
					getRegister(Processor.MAR).getValue()));
			animations.add(new Animation(AnimationType.MBR_TO_MEMORY,
					getRegister(Processor.MBR).getValue()));
			dataMemory.writeByte(getRegister(Processor.MAR).getValue()
					.getValue(), new Data(getRegister(Processor.MBR).getValue()
					.getValue()));
			break;
		case OpCode.LDA_OPCODE:
			animations.add(new Animation(AnimationType.IR_OPERAND_TO_MAR,
					getInstruction().getOperand()));
			readFromMemory(getInstruction().getOperand().getValue());
			animations.add(new Animation(AnimationType.MBR_TO_ACC, getRegister(
					Processor.MBR).getValue()));
			setAcc((Data) getRegister(Processor.MBR).getValue());
			break;
		case OpCode.ADD_OPCODE:
			processor.getALU().add();
			animations.add(new Animation(AnimationType.ALU_OUTPUT_CHANGE,
					processor.getALU().getOut()));
			animations.add(new Animation(AnimationType.ALU_OUTPUT_TO_ACC,
					processor.getALU().getOut()));
			setAcc((Data) processor.getALU().getOut());
			break;
		case OpCode.OUT_OPCODE:
			animations.add(new Animation(AnimationType.MBR_TO_LED, getRegister(
					Processor.MBR).getValue()));
			animations.add(new Animation(AnimationType.LED_CHANGE, getRegister(
					Processor.MBR).getValue()));
			led.setValue(getRegister(Processor.MBR).getValue());
			break;
		default:
			break;
		}
	}

}