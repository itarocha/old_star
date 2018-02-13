package br.itarocha.star;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

public class ChartPainter {

	private static final int SIZE = 600;
	private static final int MARGEM = 10;
	private static final int MARGEM_CASA = 60;  
	private static final int MARGEM_INTERNA = 300;  
	private static final int MARGEM_ASPECTOS = 360;  
	
	
	private static final int HORIZONTAL_SIZE = 640;
	private static final int VERTICAL_SIZE = 640;
	private static final int DISTANCE_SHAMA = HORIZONTAL_SIZE / 2 - 35;
	 
	private static final int DISTANCE_ALFA = HORIZONTAL_SIZE / 2 - 50;
	private static final int DISTANCE_BETA = HORIZONTAL_SIZE / 4 - 40;
	private static final int BIG_DOT = 8;
	private static final int PQ_DOT = 5;
	private Integer grauDefasagemAscendente = 0;
	
	public List<Obj> lstPlanetas = new ArrayList<Obj>();
	public List<Obj> lstCuspides = new ArrayList<Obj>();
		
	public ChartPainter() {
		preencherListas();
		desenharTudo();
	}
	  
  static public void main(String args[]) throws Exception {
	  ChartPainter d = new ChartPainter();
  }

  private void preencherListas() {
	  grauDefasagemAscendente = 26;
	  // Planetas
	  lstPlanetas.add(new Obj("E", 125, 05, 06));  
	  lstPlanetas.add(new Obj("A", 200, 20, 19));  
	  lstPlanetas.add(new Obj("C", 215, 05, 32));  
	  lstPlanetas.add(new Obj("F", 227, 17, 40));  
	  lstPlanetas.add(new Obj("D", 228, 18, 00));  
	  lstPlanetas.add(new Obj("J", 236, 26, 32));  
	  lstPlanetas.add(new Obj("I", 290, 20, 36));  
	  lstPlanetas.add(new Obj("H", 292, 22, 26));  
	  lstPlanetas.add(new Obj("B", 316, 16, 11));  
	  lstPlanetas.add(new Obj("G", 336, 06, 16));
	  
	  lstCuspides.add(new Obj("a",  26, 26, 05));  
	  lstCuspides.add(new Obj("b",  55, 25, 28));  
	  lstCuspides.add(new Obj("c",  85, 25, 16));  
	  lstCuspides.add(new Obj("d", 115, 25, 42));  
	  lstCuspides.add(new Obj("e", 146, 26, 55));  
	  lstCuspides.add(new Obj("f", 177, 27, 42));  
	  lstCuspides.add(new Obj("g", 206, 26, 05));  
	  lstCuspides.add(new Obj("h", 235, 25, 28));  
	  lstCuspides.add(new Obj("i", 265, 25, 16));  
	  lstCuspides.add(new Obj("j", 295, 25, 42));  
	  lstCuspides.add(new Obj("k", 326, 26, 55));  
	  lstCuspides.add(new Obj("l", 357, 27, 42));  
	  
  }
  
  private void desenharTudo() {
	try {
	    // TYPE_INT_ARGB specifies the image format: 8-bit RGBA packed
	    // into integer pixels
	    BufferedImage bi = new BufferedImage(HORIZONTAL_SIZE, VERTICAL_SIZE, BufferedImage.TYPE_INT_ARGB);
	
	    Graphics2D g = bi.createGraphics();
	    
	    //Graphics2D g = (Graphics2D) gg;
	    g.setRenderingHint(	RenderingHints.KEY_ANTIALIASING,
	            			RenderingHints.VALUE_ANTIALIAS_ON);
	
	    drawFace(g);
	    desenharPosicoesPlanetas(g);
	
	    ImageIO.write(bi, "PNG",  new File("d:\\temp\\imagem\\IMAGEM.PNG"));
	    //ImageIO.write(bi, "JPEG", new File("d:\\temp\\imagem\\yourImageName.JPG"));
	    //ImageIO.write(bi, "GIF",  new File("d:\\temp\\imagem\\IMAGEM.GIF"));
	    //ImageIO.write(bi, "BMP",  new File("d:\\temp\\imagem\\yourImageName.BMP"));
	    System.out.println("Prontinho!");
	  } catch (IOException ie) {
	    ie.printStackTrace();
	  }
  }
  
