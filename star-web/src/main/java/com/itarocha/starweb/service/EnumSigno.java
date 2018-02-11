package com.itarocha.starweb.service;

public enum EnumSigno {
	AR("ar", "a", "Áries"),
	TO("to", "b", "Touro"),
	GE("ge", "c", "Gêmeos"),
	CA("ca", "d", "Câncer"),
	LE("le", "e", "Leão"),
	VI("vi", "f", "Virgem"),
	LI("li", "g", "Libra"),
	ES("es", "h", "Escorpião"),
	SG("sg", "i", "Sagitário"),
	CP("cp", "j", "Capricórnio"),
	AQ("aq", "k", "Aquário"),
	PE("pe", "l", "Peixes");
	
	private final String id;
	private final String letra;
	private final String nome;
	
	EnumSigno(String id, String letra, String nome){
		this.id = id;
		this.letra = letra;
		this.nome = nome;
	}

	public String getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getLetra() {
		return letra;
	}
	
	public static EnumSigno getById(String id) {
		for (EnumSigno x : EnumSigno.values()) {
			if (x.getId().equalsIgnoreCase(id.toLowerCase())) {
				return x;
			}
		}
		return EnumSigno.AR;
	}
			
}
