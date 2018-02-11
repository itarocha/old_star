package com.itarocha.starweb.service;

public enum EnumPlaneta {
	SOL("sol", "A", "Sol"),
	LUA("lua", "B", "Lua"),
	MER("mer", "C", "Mercúrio"),
	VEN("ven", "D", "Vênus"),
	MAR("mar", "E", "Marte"),
	JUP("jup", "F", "Júpiter"),
	SAT("sat", "G", "Saturno"),
	URA("ura", "H", "Urano"),
	NET("net", "I", "Netuno"),
	PLU("plu", "J", "Plutão"),
	TND("tnd", "L", "Nodo Norte"),
	SND("snd", "M", "Nodo Sul");
	
	private final String id;
	private final String letra;
	private final String nome;
	
	EnumPlaneta(String id, String letra, String nome){
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

	public static EnumPlaneta getById(String id) {
		for (EnumPlaneta x : EnumPlaneta.values()) {
			if (x.getId().equalsIgnoreCase(id.toLowerCase())) {
				return x;
			}
		}
		return EnumPlaneta.SOL;
	}

	

			
}
