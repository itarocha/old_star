package br.itarocha.star.model;

public class PlanetaPosicao {
	private String nomePlaneta;
	private String siglaPlaneta;
	private String nomeSigno;
	private String grau;
	private String grauNaCasa;
	private double posicao;
	private boolean retrogrado;
	private double latitude;
	private double distancia;
	private double direcao;
	private String gnc;
	private String g;
	private String m;
	private String s;
	
	private double casa;
	
	public String getNomePlaneta() {
		return nomePlaneta;
	}
	public void setNomePlaneta(String nomePlaneta) {
		this.nomePlaneta = nomePlaneta;
	}
	public String getNomeSigno() {
		return nomeSigno;
	}
	public void setSiglaPlaneta(String siglaPlaneta) {
		this.siglaPlaneta = siglaPlaneta;
	}
	public String getSiglaPlaneta() {
		return siglaPlaneta;
	}
	public void setNomeSigno(String nomeSigno) {
		this.nomeSigno = nomeSigno;
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
	public double getPosicao() {
		return posicao;
	}
	public void setPosicao(double posicao) {
		this.posicao = posicao;
	}
	public boolean isRetrogrado() {
		return retrogrado;
	}
	public void setRetrogrado(boolean retrogrado) {
		this.retrogrado = retrogrado;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getDistancia() {
		return distancia;
	}
	public void setDistancia(double distancia) {
		this.distancia = distancia;
	}
	public double getDirecao() {
		return direcao;
	}
	public void setDirecao(double direcao) {
		this.direcao = direcao;
	}
	
	public String getStatusRetrogrado(){
		return this.isRetrogrado() ? "R" : "D";
	}
	public double getCasa() {
		return this.casa;
	}
	public void setCasa(double casa) {
		this.casa = casa;
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
