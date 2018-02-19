package br.itarocha.star;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.itarocha.star.model.Cidade;
import br.itarocha.star.model.Cuspide;
import br.itarocha.star.model.EnumPlaneta;
import br.itarocha.star.model.EnumSigno;
import br.itarocha.star.model.ItemAspecto;
import br.itarocha.star.model.PlanetaPosicao;
import br.itarocha.star.util.Funcoes;
import swisseph.SweConst;
import swisseph.SweDate;
import swisseph.SwissEph;


public class MapaBuilder {
	private SweDate sweDate; 

	private int[] aspectos_planetas = new int[18];
	private double[] aspectos_posicoes = new double[18];
	private double[] casas= new double[23];
	private static SwissEph sw;
	private double ayanamsa;

	private static final String FORMATO_DATA = "dd/MM/yyyy";
	private static final int SID_METHOD = SweConst.SE_SIDM_LAHIRI;
	
	private static MapaBuilder instance = null;
	
	public static MapaBuilder getInstance() {
		if (instance == null) instance = new MapaBuilder();
		return instance;
	}
	
	static {
		String path = MapeadorCidades.class.getProtectionDomain().getCodeSource().getLocation().getPath()+"/ephe";
		sw = new SwissEph();
		sw.swe_set_ephe_path(path);
	}
	
	public Mapa build(String nome, String data, String hora, String cidade, String uf) {
		MapeadorCidades mapeador = MapeadorCidades.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(FORMATO_DATA);
		Date d;
		try {
			d = sdf.parse(data);
		} catch (ParseException e) {
			d = Calendar.getInstance().getTime();
		}

		Cidade c = mapeador.getCidade(cidade, uf);
		if (c != null) {
			return build(nome, d, hora, c);
		}
		return null;
	}

	public Mapa build(String nome, Date data, String hora, String cidade, String uf) {
		MapeadorCidades mapeador = MapeadorCidades.getInstance();
		Cidade c = mapeador.getCidade(cidade, uf);
		if (c != null) {
			return build(nome, data, hora, c);
		} else {
			System.out.println("Cidade não encontrada");
		}
		return null;
	}

	// Principal
	public Mapa build(String nome, Date data, String hora, Cidade cidade){
		SimpleDateFormat sdf = new SimpleDateFormat(FORMATO_DATA);
		String d = sdf.format(data);
		Mapa m = new Mapa(nome, d, hora, cidade);
		calcular(m); 
		return m;
	}
	
	// TODO: Deve retornar uma classe Mapa
	private void calcular(Mapa mapa){
		this.sweDate = new SweDate(mapa.getAnoUT(),mapa.getMesUT(),mapa.getDiaUT(), mapa.getHoraDouble() );
		this.sweDate.setCalendarType(this.sweDate.SE_GREG_CAL, this.sweDate.SE_KEEP_DATE);
		this.ayanamsa = this.sw.swe_get_ayanamsa_ut(this.sweDate.getJulDay());
		
		double tmp = sweDate.getDeltaT(sweDate.getJulDay());
		
		System.out.println(tmp);
		
		mapa.setDeltaTSec(this.sweDate.getDeltaT() * 86400);
		mapa.setJulDay(this.sweDate.getJulDay()+tmp);
		
		buildCasas(mapa);
		buildPlanetas(mapa);
		buildAspectos(mapa);
	}

