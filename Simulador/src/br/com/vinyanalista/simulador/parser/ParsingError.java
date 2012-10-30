package br.com.vinyanalista.simulador.parser;

public class ParsingError {

	private int lineNumber;
	private ErrorType type;

	public ParsingError(ErrorType type, int lineNumber) {
		this.type = type;
		this.lineNumber = lineNumber;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}

	public ErrorType getType() {
		return type;
	}

	public enum ErrorType {
		INVALID_DATA_ADDRESS,
		INVALID_INSTRUCTION_ADDRESS,
		INVALID_DATA,
		OPERATION_DOES_NOT_REQUIRE_VALUE,
		INVALID_OPCODE;
	}
	
	@Override
	public String toString() {
		return "Erro " + getType() + " na linha " + getLineNumber();
	}
}