  private void drawFace(Graphics2D g) {
      g.setStroke(new BasicStroke(1));
      g.setColor(Color.white );
      g.fillOval(MARGEM, MARGEM, SIZE, SIZE);
      
      g.setColor(Color.black);
      // Círculo
      g.drawOval(MARGEM, MARGEM, SIZE, SIZE);
      
      g.setColor(Color.black);
      // Maior
      
      int RAIO_MAIOR = MARGEM + (MARGEM_CASA / 2); 
      int RAIO_MAIOR_B = SIZE - MARGEM_CASA;
      g.drawOval(RAIO_MAIOR, RAIO_MAIOR, RAIO_MAIOR_B, RAIO_MAIOR_B);
      System.out.println(String.format("RAIO_MAIOR = %s; RAIO_MAIOR_B = %s", RAIO_MAIOR, RAIO_MAIOR_B));


      // Média
      int RAIO_MEDIO = MARGEM + (MARGEM_INTERNA / 2);
      int RAIO_MEDIO_B = SIZE - MARGEM_INTERNA;
      g.drawOval(RAIO_MEDIO, RAIO_MEDIO, RAIO_MEDIO_B, RAIO_MEDIO_B);
      System.out.println(String.format("RAIO_MEDIO = %s; RAIO_MEDIO_B = %s", RAIO_MEDIO, RAIO_MEDIO_B));

      g.drawOval(MARGEM + (MARGEM_ASPECTOS / 2), MARGEM + (MARGEM_ASPECTOS / 2), SIZE - MARGEM_ASPECTOS, SIZE - MARGEM_ASPECTOS);

      // CASAS
      //g.setColor(Color.RED);
      Font font = new Font("TimesRoman", Font.BOLD, 14);
      g.setFont(font);
      for (int i = 0; i <= 11; i++) {

    	  Point ptAlfa = minToLocation(i*30, DISTANCE_ALFA);
          Point ptBeta = minToLocation(i*30, DISTANCE_BETA);
          
          Point ptLetra = minToLocation(i*30+15, DISTANCE_BETA+18);

          String xis = new Integer(i+1).toString();
          g.drawString(xis,
          		ptLetra.x - (BIG_DOT / 2) - MARGEM,
          		ptLetra.y - (BIG_DOT / 2));
          
          int xIni = ptAlfa.x - MARGEM;
          int yIni = ptAlfa.y - MARGEM;
          int xFim = ptBeta.x - MARGEM;
          int yFim = ptBeta.y - MARGEM;
          g.drawLine(xIni, yIni, xFim, yFim);
      }    
  }
  
  private void desenharPosicoesPlanetas(Graphics2D g) {
	  g.setColor(Color.red);
	  boolean alternador = false;
	  Integer acrescimo = 50;
	  Font font = this.getFontAstrologia();
	  // Planetas
      g.setFont(font.deriveFont(28f));
	  for (Obj o : lstPlanetas) {
		  Integer grau = o.getGrau()-grauDefasagemAscendente;
		  
		  alternador = !alternador;
		  acrescimo = alternador ? 50 : 90;
		  // ATENÇÃO! REDUZIR A DEFASAGEM DO SIGNO ASCENDENTE!!!
          Point ptLetra = minToLocation(grau, DISTANCE_BETA+acrescimo);
          g.drawString(o.getNome(),
          		ptLetra.x - (BIG_DOT / 2) - MARGEM,
          		ptLetra.y - (BIG_DOT / 2));

          Point ptBeta = minToLocation(grau, DISTANCE_BETA);
          g.fillOval(
            		ptBeta.x - (PQ_DOT / 2) - MARGEM,
            		ptBeta.y - (PQ_DOT / 2) - MARGEM,
            		PQ_DOT,
            		PQ_DOT);     
	  }
	  
	  // Cúspides
      g.setColor(Color.black);
      for (int i = 0; i <= 11; i++) {
    	  
    	  Obj o = lstCuspides.get(i);

    	  Point ptLetra = minToLocation(i*30, DISTANCE_ALFA+15);
          Point ptAntes = minToLocation(i*30-5, DISTANCE_ALFA+15);
          Point ptDepois = minToLocation(i*30+5, DISTANCE_ALFA+15);
          g.setFont(font.deriveFont(20f));
          g.drawString(o.getNome(),
          		ptLetra.x - (BIG_DOT / 2) - MARGEM,
          		ptLetra.y - (BIG_DOT / 2));
          
          g.setFont(new Font("TimesRoman", Font.PLAIN , 13));
          
          String grau = o.getGg().toString()+"°";
          String minuto = o.getMm().toString()+"'";
          
          String txtAntes = grau;
          String txtDepois = minuto;
          if (Arrays.asList(8,9,10,11,12).contains(i+1)) {
        	  txtAntes = minuto;
        	  txtDepois = grau;
          }
          
          g.drawString(	txtAntes,
            			ptAntes.x - (BIG_DOT / 2) - MARGEM,
            			ptAntes.y - (BIG_DOT / 2));

          g.drawString(	txtDepois,
      			ptDepois.x - (BIG_DOT / 2) - MARGEM,
      			ptDepois.y - (BIG_DOT / 2));
      }    
  }

  public static Font getFontAstrologia() {
	    Font font = null;
	    String fName = "/fonts/AstroDotBasic.ttf";
	    try {
	      InputStream is = ChartPainter.class.getResourceAsStream(fName);
	      font = Font.createFont(Font.TRUETYPE_FONT, is);
	    } catch (Exception ex) {
	      ex.printStackTrace();
	      System.err.println(fName + " not loaded.  Using serif font.");
	      font = new Font("TimesRoman", Font.PLAIN, 12);
	    }
	    return font;
  }  

  private Point minToLocation(int angulo, int radius) {
	double t = 2 * Math.PI * (angulo - 90) / 360;
    int x = (int)(HORIZONTAL_SIZE / 2 + radius * Math.sin(t));
    int y = (int)(VERTICAL_SIZE / 2 + radius * Math.cos(t));
    return new Point(x, y);
  }    
  
  
}