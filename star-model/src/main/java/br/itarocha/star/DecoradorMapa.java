package br.itarocha.star;

import java.text.DecimalFormat;

import br.itarocha.star.model.Cuspide;
import br.itarocha.star.model.ItemAspecto;
import br.itarocha.star.model.PlanetaAspecto;
import br.itarocha.star.model.PlanetaPosicao;
import br.itarocha.star.util.Funcoes;

public class DecoradorMapa {

	private Mapa mapa;
	
	public DecoradorMapa(Mapa mapa){
		this.mapa = mapa;
	}
	
	public String getJSON(){
		String retorno = "";
		retorno += displayCabecalho();
		retorno += ",\n";
		retorno += displayPlanetasNosSignos();
		retorno += ",\n";
		retorno += displayPlanetasNasCasas();
		retorno += ",\n";
		retorno += displayCuspides();
		retorno += ",\n";
		retorno += displayAspectos();
		retorno = "{"+retorno+"}";
		return retorno;
	}
    
    private String displayCabecalho(){
    	double latitude = mapa.getLatitude().Coordenada2Graus();
    	double longitude = mapa.getLongitude().Coordenada2Graus();	
    	////////int signoAscendente = (int)(casas[1] / 30)+1;
    	String retorno = "";
    	
    	String lon = (longitude > 0 ? "E" : "W");
    	String lat = (latitude > 0 ? "N" : "S");
    	
    	retorno += String.format("\"dados_pessoais\": {\"nome\": \"%s\", "+
    								"\"data\":\"%s\", "+
    								"\"hora\":\"%s\", "+
    								"\"deltaT\":\"%s\", "+
    								"\"julDay\":\"%s\", "+
    								"\"lat\":\"%s\", "+
    								"\"lon\":\"%s\"}",
    							mapa.getNome(),
    							mapa.getData(),
    							mapa.getHora(),
    							
    							new DecimalFormat("#.#").format(mapa.getDeltaTSec()),
    							new DecimalFormat("#.######").format(mapa.getJulDay()),
    							
    							Funcoes.grau(latitude)+lat,
    							Funcoes.grau(longitude)+lon
    			);
    	
		return retorno;
		
		// VEJA MAIS TARDE
		//System.out.println("Ayanamsa: " + CartaUtil.grau(ayanamsa) + " (" + sw.swe_get_ayanamsa_name(SID_METHOD) + ")");
		///////System.out.println("Ascendente: " + CartaUtil.grauNaCasa(casas[1])+" "+signos[signoAscendente-1]);
    }
	
    
    /*
    {"planetas_signos":[
      {"planeta":"sol", "signo":"ca", "gg":"07", "mm":"39"},
      {"planeta":"lua", "signo":"aq", "gg":"07", "mm":"27"},
      {"planeta":"mer", "signo":"le", "gg":"00", "mm":"56"},
      {"planeta":"ven", "signo":"ge", "gg":"20", "mm":"08"},
      {"planeta":"mar", "signo":"le", "gg":"00", "mm":"25"},
      {"planeta":"jup", "signo":"cp", "gg":"02", "mm":"52"},
      {"planeta":"sat", "signo":"ge", "gg":"13", "mm":"39"},
      {"planeta":"ura", "signo":"li", "gg":"14", "mm":"13"},
      {"planeta":"net", "signo":"sg", "gg":"03", "mm":"00"},
      {"planeta":"plu", "signo":"vi", "gg":"29", "mm":"25"}
    ]}
    */
	private String displayPlanetasNosSignos(){
		String retorno = "";
		
		//System.out.println("\nPLANETAS");
		for(PlanetaPosicao pp : mapa.getPosicoesPlanetas()){
			retorno += String.format("{\"planeta\":\"%s\", \"signo\":\"%s\", \"grau\": \"%s\", \"gg\":\"%s\", \"mm\":\"%s\", \"ss\":\"%s\"},\n",
					pp.getSiglaPlaneta(), 	// Planeta
					pp.getNomeSigno(),		// Signo
					pp.getGrau(),
					pp.getGnc(),					// gg 
					pp.getM(),					// mm 
					pp.getS()					// ss
					);
		}
		retorno =  "\"planetas_signos\":[\n"+
					retorno.substring(0,retorno.length()-2)+
					"\n]";
		return retorno;
	}

	private String displayPlanetasNasCasas(){
		String retorno = "";
		
		for(PlanetaPosicao pp : mapa.getPosicoesPlanetas()){
			retorno += String.format("{\"planeta\":\"%s\", \"casa\":\"%02d\", \"grau\":\"%s\", \"gg\":\"%s\", \"mm\":\"%s\", \"ss\":\"%s\"},\n",
					pp.getSiglaPlaneta(), 	// Planeta
					(int)pp.getCasa(),		   // Casa
					pp.getGrau(),
					pp.getGnc(),					// gg 
					pp.getM(),					// mm 
					pp.getS()
					);
		}
		retorno =  "\"planetas_casas\":[\n"+
				  retorno.substring(0,retorno.length()-2)+
				  "\n]";
		return retorno;
	}

	private String displayCuspides(){ 
		String retorno = "";
		for (Cuspide c: mapa.getListaCuspides() ){
			if (c.getNumero() > 12) { break; }
			
			String g = c.getGrau();
			String gnc = c.getGrauNaCasa();
			gnc = gnc.replace('.', '-');
			String[] gms = gnc.split("-");
			
			retorno += String.format("{\"casa\":\"%02d\", \"signo\":\"%s\", \"grau\": \"%s\", \"gg\":\"%s\", \"mm\":\"%s\", \"ss\":\"%s\"},\n",
					c.getNumero(), 	// Casa
					c.getSigno(),	// Signo
					g,
					gms[0],			// gg 
					gms[1],			// mm 
					gms[2]			// ss
					);
		}
		retorno =  "\"cuspides\":[\n"+
				  retorno.substring(0,retorno.length()-2)+
				  "\n]";
		return retorno;
	}

	private String displayAspectos(){
		String retorno = "";
		for(ItemAspecto ite : mapa.getListaAspectos()){
			PlanetaAspecto pA = ite.getPlanetaA();
			PlanetaAspecto pB = ite.getPlanetaB();
			
			retorno += String.format("{\"planeta_origem\":\"%s\", \"planeta_destino\":\"%s\", \"aspecto\":\"%s\"},\n",
					pA.getSigla(), 
					pB.getSigla(),
					ite.getAspecto()
					);
		}
		retorno = "\"aspectos\":[\n"+
				  retorno.substring(0,retorno.length()-2)+
				  "\n]";
		return retorno;
	}
	
}
