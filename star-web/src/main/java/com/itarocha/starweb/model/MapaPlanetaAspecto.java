package com.itarocha.starweb.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity()
public class MapaPlanetaAspecto {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private TipoPlaneta planetaOrigem;

	@Enumerated(EnumType.STRING)
	private TipoPlaneta planetaDestino;
	
	@Enumerated(EnumType.STRING)
	private TipoAspecto aspecto;

	@Enumerated(EnumType.STRING)
	private TipoRelacao tipoRelacao;
	
	@ManyToOne
	@JoinColumn(name = "id_mestre")
	private MapaPlanetaAspecto aspectoMestre;
	
	@Lob 
	@Basic(fetch=FetchType.LAZY)
	private String texto;
	
	@Transient
	private String descricaoResumida;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoPlaneta getPlanetaOrigem() {
		return planetaOrigem;
	}

	public void setPlanetaOrigem(TipoPlaneta planetaOrigem) {
		this.planetaOrigem = planetaOrigem;
	}

	public TipoPlaneta getPlanetaDestino() {
		return planetaDestino;
	}

	public void setPlanetaDestino(TipoPlaneta planetaDestino) {
		this.planetaDestino = planetaDestino;
	}

	public TipoAspecto getAspecto() {
		return aspecto;
	}

	public void setAspecto(TipoAspecto aspecto) {
		this.aspecto = aspecto;
	}

	public TipoRelacao getTipoRelacao() {
		return tipoRelacao;
	}

	public void setTipoRelacao(TipoRelacao tipoRelacao) {
		this.tipoRelacao = tipoRelacao;
	}

	public MapaPlanetaAspecto getAspectoMestre() {
		return aspectoMestre;
	}

	public void setAspectoMestre(MapaPlanetaAspecto aspectoMestre) {
		this.aspectoMestre = aspectoMestre;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getDescricaoResumida() {
		return String.format("%s %s %s", this.planetaOrigem.getDescricao(), this.getAspecto().getDescricao(), this.planetaDestino.getDescricao()).toUpperCase();
	}	
}
