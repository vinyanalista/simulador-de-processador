package br.com.vinyanalista.simulador.data;

public class OutOfRangeException extends IllegalArgumentException {
	private static final long serialVersionUID = 1L;
	private final long value, min, max;

	public OutOfRangeException(long value, long min, long max) {
		super("Value " + value + " out of range [" + min + ".." + max
				+ "]");
		this.value = value;
		this.min = min;
		this.max = max;
	}
	
	public OutOfRangeException(String message, OutOfRangeException cause) {
		super(message, cause);
		this.value = cause.value;
		this.min = cause.min;
		this.max = cause.max;
	}

	public long getValue() {
		return value;
	}

	public long getMin() {
		return min;
	}

	public long getMax() {
		return max;
	}
}