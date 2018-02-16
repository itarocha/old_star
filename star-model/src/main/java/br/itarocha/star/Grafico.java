package br.itarocha.star;

import java.awt.*;
import java.awt.event.*;
import static java.lang.Math.*;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import javax.swing.*;

import br.itarocha.star.model.Cuspide;
import br.itarocha.star.model.PlanetaPosicao;

//http://freefrontend.com/css-clocks/
class Grafico extends JPanel {

	private Mapa mapa; 
	
    final float degrees06 = (float) (PI / 30);
    final float degrees30 = degrees06 * 5;
    final float degrees90 = degrees30 * 3;
    
    
    float GRAU_DEFASAGEM = (float) Math.PI / 2; 
    
    
 
    final int sizeTotal = 640;
    final int margem = 20;
    final int diametro = sizeTotal - (2 * margem);
    final int d2 = (int) (diametro - (diametro * 0.1));
    
    final int dmenor = (int)(diametro / 2.1); 
    final int dBemmenor = (int)(diametro / 5); 
    final int dMin = 50; 
    final int raioMaior = diametro / 2;
    
    final int cx = diametro / 2 + margem;
    final int cy = diametro / 2 + margem;
 

    public Grafico(Mapa mapa) {
    	this.mapa = mapa;
    	
        setPreferredSize(new Dimension(sizeTotal, sizeTotal));
        setBackground(Color.white);
        System.out.println("diametro = "+diametro);
        
        
        repaint();
        /*
        new Timer(500, (ActionEvent e) -> {
            repaint();
        }).start();
        */
    }
    
    private void atualizarGrauDefasagem(int angulo) {
    	GRAU_DEFASAGEM = anguloToRad(angulo);
    }

    @Override
    public void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        Graphics2D g = (Graphics2D) gg;
        g.setRenderingHint(	RenderingHints.KEY_ANTIALIASING,
                			RenderingHints.VALUE_ANTIALIAS_ON);
 
        drawFace(g);
        
        drawTicks(g);
 
