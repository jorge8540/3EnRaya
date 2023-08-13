package modelo;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;
import vista.Form3EnRaya;
import vista.FormResultado;

public class Tablero extends JPanel {

    private int anchoCI;
    private int alturaCI;
    private int margen;
    private Color colorTablero;
    private Color colorCI;
    private TipoImagen jugadorActual;
    private TipoImagen turnoPartida;
    
    
    
    private Jugador jugador1;
    private Jugador jugador2;
    private ArrayList<Cuadro> cuadros;
    private Cuadro cuadroFrontal;
    public Tablero() {
        init();
    }

    private void init() {
        anchoCI = 80;
        anchoCI = 80;
        colorCI = Color.BLUE;
        colorTablero = Color.RED;
        margen = 6;
        jugador1 = new Jugador();
        jugador2 = new Jugador();
        cuadros = new ArrayList();
        jugadorActual = TipoImagen.EQUIS;
        turnoPartida = TipoImagen.EQUIS;
    }

    public void crearTablero() {
        setLayout(null);
        setSize(anchoCI * 3 + margen * 4, alturaCI * 3 + margen * 4);
        setBackground(colorTablero);
        cuadroFrontal= new Cuadro(this.getWidth(), this.getHeight(),Color.RED);
        cuadroFrontal.setLocation(0, 0);
        cuadroFrontal.setOpaque(false);
        cuadroFrontal.setEnabled(false);
        add(cuadroFrontal);
        crearCuadrosInternos();
        
    }

    private void crearCuadrosInternos() {
        int x = margen;
        int y = margen;

        for (int i = 0; i < 3; i++) {
            x = margen;
            for (int j = 0; j < 3; j++) {
                Cuadro cuadro = new Cuadro(anchoCI, alturaCI, colorCI);
                cuadro.setCursor(new Cursor(Cursor.HAND_CURSOR));
                cuadro.setLocation(x, y);
                cuadro.setI(i);
                cuadro.setJ(j);
                add(cuadro);
                crearEventosCuadro(cuadro);
                cuadros.add(cuadro);

                x += (anchoCI + margen);
            }
            y += (alturaCI + margen);
        }
    }

    public void crearEventosCuadro(Cuadro cuadro) {
        MouseListener evento = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (cuadro.isDibujado()) 
                    return;
                
                
                TipoImagen tipoImagenResultado = null;
                if (jugadorActual == TipoImagen.EQUIS) {

                    
                    cuadro.setTipoImagen(TipoImagen.EQUIS);
                    jugador1.getTablero()[cuadro.getI()][cuadro.getJ()] = 1;
                    tipoImagenResultado = jugador1.tictactoe(jugador2);
                    resultado(tipoImagenResultado, TipoImagen.EQUIS);
                    jugadorActual = TipoImagen.CIRCULO;

                    cambiarEstilos(TipoImagen.CIRCULO);
                } else if (jugadorActual == TipoImagen.CIRCULO) {
                    cuadro.setTipoImagen(TipoImagen.CIRCULO);
                    jugador2.getTablero()[cuadro.getI()][cuadro.getJ()] = 1;
                    tipoImagenResultado = jugador2.tictactoe(jugador1);
                    resultado(tipoImagenResultado, TipoImagen.CIRCULO);
                    jugadorActual = TipoImagen.EQUIS;
                    cambiarEstilos(TipoImagen.EQUIS);

                }
                cuadro.setDibujado(true);
                repaint();

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
        cuadro.addMouseListener(evento);
    }

    public void cambiarEstilos(TipoImagen jugadorAct) {
        if (jugadorAct == TipoImagen.CIRCULO) {
            Form3EnRaya.imgJugadorEquis.setRuta(Ruta.JUGADORAUXILIAR);
            Form3EnRaya.imgJugadorEquis.repaint();
            Form3EnRaya.nombreJugadorEquis.setForeground(new Color(240, 240, 240, 100));
            Form3EnRaya.imgJugadorCirculo.setRuta(Ruta.JUGADORCIRCULO);
            Form3EnRaya.imgJugadorCirculo.repaint();
            Form3EnRaya.nombreJugadorCirculo.setForeground(new Color(255, 200, 255));
        } else if (jugadorAct == TipoImagen.EQUIS) {
            Form3EnRaya.imgJugadorCirculo.setRuta(Ruta.JUGADORAUXILIAR);
            Form3EnRaya.imgJugadorCirculo.repaint();
            Form3EnRaya.nombreJugadorCirculo.setForeground(new Color(240, 240, 240, 100));
            Form3EnRaya.imgJugadorEquis.setRuta(Ruta.JUGADOREQUIS);
            Form3EnRaya.imgJugadorEquis.repaint();
            Form3EnRaya.nombreJugadorEquis.setForeground(new Color(180, 232, 255));
        }

    }

