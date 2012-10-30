package br.com.vinyanalista.simulador.parser;

import java.util.LinkedList;
import java.util.List;

import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;
import br.com.vinyanalista.simulador.data.Data;
import br.com.vinyanalista.simulador.data.DataAddress;
import br.com.vinyanalista.simulador.data.OpCode;
import br.com.vinyanalista.simulador.data.OutOfRangeException;
import br.com.vinyanalista.simulador.software.Instruction;
import br.com.vinyanalista.simulador.software.Program;

public class ProgramParser {
	private Prolog engine;

	private static ProgramParser instance;

	private ProgramParser() {
		engine = new Prolog();
		try {
			engine.setTheory(new Theory(
					ProgramParser.class
							.getResourceAsStream("/br/com/vinyanalista/simulador/prolog/valida.pl")));
		} catch (Exception e) {
			throw new RuntimeException("Erro no arquivo prolog", e);
		}
	}

	private List<Instruction> getInstructions(StringBuilder builder) {
		List<Instruction> instructions = new LinkedList<Instruction>();
		while (builder.length() != 0) {
			for (char c = builder.charAt(0); !Character.isDigit(c); c = builder
					.charAt(0)) {
				builder.deleteCharAt(0);
			}
			int v = Integer.valueOf(builder.substring(0, builder.indexOf("|")));
			br.com.vinyanalista.simulador.data.Byte value;
			try {
				value = new Data(v);
			} catch (OutOfRangeException oore) {
				value = new DataAddress(v);
			}
			builder.delete(0, builder.indexOf("|") + 1);
			OpCode.Operation type = OpCode.Operation.valueOf(builder.substring(
					0, builder.indexOf("]")));
			instructions.add(new Instruction(new OpCode(type), value));
			int index = builder.indexOf("[");
			if (index < 0) {
				index = builder.length();
			}
			builder.delete(0, index);
		}

		return instructions;
	}

	private List<ParsingError> getErrors(StringBuilder builder) {
		List<ParsingError> errors = new LinkedList<ParsingError>();
		while (builder.length() != 0) {
			for (char c = builder.charAt(0); !Character.isDigit(c); c = builder
					.charAt(0)) {
				builder.deleteCharAt(0);
			}
			int value = Integer.valueOf(builder.substring(0,
					builder.indexOf("|")));
			builder.delete(0, builder.indexOf("'") + 1);
			ParsingError.ErrorType type = ParsingError.ErrorType
					.valueOf(builder.substring(0, builder.indexOf("'")));
			errors.add(new ParsingError(type, value));
			int index = builder.indexOf("[");
			if (index < 0) {
				index = builder.length();
			}
			builder.delete(0, index);
		}
		return errors;
	}

	public Program parseFrom(String sourceCode) throws ParsingException {
		SolveInfo solution;
		StringBuilder builder;
		List<ParsingError> errors;
		List<Instruction> instructions;
		try {
			builder = new StringBuilder();
			builder.append("valida_bloco('").append(sourceCode)
					.append("',X,Y).");
			System.out.println(builder);
			solution = engine.solve(builder.toString());
			builder.delete(0, builder.length());
			builder.append(solution.getVarValue("X").toString());
			errors = getErrors(builder);
			if (!errors.isEmpty()) {
				throw new ParsingException(errors);
			}
			builder.delete(0, builder.length()).append(
					solution.getVarValue("InstrS").toString());
			instructions = getInstructions(builder);
		} catch (MalformedGoalException mge) {
			System.err.println(mge.getMessage());
			throw new RuntimeException("Erro inesperado no prolog", mge);
		} catch (NoSolutionException nse) {
			System.err.println(nse.getMessage());
			throw new RuntimeException("Erro inesperado no prolog", nse);
		}
		return new Program(sourceCode, instructions);
	}

	public static ProgramParser getParser() {
		if (instance == null) {
			instance = new ProgramParser();
		}
		return instance;
	}

}