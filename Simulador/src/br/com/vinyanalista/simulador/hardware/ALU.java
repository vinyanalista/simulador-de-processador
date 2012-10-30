package br.com.vinyanalista.simulador.hardware;

import br.com.vinyanalista.simulador.data.Data;

public class ALU {
	private Data in1;
	private Data in2;
	private Data out;

	public Data getIn1() {
		return in1;
	}

	public void setIn1(Data in1) {
		this.in1 = in1;
	}

	public Data getIn2() {
		return in2;
	}

	public void setIn2(Data in2) {
		this.in2 = in2;
	}

	public Data getOut() {
		return out;
	}

	public void setOut(Data out) {
		this.out = out;
	}

	public Data add() {
		out = new Data((byte) (in1.getValue() + in2.getValue()));
		return out;
	}

	public Data sub() {
		out = new Data((byte) (in1.getValue() - in2.getValue()));
		return out;
	}

	public Data not() {
		out = new Data((byte) (255 - in1.getValue()));
		return out;
	}

	private char and(char bit1, char bit2) {
<<<<<<< HEAD
		if ((bit1 == bit2) && (bit2 == '0'))
			return '0';
		else
			return '1';
	}

	public Data and() {
		String byte1 = in1.getValueAsBinary();
		String byte2 = in2.getValueAsBinary();
		String resultingByte = "";
		for (int bit = 0; bit < 8; bit++) {
			char bit1 = byte1.charAt(bit);
			char bit2 = byte2.charAt(bit);
			resultingByte += and(bit1, bit2);
		}
		if (resultingByte.charAt(0) == '0')
			out = new Data(Integer.parseInt(resultingByte, 2));
		else {
			out = new Data(Integer.parseInt(resultingByte.substring(1), 2) * -1);
		}
		return out;
	}

	private char or(char bit1, char bit2) {
		if ((bit1 == bit2) && (bit2 == '1'))
			return '1';
		else
			return '0';
	}

	public Data or() {
		String byte1 = in1.getValueAsBinary();
		String byte2 = in2.getValueAsBinary();
		String resultingByte = "";
		for (int bit = 0; bit < 8; bit++) {
			char bit1 = byte1.charAt(bit);
			char bit2 = byte2.charAt(bit);
			resultingByte += or(bit1, bit2);
		}
		if (resultingByte.charAt(0) == '0')
			out = new Data(Integer.parseInt(resultingByte, 2));
		else {
			out = new Data(Integer.parseInt(resultingByte.substring(1), 2) * -1);
		}
		return out;
=======
		if (bit1 == bit2)
			return '0';
		else
			return '1';
	}

	public Data and() {
		String byte1 = in1.getValueAsBinary();
		String byte2 = in2.getValueAsBinary();
		String resultingByte = "";
		for (int bit = 0; bit < 8; bit++) {
			char bit1 = byte1.charAt(bit);
			char bit2 = byte2.charAt(bit);
			resultingByte += and(bit1, bit2);
		}
		if (resultingByte.charAt(0) == '0')
			return new Data(Integer.parseInt(resultingByte, 2));
		else {
			return new Data(Integer.parseInt(resultingByte.substring(1), 2)
					* -1);
		}
	}

	private char or(char bit1, char bit2) {
		if (bit1 == bit2 && bit2 == '1')
			return '1';
		else
			return '0';
	}

	public Data or() {
		String byte1 = in1.getValueAsBinary();
		String byte2 = in2.getValueAsBinary();
		String resultingByte = "";
		for (int bit = 0; bit < 8; bit++) {
			char bit1 = byte1.charAt(bit);
			char bit2 = byte2.charAt(bit);
			resultingByte += or(bit1, bit2);
		}
		if (resultingByte.charAt(0) == '0')
			return new Data(Integer.parseInt(resultingByte, 2));
		else {
			return new Data(Integer.parseInt(resultingByte.substring(1), 2)
					* -1);
		}
>>>>>>> refs/remotes/origin/master
	}
}
