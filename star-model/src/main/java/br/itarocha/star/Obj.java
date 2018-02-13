package br.itarocha.star;

public class Obj {
	
	private String nome;
	private Integer grau;
	private Integer gg;
	private Integer mm;
	
	public Obj(String nome, Integer grau, Integer gg, Integer mm) {
		this.nome = nome;
		this.grau = grau;
		this.gg = gg;
		this.mm = mm;
	}

	public String getNome() {
		return nome;
	}

	public Integer getGrau() {
		return grau;
	}

	public Integer getGg() {
		return gg;
	}

	public Integer getMm() {
		return mm;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setGrau(Integer grau) {
		this.grau = grau;
	}

	public void setGg(Integer gg) {
		this.gg = gg;
	}

	public void setMm(Integer mm) {
		this.mm = mm;
	}

}
