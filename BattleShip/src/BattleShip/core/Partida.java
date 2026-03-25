/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BattleShip.core;

/**
 *
 * @author axelr
 */
import java.util.Calendar;
public class Partida extends Log{
    private final String jugador1;
    private final String jugador2;
    private final String ganador;
    private final Player.Difficulty difficulty;
    private final Player.Mode mode;
    private final boolean retiro;

    public Partida(Calendar fechaPartida, String jugador1, String jugador2, String ganador, Player.Difficulty difficulty, Player.Mode mode, boolean retiro) {
        super(fechaPartida);
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.ganador = ganador;
        this.difficulty = difficulty;
        this.mode = mode;
        this.retiro = retiro;
    }
    
    public String getJugador1() { 
        return jugador1; 
    }
    
    public String getJugador2() { 
        return jugador2; 
    }
    
    public String getGanador() { 
        return ganador; 
    }
    
    public Player.Difficulty getDifficulty() { 
        return difficulty; 
    }
    
    public Player.Mode getMode() { 
        return mode;
    }
    
    public boolean isRetiro() { 
        return retiro; 
    }

    @Override//Sobreesritura del metodo abstracto de Log
    public String resumen() {//ternario
        if (retiro) {
            return ganador + " gano porque " + (ganador.equals(jugador1) ? jugador2 : jugador1) + " se retiro (" + difficulty + ") Fecha: ";
        }
        return ganador + " hundio todos los barcos de "
                + (ganador.equals(jugador1) ? jugador2 : jugador1)
                + " en modo " + difficulty + "Fecha: "+ getFecha();
    }
    
    @Override
    public String toString() {
        return resumen();
    }
}
