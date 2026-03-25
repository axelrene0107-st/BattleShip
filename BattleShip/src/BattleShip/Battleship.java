/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BattleShip;

import BattleShip.core.*;
import java.util.Calendar;

/**
 *
 * @author axelr
 */

public class Battleship {
    public enum Estado{
        SETUP, EN_JUEGO, TERMINADO;
    }
    
    public enum Turno{
        JUGADOR1, JUGADOR2;
    }
    
    public static class Disparo{
        public final Turno turnoDisparo;
        public final ResultadoDisparo resultado;
        public final boolean tableroRegenerado;
        public final boolean findelJuego;
        public final Player ganador;

        public Disparo(Turno turnoDisparo, ResultadoDisparo resultado, boolean tableroRegenerado, boolean findelJuego, Player ganador) {
            this.turnoDisparo = turnoDisparo;
            this.resultado = resultado;
            this.tableroRegenerado = tableroRegenerado;
            this.findelJuego = findelJuego;
            this.ganador = ganador;
        }
    }
    
    private final Player player1;
    private final Player player2;
    private final Calendar fechaPartida;
    private boolean disparoHecho;
    
    private final Tablero tableroJugador1;
    private final Tablero tableroJugador2;

    private Estado estado = Estado.SETUP;
    private Turno turnoActual = Turno.JUGADOR1;

    public Battleship(Player player1, Player player2, Tablero tableroJugador1, Tablero tableroJugador2) {
        if(player1 == null || player2 == null) 
            throw new IllegalArgumentException("Players no pueden ser null.");
        this.player1 = player1;
        this.player2 = player2;
        this.tableroJugador1 = tableroJugador1;
        this.tableroJugador2 = tableroJugador2;
        fechaPartida = Calendar.getInstance();
    }
    
    public void iniciarPartida(){
        estado = Estado.EN_JUEGO;
        turnoActual = Turno.JUGADOR1;      
        disparoHecho = false;
    }

    public Estado getEstado(){
        return estado;
    }
    
    public Turno getTurnoActual(){
        return turnoActual;
    }
    
    public Player getJugadorActual(){
        return (turnoActual == Turno.JUGADOR1) ? player1 : player2;//ternario para conseguir player dependiendo del turno
    }
    
    public Player getOponenteActual(){
        return (turnoActual == Turno.JUGADOR1) ? player2 : player1;//ternario para conseguir el oponente
    }

    public Tablero getTableroJugador1() {
        return tableroJugador1;
    }

    public Tablero getTableroJugador2() {
        return tableroJugador2;
    }

    
    public Disparo disparar(int fila, int columna){ 
        if(estado != Estado.EN_JUEGO){
            throw new IllegalStateException("La partida no esta en juego");//Excepcion por si el estado de la partida no se guarda, lanza un mensaje.
        }
        
        if (disparoHecho) {
            return new Disparo(turnoActual, ResultadoDisparo.REPETIDO, false, false, null);
        }
        
        Turno turnoDisparo = turnoActual;
        Tablero tableroDefensor = (turnoActual == Turno.JUGADOR1) ? tableroJugador2 : tableroJugador1;//ternario para conseguir el tablero que se muestra
        
        ResultadoDisparo resultado = tableroDefensor.disparar(fila, columna);
        disparoHecho = true;
            
        boolean regenerado = false;
        boolean fin = false;
        Player ganador = null;
        
        if(resultado == ResultadoDisparo.HUNDIDO){
            if(tableroDefensor.todosHundidos()){
                estado = Estado.TERMINADO;
                fin = true;
                ganador = getJugadorActual();
                
                Partida log = new Partida(fechaPartida, player1.getUsername(), player2.getUsername(), ganador.getUsername(), ganador.getDifficulty(), ganador.getMode(), false);
                
                player1.addLog(log);
                player2.addLog(log);

                ganador.sumarVictoria();
                
                player1.getTablero().reset();
                player2.getTablero().reset();
            }else{
                tableroDefensor.reubicarBarcos();
                regenerado = true;
            }
        }       
        return new Disparo(turnoDisparo, resultado, regenerado, fin, ganador);
    }
    
    
    private void cambiarTurno(){
        turnoActual = (turnoActual == Turno.JUGADOR1) ? Turno.JUGADOR2 : Turno.JUGADOR1;//Ternario para cambiar turno
    }
    
    public boolean pasarTurno(){
        if (estado != Estado.EN_JUEGO) 
            return false;

        if (!disparoHecho) {
            return false;
        }

        cambiarTurno();
        disparoHecho = false;
        return true;
    }
    
    
    public void retirarJugadorActual(){
        if(estado != Estado.EN_JUEGO)
            return;
        
        Player ganador = getOponenteActual();       
        estado= Estado.TERMINADO;
        disparoHecho = false;
        
        Partida log = new Partida(fechaPartida, player1.getUsername(), player2.getUsername(), ganador.getUsername(), ganador.getDifficulty(), ganador.getMode(), true);

        player1.addLog(log);
        player2.addLog(log);

        ganador.sumarVictoria();//Metodo de player para sumar los tres puntos 
        
        player1.getTablero().reset();//Reseteo de los tablero para que esten limpios para la siguiente partida  
        player2.getTablero().reset();
    }    
}
