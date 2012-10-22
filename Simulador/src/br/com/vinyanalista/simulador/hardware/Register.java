package br.com.vinyanalista.simulador.hardware;

import br.com.vinyanalista.simulador.data.Byte;
import br.com.vinyanalista.simulador.data.Data;

public class Register extends ProcessorUnit {
	public static final String ACC_NAME = "ACC";
	public static final String ACC_COMPLETE_NAME = "";
	public static final String ACC_DESCRIPTION = "Not defined yet.";

	public static final String PC_NAME = "PC";
	public static final String PC_COMPLETE_NAME = "Program Counter";
	public static final String PC_DESCRIPTION = "Not defined yet.";

	public static final String MAR_NAME = "MAR";
	public static final String MAR_COMPLETE_NAME = "Memory Address Register";
	public static final String MAR_DESCRIPTION = "Not defined yet.";

	public static final String MBR_NAME = "MBR";
	public static final String MBR_COMPLETE_NAME = "Memory Buffer Register";
	public static final String MBR_DESCRIPTION = "Not defined yet.";

	private String name;
	private String completeName;
	private String description;
	private Byte value;

	public Register() {
		value = new Data();
	}

	@Override
	public String getName() {
		return name;
	}

	public Register setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public String getCompleteName() {
		return completeName;
	}

	public Register setCompleteName(String completeName) {
		this.completeName = completeName;
		return this;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public Register setDescription(String description) {
		this.description = description;
		return this;
	}

	public Byte getValue() {
		return value;
	}

	public Register setValue(Byte value) {
		this.value = value;
		return this;
	}
}