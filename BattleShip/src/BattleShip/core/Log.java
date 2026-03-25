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

public abstract class Log {
    protected final Calendar fecha;
    
    protected Log(Calendar fecha){
        this.fecha=fecha;
    }
    
    public Calendar getFecha(){
        return fecha;
    }
    
    public abstract String resumen();
}
