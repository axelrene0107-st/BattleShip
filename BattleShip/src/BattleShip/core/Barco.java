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
    DESTRUCTOR("DT", "DESTRUCTOR", 2), SUBMARINO("SM", "SUBMARINO", 3), ACORAZADO("AZ", "ACORAZADO", 4), PORTAVIONES("PA", "PORTAVIONES", 5);
    
    private final String codigo;
    private final String nombre;
    private final int tamanio;
    
    Barco(String codigo, String nombre, int tamanio){
        this.codigo= codigo;
        this.nombre= nombre;
        this.tamanio= tamanio;
    }
    
    public String getCodigo(){
        return codigo;
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public int getTamanio(){
        return tamanio;
    }

    public static Barco fromCodigo(String codigo) {
        if (codigo == null) return null;

        for (Barco b : Barco.values()) { 
            if (b.codigo.equalsIgnoreCase(codigo)) {
                return b;
            }
        }
        return null;
    }
}
