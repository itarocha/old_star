package com.itarocha.starweb.model;

public enum TipoAspecto {

    XX("*** Descrição ***"),
    CJ("Conjunção"),
    TG("Trígono"),
    SX("Sextil"),
    QD("Quadratura"),
    OP("Oposição");
	
	private String descricao;
	
	TipoAspecto(String descricao){
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
	public static String getByString(String texto) {
		for (TipoAspecto t: TipoAspecto.values()) {
			if (t.toString().equalsIgnoreCase(texto)) {
				return t.getDescricao();
			}
		}
		return texto;
	}
	
}
