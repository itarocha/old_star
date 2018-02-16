package br.itarocha.star.model;

public class Cuspide {
	private int numero;
	private double posicao;
	private EnumSigno enumSigno;
	private String grau;
	private String grauNaCasa;
	private String gnc;
	private String g;
	private String m;
	private String s;
	
	public int getNumero() {
		return numero;
	}
	
	public void setNumero(int numero) {
		this.numero = numero;
	}
	
	public double getPosicao() {
		return posicao;
	}
	
	public void setPosicao(double posicao) {
		this.posicao = posicao;
	}
	
	public EnumSigno getEnumSigno() {
		return enumSigno;
	}
	
	public void setEnumSigno(EnumSigno enumSigno) {
		this.enumSigno = enumSigno;
	}

	public String getGrau() {
		return grau;
	}
	
	public void setGrau(String grau) {
		this.grau = grau;
		String tmp = grau;
		tmp = tmp.replace('.', '-');
		String[] gms = tmp.split("-");
		g = gms[0];
		m = gms[1];
		s = gms[2];
	}

	public String getGrauNaCasa() {
		return grauNaCasa;
	}
	
	public void setGrauNaCasa(String grauNaCasa) {
		this.grauNaCasa = grauNaCasa;
		String tmp = grauNaCasa;
		tmp = tmp.replace('.', '-');
		String[] gms = tmp.split("-");
		gnc = gms[0];
		m = gms[1];
		s = gms[2];
	}
	
	public String getGnc() {
		return gnc;
	}
	
	public String getG() {
		return g;
	}
	
	public String getM() {
		return m;
	}
	
	public String getS() {
		return s;
	}
}
