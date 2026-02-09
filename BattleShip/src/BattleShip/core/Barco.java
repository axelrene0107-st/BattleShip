/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package BattleShip.core;

/**
 *
 * @author axelr
 */
public enum Barco {
    DESTRUCTOR("DT", 2), SUBMARINO("SM", 3), ACORAZADO("AZ", 4), PORTAVIONES("PA", 5);
    
    private final String codigo;
    private final int tamanio;
    
    Barco(String codigo, int tamanio){
        this.codigo= codigo;
        this.tamanio= tamanio;
    }
    
    
    public String getCodigo(){
        return codigo;
    }
    
    public int getTamanio(){
        return tamanio;
    }
}
