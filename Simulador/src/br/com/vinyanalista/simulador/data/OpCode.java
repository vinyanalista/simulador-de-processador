package br.com.vinyanalista.simulador.data;

public class OpCode extends Byte {
	private Operation operation;

	public enum Operation {
		NOP(0, "NOP"), STA(1, "STA"), LDA(2, "LDA"), ADD(3, "ADD"), SUB(4,
				"SUB"), OR(5, "OR"), AND(6, "AND"), NOT(7, "NOT"), JMP(8, "JMP"), JN(
				9, "JN"), JZ(10, "JZ"), JNZ(11, "JNZ"), IN(12, "IN"), OUT(13,
				"OUT"), LDI(14, "LDI"), HLT(15, "HLT");

		private int code;
		private String mnemonic;

		private Operation(int code, String mnemonic) {
			this.code = code;
			this.mnemonic = mnemonic;
		}

		public int getCode() {
			return code;
		}

		public String getMnemonic() {
			return mnemonic;
		}
	}

	public static final int REPRESENTATION_MNEMONIC = 4;

	protected static int preferredRepresentation = REPRESENTATION_MNEMONIC;

	public static void setRepresentation(int representation) {
		switch (representation) {
		case REPRESENTATION_RECOMMENDED:
			preferredRepresentation = REPRESENTATION_MNEMONIC;
			break;
		case REPRESENTATION_DECIMAL:
		case REPRESENTATION_HEX:
		case REPRESENTATION_BINARY:
		case REPRESENTATION_MNEMONIC:
			preferredRepresentation = representation;
			break;
		default:
			throw new IllegalArgumentException();
		}
	}

	public static final int MIN_VALUE = 0;
	public static final int MAX_VALUE = 15;

	public static final int NOP_OPCODE = 0;
	public static final int STA_OPCODE = 1;
	public static final int LDA_OPCODE = 2;
	public static final int ADD_OPCODE = 3;
	public static final int SUB_OPCODE = 4;
	public static final int OR_OPCODE = 5;
	public static final int AND_OPCODE = 6;
	public static final int NOT_OPCODE = 7;
	public static final int JMP_OPCODE = 8;
	public static final int JN_OPCODE = 9;
	public static final int JZ_OPCODE = 10;
	public static final int JNZ_OPCODE = 11;
	public static final int IN_OPCODE = 12;
	public static final int OUT_OPCODE = 13;
	public static final int LDI_OPCODE = 14;
	public static final int HLT_OPCODE = 15;

	public static final String NOP_MNEMONIC = "NOP";
	public static final String STA_MNEMONIC = "STA";
	public static final String LDA_MNEMONIC = "LDA";
	public static final String ADD_MNEMONIC = "ADD";
	public static final String SUB_MNEMONIC = "SUB";
	public static final String OR_MNEMONIC = "OR";
	public static final String AND_MNEMONIC = "AND";
	public static final String NOT_MNEMONIC = "NOT";
	public static final String JMP_MNEMONIC = "JMP";
	public static final String JN_MNEMONIC = "JN";
	public static final String JZ_MNEMONIC = "JZ";
	public static final String JNZ_MNEMONIC = "JNZ";
	public static final String IN_MNEMONIC = "IN";
	public static final String OUT_MNEMONIC = "OUT";
	public static final String LDI_MNEMONIC = "LDI";
	public static final String HLT_MNEMONIC = "HLT";

	public OpCode() {
		super(MIN_VALUE, MAX_VALUE);
	}

	@Deprecated
	public OpCode(int value) {
		super(value);
	}

	public OpCode(Operation operation) {
		super(operation.getCode());
		this.operation = operation;
	}

	@Deprecated
	public static final String toMnemonic(int value) {
		switch (value) {
		case NOP_OPCODE:
			return NOP_MNEMONIC;
		case STA_OPCODE:
			return STA_MNEMONIC;
		case LDA_OPCODE:
			return LDA_MNEMONIC;
		case ADD_OPCODE:
			return ADD_MNEMONIC;
		case SUB_OPCODE:
			return SUB_MNEMONIC;
		case OR_OPCODE:
			return OR_MNEMONIC;
		case AND_OPCODE:
			return AND_MNEMONIC;
		case NOT_OPCODE:
			return NOT_MNEMONIC;
		case JMP_OPCODE:
			return JMP_MNEMONIC;
		case JN_OPCODE:
			return JN_MNEMONIC;
		case JZ_OPCODE:
			return JZ_MNEMONIC;
		case JNZ_OPCODE:
			return JNZ_MNEMONIC;
		case IN_OPCODE:
			return IN_MNEMONIC;
		case OUT_OPCODE:
			return OUT_MNEMONIC;
		case LDI_OPCODE:
			return LDI_MNEMONIC;
		case HLT_OPCODE:
			return HLT_MNEMONIC;
		default:
			throw new IllegalArgumentException();
		}
	}

	@Override
	protected String getValueAsRepresentation(int representation) {
		switch (representation) {
		case REPRESENTATION_DECIMAL:
		case REPRESENTATION_HEX:
		case REPRESENTATION_BINARY:
			return super.getValueAsRepresentation(representation);
		case REPRESENTATION_RECOMMENDED:
		case REPRESENTATION_MNEMONIC:
			return getValueAsMnemonic();
		default:
			throw new IllegalArgumentException();
		}
	}

	@Deprecated
	public String getValueAsMnemonic() {
		return toMnemonic(value);
	}

	@Override
	public String getValueAsPreferredRepresentation() {
		return getValueAsRepresentation(preferredRepresentation);
	}

	@Override
	public String getValueAsRecommendedRepresentation() {
		return getValueAsRepresentation(REPRESENTATION_RECOMMENDED);
	}

	@Override
	public void setValue(int value) {
		setValue(value, MIN_VALUE, MAX_VALUE);
	}

	// @Override
	// public void setValueAsBinary(String value) {
	// setValueAsBinary(value, MIN_VALUE, MIN_VALUE);
	// }
	//
	// @Override
	// public void setValueAsHex(String value) {
	// setValueAsHex(value, MIN_VALUE, MIN_VALUE);
	// }

	@Override
	public String toString() {
		return this.operation.toString();
	}

}