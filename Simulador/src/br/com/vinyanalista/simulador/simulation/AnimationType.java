package br.com.vinyanalista.simulador.simulation;

public enum AnimationType {
	PC_TO_MAR,
	MAR_CHANGE,
	MAR_TO_MEMORY,
	MEMORY_TO_MBR,
	MBR_CHANGE,
	MBR_TO_IR_OPCODE,
	MBR_TO_IR_OPERAND,
	IR_OPCODE_CHANGE,
	IR_OPERAND_CHANGE,
	PC_CHANGE,
	IR_OPERAND_TO_MAR,
	MBR_TO_ACC,
	ACC_CHANGE,
	ACC_TO_ALU_IN_1,
	ALU_IN_1_CHANGE,
	ACC_TO_ALU_IN_2,
	ALU_IN_2_CHANGE,
	ALU_OUTPUT_CHANGE,
	ALU_OUTPUT_TO_ACC,
	IR_OPERAND_TO_ACC,
	MBR_TO_MEMORY,
	ACC_TO_MBR,
	MBR_TO_LED,
	LED_CHANGE,
	STATUS_FETCH_INSTRUCTION,
	STATUS_PC_INCREMENT,
	STATUS_FETCH_OPERAND,
	STATUS_EXECUTE,
	UPDATE_INSTRUCTION,
	IR_OPERAND_TO_PC;	
}