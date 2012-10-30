package br.com.vinyanalista.simulador.parser;

@SuppressWarnings("serial")
public class ParsingException extends Exception {
	private java.util.List<ParsingError> list;
	
	public ParsingException(ParsingError error) {
		super(error.toString());
		list = new java.util.LinkedList<ParsingError>();
	}
	
	public ParsingException(java.util.List<ParsingError> errors) {
		super(generateMessage(errors));
		list = errors;
	}
	
	private static String generateMessage(java.util.List<ParsingError> errors) {
		StringBuilder builder = new StringBuilder();
		for (ParsingError err : errors) {
			builder.append(err.toString());
		}
		return builder.toString();
	}
	
	public java.util.List<ParsingError> getErrors() {
		return list;
	}
}