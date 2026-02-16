/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BattleShip.core;

/**
 *
 * @author axelr
 */
public class Instance {
    private final Barco tipo;
    private int vidasActuales;
    
    public Instance(Barco tipo){
        this.tipo= tipo;
        this.vidasActuales= tipo.getTamanio();
    }
    
    public Barco getTipo(){
        return tipo;
    }
    
    public int getVidasMax(){
        return tipo.getTamanio();
    }
    
    public int getVidasActuales(){
        return vidasActuales;
    }
        
    public void recibirImpacto(){
        if(vidasActuales > 0){
            vidasActuales--;
        }
    }
        
    public boolean estaHundido(){
        return vidasActuales<= 0;
    }
    
    @Override
    public String toString() {
        return tipo.getCodigo() + " (" + vidasActuales + "/" + tipo.getTamanio() + ")";
    }
    

}
