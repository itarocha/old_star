package br.itarocha.star.model;

public enum EnumAspecto {

	CJ("cj","m","Conjun��o"),
	OP("op","n","Oposi��o" ),
	QD("qd","o","Quadratura"),
	TG("tg","p","Tr�gono"),
	SX("sx","q","Sextil");
	
	private String sigla;
	private String letra;
	private String nome;
	
	EnumAspecto(String sigla, String letra, String nome){
		this.sigla = sigla;
		this.letra = letra;
		this.nome = nome;
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
	
	public static EnumAspecto getBySigla(String sigla) {
		for (EnumAspecto x : EnumAspecto.values()) {
			if (x.getSigla().equalsIgnoreCase(sigla.toLowerCase())) {
				return x;
			}
		}
		return EnumAspecto.CJ;
	}
			
}