/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BattleShip.core;

/**
 *
 * @author axelr
 */
public class Player {
    
    private String username;
    private String password;
    private int puntos;
    private String imagePath;
    
    public Player(String username, String password, String imagePath){
        this.username= username;
        this.password= password;
        this.imagePath= imagePath;
        this.puntos=0;
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
    
    public void setImagePath(String imagePath){
        this.imagePath = imagePath;
    }
    
}
