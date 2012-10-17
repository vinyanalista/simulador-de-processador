package br.com.vinyanalista.simulador.hardware;

public class Register extends ProcessorUnit {
	public String ACC_NAME = "ACC";
	public String ACC_COMPLETE_NAME = "";
	public String ACC_DESCRIPTION = "Not defined yet.";
	
	public String PC_NAME = "PC";
	public String PC_COMPLETE_NAME = "Program Counter";
	public String PC_DESCRIPTION = "Not defined yet.";
	
	public String MAR_NAME = "MAR";
	public String MAR_COMPLETE_NAME = "Memory Address Register";
	public String MAR_DESCRIPTION = "Not defined yet.";
	
	public String MBR_NAME = "MBR";
	public String MBR_COMPLETE_NAME = "Memory Buffer Register";
	public String MBR_DESCRIPTION = "Not defined yet.";
	
	private String name;
	private String completeName;
	private String description;

	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getCompleteName() {
		return completeName;
	}
	
	public void setCompleteName(String completeName) {
		this.completeName = completeName;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	private Byte value;

	public Byte getValue() {
		return value;
	}

	public void setValue(Byte value) {
		this.value = value;
	}
}