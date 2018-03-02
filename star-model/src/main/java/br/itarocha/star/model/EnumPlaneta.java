package br.itarocha.star.model;

public enum EnumPlaneta {
	SOL(0, "sol", "A", "Sol"),
	LUA(1, "lua", "B", "Lua"),
	MER(2, "mer", "C", "Mercúrio"),
	VEN(3, "ven", "D", "Vênus"),
	MAR(4, "mar", "E", "Marte"),
	JUP(5, "jup", "F", "Júpiter"),
	SAT(6, "sat", "G", "Saturno"),
	URA(7, "ura", "H", "Urano"),
	NET(8, "net", "I", "Netuno"),
	PLU(9, "plu", "J", "Plutão"),
	//TND(10, "tnd", "L", "Nodo Norte"),
	ASC(10, "asc", "P", "Ascendente"),
	MCE(11, "mce", "Q", "Meio do Céu");
	//SND("snd", "M", "Nodo Sul");
	
	/*
	mapPlanetas.add(new Planeta(SweConst.SE_SUN, "sol", "Sol", "A"));
	mapPlanetas.add(new Planeta(SweConst.SE_MOON, "lua", "Lua", "B"));
	mapPlanetas.add(new Planeta(SweConst.SE_MERCURY, "mer", "Merc�rio", "C"));
	mapPlanetas.add(new Planeta(SweConst.SE_VENUS, "ven", "V�nus", "D"));
	mapPlanetas.add(new Planeta(SweConst.SE_MARS, "mar", "Marte", "E"));
	mapPlanetas.add(new Planeta(SweConst.SE_JUPITER, "jup", "J�piter", "F")); 
	mapPlanetas.add(new Planeta(SweConst.SE_SATURN, "sat", "Saturno", "G"));
	mapPlanetas.add(new Planeta(SweConst.SE_URANUS, "ura", "Urano", "H")); 
	mapPlanetas.add(new Planeta(SweConst.SE_NEPTUNE, "net", "Netuno", "I")); 
	mapPlanetas.add(new Planeta(SweConst.SE_PLUTO, "plu", "Plut�o", "J"));
	mapPlanetas.add(new Planeta(SweConst.SE_TRUE_NODE, "nor", "N�dulo Norte", "L"));
	mapPlanetas.add(new Planeta(SweConst.SE_ASC, "asc", "Ascendente", "P"));
	mapPlanetas.add(new Planeta(SweConst.SE_MC, "mce", "Meio do C�u", "Q"));
	*/
	
	private Integer codigo;
	private String sigla;
	private String letra;
	private String nome;
	
	EnumPlaneta(Integer codigo, String sigla, String letra, String nome){
		this.codigo = codigo;
		this.sigla = sigla;
		this.letra = letra;
		this.nome = nome;
	}

	public Integer getCodigo() {
		return this.codigo;
	}

	public String getSigla() {
		return sigla;
	}

	public String getNome() {
		return nome;
	}

	public String getLetra() {
		return letra;
	}

	public static EnumPlaneta getBySigla(String sigla) {
		for (EnumPlaneta x : EnumPlaneta.values()) {
			if (x.getSigla().equalsIgnoreCase(sigla.toLowerCase())) {
				return x;
			}
		}
		return EnumPlaneta.SOL;
	}

	public static EnumPlaneta getByCodigo(Integer codigo) {
		for (EnumPlaneta x : EnumPlaneta.values()) {
			if (x.getCodigo().equals(codigo)) {
				return x;
			}
		}
		return EnumPlaneta.SOL;
	}
	

			
}
