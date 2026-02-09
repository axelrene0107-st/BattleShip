/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BattleShip.core;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author axelr
 */
public class Tablero {
    public static final int SIZE= 8;
    
    private final Instance[][] ocupacion;
    private final boolean[][] disparado;
    private List<Instance> barcos;
    
    public Tablero(){
        ocupacion = new Instance[SIZE][SIZE];
        disparado = new boolean[SIZE][SIZE];
        barcos = new ArrayList<>();
    }
    
    public static boolean dentro(int fila, int columna){
        return fila>=0 && fila<SIZE && columna>=0 && columna<SIZE;
    }
    
    public boolean puedeColocar(int fila, int columna, Direccion dir, Barco tipo){
        int longitud = tipo.getTamanio();
        
        for(int i=0; i<longitud; i++){
            int row =(dir == Direccion.HORIZONTAL)? fila : fila+i;
            int column =(dir == Direccion.HORIZONTAL)? columna+i : columna;
            
            if(!dentro(row, column)) return false;
            if(ocupacion[row][column] != null) return false;
        }
        
        return true;
    }
    
    public boolean colocarBarco(int fila, int columna, Direccion dir, Barco tipo){
        if(!puedeColocar(fila, columna, dir, tipo)) return false;
        
        Instance barco = new Instance(tipo);
        int longitud = tipo.getTamanio();
        
        for(int i=0; i<longitud; i++){
            int row =(dir == Direccion.HORIZONTAL)? fila : fila+i;
            int column =(dir == Direccion.HORIZONTAL)? columna+i : columna;
            ocupacion[row][column] = barco;
        }
        
        barcos.add(barco);
        return true;
    }
    
    public boolean hayBarcoEn(int fila, int columna){
        return dentro(fila, columna) && ocupacion[fila][columna] != null;
    }
    
    public void reset(){
        for (int r=0; r< SIZE; r++){
            for(int c=0; c< SIZE; c++){
                ocupacion[r][c]=null;
                disparado[r][c]=false;
            }
        }
        barcos.clear();
    }
    
    
    public ResultadoDisparo disparar(int fila, int columna){
        if(!dentro(fila, columna))throw new IllegalArgumentException("Cordenadas fuera de alcance!");
        if(disparado[fila][columna])return ResultadoDisparo.REPETIDO;
        
        disparado[fila][columna] = true;
        
        Instance b= ocupacion[fila][columna];
        if(b==null){
            return ResultadoDisparo.AGUA;
        }
        
        b.recibirImpacto();
        return b.estaHundido() ? ResultadoDisparo.HUNDIDO : ResultadoDisparo.IMPACTO;
    }
    
    public boolean fueDisparado(int fila, int columna){
        return dentro(fila, columna) && disparado[fila][columna];
    }
    
    public boolean fueImpacto(int fila, int columna){
        return dentro(fila, columna) && disparado[fila][columna] && ocupacion[fila][columna] != null;
    }
    
    public int barcosRestantes(){
        int cantidad=0;
        for (Instance b : barcos){
            if(!b.estaHundido())
                cantidad++;
        }
        
        return cantidad;
    }
    
    public boolean todosHundidos(){
        return barcosRestantes() == 0;
    }
}
