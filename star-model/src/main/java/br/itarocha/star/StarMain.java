package br.itarocha.star;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.itarocha.star.model.Cidade;

public class StarMain {

	//http://th-mack.de/international/download/index.html
	//http://www.radixpro.org/fix-east-west.html
	public static void main( String[] args) {
		// Rode com os seguintes argumentos
		// "Itamar Rocha" "29/06/1972" "05:00" "Caxias" "MA"
		
		String[] argumentos = new String[5];
		String nome;
		String data;
		String hora;
		String cidade;
		String uf;
		if (args.length < 5){
			System.out.println("Entre com os 5 par�metros com espa�o");
			System.out.println("Nome");
			System.out.println("Data de nascimento formato DD/MM/AAAA");
			System.out.println("Hora de nascimento qualquer formato com ou sem segundos com delimitador \".\" ou \":\" HH:MM OU HH.MM.SS");
			System.out.println("Cidade de nascimento");
			System.out.println("UF de nascimento");
			nome = "Itamar Rocha";
			data = "29/06/1972";
			hora = "05:00";
			cidade = "Caxias";
			uf = "MA";
		} else {
			nome = args[0];
			data = args[1];
			hora = args[2];
			cidade = args[3];
			uf = args[4];
		}
		
		StarMain m = new StarMain();
		try {
			Mapa mapa = m.buildMapa(nome, data, hora, cidade, uf);
			if (mapa != null) {
	    		String json = new DecoradorMapa(mapa).getJSON();
	    		//Grafico.build(mapa);
	    		ChartPainter cp = new ChartPainter(mapa);
	    		System.out.println(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public StarMain(){}

	// ("Itamar","29/06/1972","5.0.0", Caxias, MA
	public Mapa buildMapa(String nome, String data, String hora, String cidade, String uf ) throws Exception{
		Mapa retorno = null;
		MapaBuilder construtor = MapaBuilder.getInstance();
		Cidade c = MapeadorCidades.getInstance().getCidade(cidade, uf);
		if (c != null){
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date d = sdf.parse(data);
    		retorno = construtor.build(nome, d, hora, c);
		} else {
			System.out.println("N�o conseguiu localizar cidade");
		}
		return retorno;
	}
	
}
/*
Tempo Sideral 23:36:35
SOL 07 Ca 39.47
LUA 07 Aq 27.55
MER 00 Le 56.03
VEN 20 Ge 08.45 R
MAR 00 Le 25.07
JUP 02 Cp 52.35 R
SAT 13 Ge 39.17
URA 14 Li 13.08
NET 03 Sg 00.16 R
PLU 29 Vg 25.17
*/

// Site Teoria da Conspira��o - Link de mapa
// http://www.deldebbio.com.br/
// http://www.viraj.com.br/
// http://www.sadhana.com.br/cgi-local/mapas/mapanow.cgi


/*	
SP - 19/09/2014 - 12:16 UTC (9:16)
ver mapa
Sol 26� Vg 29
Lua 04� Le 30
Mer 22� Li 39
V�n 17� Vg 06
Mar 03� Sg 46
J�p 13� Le 51
Sat 19� Es 27
Ura 15� �r 14 R
Net 05� Pe 38 R
Plu 10� Cp 59 R
Nod 20� Li 25 R
Lil 22� Le 15
Qui 14� Pe 52 R
*/


