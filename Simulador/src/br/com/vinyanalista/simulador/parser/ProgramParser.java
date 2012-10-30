package br.com.vinyanalista.simulador.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import alice.tuprolog.InvalidTheoryException;
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
import br.com.vinyanalista.simulador.parser.ParsingError;
import br.com.vinyanalista.simulador.parser.ParsingException;
import br.com.vinyanalista.simulador.software.Program;

public class ProgramParser {
	private final Prolog engine;
	private static ProgramParser instance;

	public static void main(String[] args) throws Exception {
		Program program = ProgramParser.getParser().parseFrom(
				"JMP 12\nADD 128\nNOP");
		for (Instruction instruction : program.getInstructions())
			System.out.println(instruction);
	}

	public static ProgramParser getParser() {
		if (instance == null) {
			instance = new ProgramParser();
		}
		return instance;
	}

	private ProgramParser() {
		engine = new Prolog();
		try {
			engine.setTheory(new Theory(
					ProgramParser.class
							.getResourceAsStream("/br/com/vinyanalista/simulador/prolog/valida.pl")));
		} catch (InvalidTheoryException ite) {
			System.err.println(ite.getMessage());
			throw new RuntimeException("Erro no arquivo prolog!", ite);
		} catch (IOException io) {
			System.err.println(io.getMessage());
			throw new RuntimeException("Arquivo não encontrado!", io);
		}
	}

	private List<Instruction> getInstructions(StringBuilder builder) {
		List<Instruction> instructions = new LinkedList<Instruction>();
		if (builder.toString().equals("[]"))
			return instructions;
		while (builder.length() != 0) {
			char c = builder.charAt(0);
			while (!Character.isDigit(c)) {
				builder.deleteCharAt(0);
				c = builder.charAt(0);
			}
			int v = Integer.valueOf(builder.substring(0, builder.indexOf("|")));
			br.com.vinyanalista.simulador.data.Byte value;
			try {
				value = new Data(v);
			} catch (OutOfRangeException oore) {
				try {
					value = new DataAddress(v);
				} catch (OutOfRangeException oore2) {
					value = null;
				}
			}
			builder.delete(0, builder.indexOf("'") + 1);
			OpCode.Operation type = OpCode.Operation.valueOf(builder.substring(
					0, builder.indexOf("'")));
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
		if (builder.toString().equals("[]"))
			return errors;
		while (builder.length() != 0) {
			char c = builder.charAt(0);
			while (!Character.isDigit(c)) {
				builder.deleteCharAt(0);
				c = builder.charAt(0);
			}
			int value = Integer.valueOf(builder.substring(0,
					builder.indexOf(",")));
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
		sourceCode = sourceCode.toUpperCase();
		SolveInfo solution;
		StringBuilder builder;
		List<ParsingError> allErrors = new ArrayList<ParsingError>();
		List<Instruction> instructions = new ArrayList<Instruction>();
		try {
			StringTokenizer st = new StringTokenizer(sourceCode, "\n");
			String line;
			int lineNumber = 1;
			while (st.hasMoreTokens()) {
				line = st.nextToken();
				builder = new StringBuilder();
				builder.append("valida('").append(line)
						.append("',InstrS,ErrorS," + lineNumber + ").");
				solution = engine.solve(builder.toString());
				builder.delete(0, builder.length());
				builder.append(solution.getVarValue("ErrorS").toString());
				List<ParsingError> errors = getErrors(builder);
				if (errors.isEmpty()) {
					builder = new StringBuilder(solution.getVarValue("InstrS")
							.toString());
					instructions.addAll(getInstructions(builder));
				} else {
					allErrors.addAll(errors);
				}
				lineNumber++;
			}
			if (!allErrors.isEmpty()) {
				throw new ParsingException(allErrors);
			} else {
				return new Program(sourceCode, instructions);
			}
		} catch (MalformedGoalException mge) {
			System.err.println(mge.getMessage());
			throw new RuntimeException("Erro inesperado no prolog", mge);
		} catch (NoSolutionException nse) {
			System.err.println(nse.getMessage());
			throw new RuntimeException("Erro inesperado no prolog", nse);
		}
	}
}