/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BattleShip.core;

/**
 *
 * @author axelr
 */

import java.util.ArrayList;
public class Player {
    public enum Difficulty{EASY, NORMAL, EXPERT, GENIUS}
    public enum Mode{ARCADE, TUTORIAL}
    
    private String username;
    private String password;
    private int puntos;
    private String imagePath;
    private Difficulty difficulty;
    private Mode mode;   
    private final Tablero tablero;
    private final ArrayList<Partida> historial;
      
    public Player(String username, String password, String imagePath){
        this.username= username;
        this.password= password;
        this.imagePath= imagePath;
        
        this.puntos=0;       
        difficulty= Difficulty.NORMAL;
        mode= Mode.ARCADE;            
        this.tablero = new Tablero();
        historial = new ArrayList<>();
    }
    
    public void addLog(Partida log) {
        historial.add(0, log);
        if (historial.size() > 10) {
            historial.remove(10); 
        }
    }

    public ArrayList<Partida> getUltimos10Juegos() {
        return new ArrayList<>(historial);
    }

    public void sumarVictoria() {
        puntos += 3;
    }
    
    public Tablero getTablero(){
        return tablero;
    }
    
    public String getUsername(){
        return username; 
    }
    
    public String getPassword(){
        return password;
    }
    
    public int getPuntos(){
        return puntos;
    }
    
    public String getImagePath(){
        return imagePath;
    }
    
    public Difficulty getDifficulty(){
        return difficulty;
    }
    
    public Mode getMode(){
        return mode;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }
    
    public void setImagePath(String imagePath){
        this.imagePath = imagePath;
    }
    
    public void setDifficulty(Difficulty difficulty){
        this.difficulty = difficulty;
    }
    
    public void setMode(Mode mode){
        this.mode= mode;
    }
    
}
