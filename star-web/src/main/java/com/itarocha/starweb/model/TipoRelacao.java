package com.itarocha.starweb.model;

public enum TipoRelacao {

    MESTRE("Principal"),
    DETALHE("Dependente");
	
	private String descricao;
	
	TipoRelacao(String descricao){
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
}