    public void resultado(TipoImagen tipoImagenResultado, TipoImagen jugadorGanador) {

        if (tipoImagenResultado == TipoImagen.EMPATE) {
            FormResultado formResultado = new FormResultado(TipoImagen.EMPATE, this);
            formResultado.setVisible(true);
            System.out.println("Empate");
        }
          else if (tipoImagenResultado != null) {
            System.out.println("Hay un ganador");
            Ruta.cambiarRutas(jugadorGanador);
            cuadroFrontal.setTipoImagen(tipoImagenResultado);
            desactivarCuadros(true);
            FormResultado formResultado = new FormResultado(jugadorGanador, this);
            formResultado.setVisible(true);
            
            Tablero tablero = this;
            Timer timer = new Timer();
            TimerTask tarea = new TimerTask() {
                @Override
                public void run() {
                    FormResultado formResultado = new FormResultado(jugadorGanador, tablero);
            formResultado.setVisible(true);
                }
                
            };
            
        }
    }
    public void reiniciarTablero(TipoImagen ganador){
        desactivarCuadros(false);
        borrarImagenes();
        cuadroFrontal.setTipoImagen(null);
        if(ganador==TipoImagen.EQUIS){
           int puntajeNuevo= Integer.parseInt(Form3EnRaya.puntajeEquis.getText())+1 ;
           Form3EnRaya.puntajeEquis.setText(String.valueOf(puntajeNuevo));
        }else if (ganador==TipoImagen.CIRCULO){
           int puntajeNuevo= Integer.parseInt(Form3EnRaya.puntajeCirculo.getText())+1 ;
           Form3EnRaya.puntajeCirculo.setText(String.valueOf(puntajeNuevo));
           
    }
        if(turnoPartida ==TipoImagen.EQUIS){
            jugadorActual=TipoImagen.CIRCULO;
            turnoPartida = TipoImagen.CIRCULO;
                    
        }else if (turnoPartida == TipoImagen.CIRCULO ){
            jugadorActual=TipoImagen.EQUIS;
        turnoPartida = TipoImagen.EQUIS;
                }
        cambiarEstilos(jugadorActual);
        jugador1.limpiar();
        jugador2.limpiar();
        
        repaint();
    }
    public void desactivarCuadros(boolean valor){
        for(Cuadro cuadro : cuadros){
            cuadro.setDibujado(valor);
            
        }
    }
    public void borrarImagenes(){
        for(Cuadro cuadro : cuadros)
            cuadro.setTipoImagen(null);
    }

    public TipoImagen getJugadorActual() {
        return jugadorActual;
    }

    public void setJugadorActual(TipoImagen jugadorActual) {
        this.jugadorActual = jugadorActual;
    }

    public ArrayList<Cuadro> getCuadros() {
        return cuadros;
    }

    public void setCuadros(ArrayList<Cuadro> cuadros) {
        this.cuadros = cuadros;
    }

    public int getAnchoCI() {
        return anchoCI;
    }

    public void setAnchoCI(int anchoCI) {
        this.anchoCI = anchoCI;
    }

    public int getAlturaCI() {
        return alturaCI;
    }

    public void setAlturaCI(int alturaCI) {
        this.alturaCI = alturaCI;
    }

    public int getMargen() {
        return margen;
    }

    public void setMargen(int margen) {
        this.margen = margen;
    }

    public Color getColorTablero() {
        return colorTablero;
    }

    public void setColorTablero(Color colorTablero) {
        this.colorTablero = colorTablero;
    }

    public Color getColorCI() {
        return colorCI;
    }

    public void setColorCI(Color colorCI) {
        this.colorCI = colorCI;
    }

    public Jugador getJugador1() {
        return jugador1;
    }

    public void setJugador1(Jugador jugador1) {
        this.jugador1 = jugador1;
    }

    public Jugador getJugador2() {
        return jugador2;
    }

    public void setJugador2(Jugador jugador2) {
        this.jugador2 = jugador2;
    }
}