        /*
        final LocalTime time  = LocalTime.now();
        int hour = time.getHour();
        int minute = time.getMinute();
        int second = time.getSecond();
 
        
        // Segundos
        float angle = degrees90 - (degrees06 * second);
        desenhaLinha(g, angle, diameter / 2 - 30, Color.red);
 
        // Minutos
        float minsecs = (minute + second / 60.0F);
        angle = degrees90 - (degrees06 * minsecs);
        desenhaLinha(g, angle, diameter / 3 + 10, Color.blue);
 
        // Horas
        float hourmins = (hour + minsecs / 60.0F);
        angle = degrees90 - (degrees30 * hourmins);
        desenhaLinha(g, angle, diameter / 4 + 10, Color.black);
        */
    }
 
    private void drawFace(Graphics2D g) {
        g.setStroke(new BasicStroke(1));
        g.setColor(Color.white );
        g.fillOval(margem, margem, diametro, diametro);
        g.setColor(Color.blue);
        g.drawOval(margem, margem, diametro, diametro);
        //g.setColor(Color.black);
        g.drawOval(margem * 3, margem * 3, diametro - (margem * 4), diametro - (margem * 4));
        //g.setColor(Color.black);
        g.drawOval(margem * 8, margem * 8, margem * 16, margem * 16);
        g.drawOval(margem * 10, margem * 10, diametro - (margem * 18), diametro - (margem * 18));
    }
 
    private float anguloToRad(float angulo) {
    	return ((float)(Math.PI / 180) * angulo);
    }
    
    
    private void drawTicks(Graphics2D g) {

    	//int xcenter = 175, ycenter = 175, lastxs = 0, lastys = 0, lastxm = 0, lastym = 0;
    	//Read more: http://mrbool.com/how-to-construct-an-analog-clock-with-java/26863#ixzz53daGMYMK    	

    	/*
    	int grau = 0;
    	int second = 45;
    	int	minute = 10; 
    	int hour = 6;
    	int xminute = (int) (Math.cos(minute * Math.PI / 30 - Math.PI / 2) * 100 + cx); 
    	int yminute = (int) (Math.sin(minute * Math.PI / 30 - Math.PI / 2) * 100 + cy); 
    	int xhour = (int) (Math.cos((hour * 30 + minute / 2) * Math.PI / 180 - Math.PI / 2) * 80 + cx); 
    	int yhour = (int) (Math.sin((hour * 30 + minute / 2) * Math.PI / 180 - Math.PI / 2) * 80 + cy);
		*/
    	int intGrauDef = 0;
    	if (!mapa.getListaCuspides().isEmpty()) {
    		String grauDef = mapa.getListaCuspides().get(0).getGrau();
    		grauDef = grauDef.replace('.', '-');
    		String[] gms = grauDef.split("-");
    		intGrauDef = Integer.parseInt(gms[0]);
    		
    		//System.out.println("A DEFASAGEM É DE "+gms[0]);
    		//System.out.println("A DEFASAGEM É DE "+intGrauDef);
    	}
    	
    	atualizarGrauDefasagem(90+intGrauDef);
    	
    	/*
    	g.setColor(Color.blue);
    	for (int i = 0; i < 360; i++) {
    		Ponto p = calcularAngulo(i);

    		int xIni = (int) (p.x * raioMaior) + cx; 
    		int yIni = (int) (p.y * raioMaior) + cy;
    		int xFim = (int) (p.x * dmenor) + cx; 
	    	int yFim = (int) (p.y * dmenor) + cy;

	    	g.drawLine(xIni, yIni, xFim, yFim);
    	}
    	*/
    	g.setColor(Color.blue);
    	List<Ponto> ptCasas = new ArrayList<Ponto>();
    	for (Cuspide c : mapa.getListaCuspides()) {
    		if (c.getNumero() > 12) { break; }
    		String gnc = c.getGrau();
    		gnc = gnc.replace('.', '-');
    		String[] gms = gnc.split("-");
        	ptCasas.add(calcularAngulo(Integer.parseInt(gms[0]) , Integer.toString(c.getNumero()) ));
    	}
    	
    	//g.setColor(Color.red);

    	for (Ponto p : ptCasas) {
    		int xIni = (int) (p.getX() * 260) + cx; 
    		int yIni = (int) (p.getY() * 260) + cy;
    		int xFim = (int) (p.getX() * 120) + cx; 
	    	int yFim = (int) (p.getY() * 120) + cy;
	    	g.drawLine(xIni, yIni, xFim, yFim);
	    	//g.drawString(p.nome, xFim, yFim);
    	}
    	
    	for (Ponto p : ptCasas) {
    		p.setAngulo(p.angulo + 15);
    		//int xIni = (int) (p.getX() * raioMaior) + cx; 
    		//int yIni = (int) (p.getY() * raioMaior) + cy;
    		int xFim = (int) (p.getX() * 150) + cx; 
	    	int yFim = (int) (p.getY() * 150) + cy;
	    	//g.drawLine(xIni, yIni, xFim, yFim);
	    	g.drawString(p.nome, xFim, yFim);
    	}

    	List<Ponto> ptPlanetas = new ArrayList<Ponto>();
    	for (PlanetaPosicao pp : mapa.getPosicoesPlanetas()) {
    		String _g = pp.getGrau();
    		//System.out.println(_g);
    		_g = _g.replace('.', '-');
    		String[] gms = _g.split("-");
    		//System.out.println("--------------"+gms[0]);
        	ptPlanetas.add(calcularAngulo(Integer.parseInt(gms[0]) ,pp.getEnumPlaneta().getSigla() ));
    	}
    	
    	g.setColor(Color.black);

    	int x = 0;
    	for (Ponto p : ptPlanetas) {
    		x++;
    		
    		x = 200 + new Random().nextInt(50);
    				
    		int xIni = (int) (p.getX() * raioMaior) + cx; 
    		int yIni = (int) (p.getY() * raioMaior) + cy;
    		int xFim = (int) (p.getX() * x ) + cx; 
	    	int yFim = (int) (p.getY() * x ) + cy;
	    	//g.drawLine(xIni, yIni, xFim, yFim);
	    	g.drawString(p.nome, xFim, yFim);
    	}
    	
    	/*
    	startX = x;
    	startY = y;
    	endX   = x + 40 * Math.sin(angle);
    	endY   = y + 40 * Math.cos(angle);
    	
    	angle = angle * Math.PI / 180;
    	*/
    	
    	// Segundos
    	/*
    	for (int second = 0; second < 360; second++) {
    		//float angle = degrees90 - (degrees06 * second);
    		//float angle = ((float)(Math.PI / 180) * second);
    		
    		float angle = anguloToRad(second);
    		
    		System.out.println(angle);
    		desenhaLinha(g, angle, diameter / 2 - 30, Color.red);
    		//System.out.println(m.calcular("Segundo: "+second));
    	}
    	*/
    	
    	//desenhaLinha(g, 30, diameter / 2 - 30, Color.black);
    	//desenhaLinha(g, 60, diameter / 2 - 30, Color.blue);
    	//desenhaLinha(g, 90, diameter / 2 - 30, Color.green);
    }
 
    private Ponto calcularAngulo(int angulo, String nome) {
    	//double x = Math.cos(angulo * Math.PI / 180 - GRAU_DEFASAGEM);
    	//double y = Math.sin(angulo * Math.PI / 180 - GRAU_DEFASAGEM);
    	//double x = Math.sin(angulo * Math.PI / 180 - GRAU_DEFASAGEM);
    	//double y = Math.cos(angulo * Math.PI / 180 - GRAU_DEFASAGEM);
    	return new Ponto(angulo, nome, GRAU_DEFASAGEM);
	}

    private Ponto calcularAngulo(int angulo) {
    	return calcularAngulo(angulo,"");
	}

    private void desenhaLinha(Graphics2D g, float angulo, int raio, Color cor) {
        int x = cx + (int) (raio * cos(angulo));
        int y = cy - (int) (raio * sin(angulo));
        g.setColor(cor);
        g.drawLine(cx, cy, x, y);
    }
 
    public static void build(Mapa mapa) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setTitle("Mapa");
            f.setResizable(false);
            f.add(new Grafico(mapa), BorderLayout.CENTER);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}