	// Do the coordinate calculation for this planet p
	// x2[0] = longitude (Planeta)
	// x2[1] = latitude
	// x2[2] = distância
	// x2[3] = velocidade do planeta em longitude // Se negativo, retrógrado
	// x2[4] = velodicade em latitude
	// x2[5] = velocidade em distância???
	private void buildPlanetas(Mapa mapa){
		int signo;
		//String nomeSigno = "";
		long iflag, iflgret;
		iflag = SweConst.SEFLG_SPEED;

		double tjd, te;
		tjd=sweDate.getJulDay();
		te = tjd + sweDate.getDeltaT(tjd);
		double x[]=new double[6];
		double x2[]=new double[6];
		StringBuffer serr=new StringBuffer();
		boolean retrogrado = false;
		int idxpos = -1;

		mapa.getPosicoesPlanetas().clear();
		
		iflgret = sw.swe_calc(te, SweConst.SE_ECL_NUT, (int)iflag, x, serr);
		
		// O último era SE_CHIRON
		for(int xis = 0; xis <= 9; xis++){
			//Planeta planeta = mapPlanetas.get(xis);
			
			EnumPlaneta enumPlaneta = EnumPlaneta.getByCodigo(xis);
			
			iflgret = sw.swe_calc(te, xis, (int)iflag, x2, serr);
			// if there is a problem, a negative value is returned and an errpr message is in serr.
			if (iflgret < 0)
				System.out.print("error: "+serr.toString()+"\n");
			else if (iflgret != iflag)
				System.out.print("warning: iflgret != iflag. "+serr.toString()+"\n");
		  
			//print the coordinates
			signo = (int)(x2[0] / 30); // + 1;
			//house = (sign + 12 - signoAscendente) % 12 +1;
			retrogrado = (x2[3] < 0);
		  
			// Atualizando posições para cálculo de aspectos
			idxpos++;
			aspectos_planetas[idxpos] = enumPlaneta.getCodigo(); //planeta.getId();
			aspectos_posicoes[idxpos] = x2[0]; 			
			
			PlanetaPosicao pp = new PlanetaPosicao();

			pp.setEnumPlaneta(enumPlaneta);
			pp.setEnumSigno(EnumSigno.getByCodigo(signo));
			
			pp.setPosicao(x2[0]);
			pp.setGrau( Funcoes.grau(x2[0]) );
			pp.setGrauNaCasa( Funcoes.grauNaCasa(x2[0]) );
			pp.setRetrogrado(retrogrado);
			pp.setLatitude(x2[1]);
			pp.setDistancia(x2[2]);
			pp.setDirecao(x2[3]);
			
			double _geolat = mapa.getLatitude().Coordenada2Graus(); // ok
			double _armc = mapa.getSideralTime(); // ok
			double _eps_true = x[0];
			
	        double hpos = sw.swe_house_pos(_armc, _geolat, _eps_true, 'P', x2, serr);
			
			pp.setCasaDouble(hpos);

			mapa.getPosicoesPlanetas().add(pp);
		}
		
		// Ascendente e Meio do Céu
		aspectos_planetas[10] = SweConst.SE_ASC;
		aspectos_posicoes[10] = casas[1];

		aspectos_posicoes[11] = casas[10];
		aspectos_planetas[11] = SweConst.SE_MC;
	}
	
	// Fabricando Cúspides
	// Depende do cálculo das casas
	private void buildCasas(Mapa mapa){
		int sign;
		mapa.getListaCuspides().clear();
		
		this.casas = this.getHouses(this.sw, 
									mapa, 
									sweDate.getJulDay(), 
									mapa.getLatitude().Coordenada2Graus(),
									mapa.getLongitude().Coordenada2Graus() );
		
		for (int i = 1; i < 21; i++){
			sign = (int)(casas[i] / 30); // + 1;
			
			Cuspide cuspide = new Cuspide();
			cuspide.setNumero(i);
			cuspide.setPosicao(casas[i]);
			cuspide.setGrau(Funcoes.grau(casas[i]));
			cuspide.setGrauNaCasa( Funcoes.grauNaCasa(casas[i]) );
			cuspide.setEnumSigno(EnumSigno.getByCodigo(sign));
			mapa.getListaCuspides().add(cuspide);
		}

		int intGrauDef = 0;
    	if (!mapa.getListaCuspides().isEmpty()) {
    		String grauDef = mapa.getListaCuspides().get(0).getGrau();
    		grauDef = grauDef.replace('.', '-');
    		String[] gms = grauDef.split("-");
    		intGrauDef = Integer.parseInt(gms[0]);
    		
    		//System.out.println("A DEFASAGEM É DE "+gms[0]);
    		//System.out.println("A DEFASAGEM É DE "+intGrauDef);
    	}
    	mapa.setGrausDefasagemAscendente(intGrauDef);
	}
	
