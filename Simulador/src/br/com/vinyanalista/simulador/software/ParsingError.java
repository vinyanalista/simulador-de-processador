package br.com.vinyanalista.simulador.software;

public class ParsingError {
	
	public enum ErrorType {
		INVALID_DATA_ADDRESS;
	}
	
	private int lineNumber;
	private ErrorType type;

}