	// Fabricando de Aspectos
	private void buildAspectos(Mapa mapa){
		mapa.getListaAspectos().clear();
		String aspecto;
		for (int x=0; x < 11; x++){
			for(int y=x+1; y < 12; y++){
				EnumPlaneta eA = EnumPlaneta.getByCodigo(x);
				EnumPlaneta eB = EnumPlaneta.getByCodigo(y);
				
				aspecto = Funcoes.buildAspect(eA.getSigla(),eB.getSigla(),aspectos_posicoes[x], aspectos_posicoes[y]);
				if (aspecto != ""){
					ItemAspecto item = new ItemAspecto();
					
					item.getPlanetaA().setEnumPlaneta(EnumPlaneta.getByCodigo(x));
					
					item.getPlanetaA().setPosicao(aspectos_posicoes[x]);

					item.getPlanetaB().setEnumPlaneta(EnumPlaneta.getByCodigo(y));
					item.getPlanetaB().setPosicao(aspectos_posicoes[y]);
					
					item.setAspecto(aspecto);
					item.getPlanetaA().setCoordenada(x);
					item.getPlanetaB().setCoordenada(y);

					item.getPlanetaA().setGrau(Funcoes.grau(aspectos_posicoes[x]));
					item.getPlanetaB().setGrau(Funcoes.grau(aspectos_posicoes[y]));
					
					mapa.getListaAspectos().add(item);
				}
			}
		} // end aspecto
	}
	
    /// <summary>
    /// Calculate houses
    /// </summary>
    /// <param name="jdnr">Julian day number</param>
    /// <param name="lat">Geographical latitude</param>
    /// <param name="lon">Geographical longitude</param>
    /// <param name="system">Index to define housesystem</param>
    /// <returns>Array of doubles with with the following values:
    ///  0: not used, 1..12 cusps 1..12, 13: asc., 14: MC, 15: ARMC, 16: Vertex,
    ///  17: Equatorial asc., 18: co-ascendant (Koch), 19: co-ascendant(Munkasey),
    ///  20: polar ascendant 
	///
	///  yy[0] = ascendant
	///  yy[1] = mc
	///  yy[2] = armc (= sidereal time) !!!!!
	///  yy[3] = vertex
	///  yy[4] = equatorial ascendant
	///  yy[5] = co-ascendant (Walter Koch)
	///  yy[6] = co-ascendant (Michael Munkasey)
	///  yy[7] = polar ascendant (Michael Munkasey)
	///  yy[8] = reserved for future use
	///  yy[9] = reserved for future use
    ///</returns>
    private double[] getHouses(SwissEph sw, Mapa mapa, double jdnr, double lat, double lon) {
        double[] xx = new double[13];
        double[] yy = new double[10];
        double[] zz = new double[23];
        int flag = sw.swe_houses(jdnr, SweConst.SEFLG_SPEED, lat, lon, 'P', xx, yy);
        
        mapa.setSideralTime(yy[2]);
        
        for (int i = 0; i < 13; i++) {
            zz[i] = xx[i];
        }
        for (int i = 0; i < 10; i++) {
            zz[i + 13] = yy[i];
        }
        return zz;
    }
}

//http://www.timeanddate.com/worldclock/switzerland/zurich
//http://www.sadhana.com.br/cgi-local/mapas/mapanow.cgi?indic=10003&ref=http%3A//www.deldebbio.com.br/
// http://www.astrosage.com/astrology/ayanamsa-calculator.asp
//http://www.astro.com/swisseph/swephprg.htm#_Toc471829052


//SweConst.SE_MEAN_APOG 
//SweConst.SE_OSCU_APOG
//SweConst.SE_MEAN_NODE
//SweConst.SE_CHIRON
//get the name of the planet p
//snam=sw.swe_get_planet_name(p);



//%-12s %10.7f\t %10.7f\t %10.7f\t
//pp.getSiglaPlaneta()
//pp.getLatitude(),			// Latitude
//pp.getDistancia(),   		// Distância
//pp.getDirecao()   		// Direção