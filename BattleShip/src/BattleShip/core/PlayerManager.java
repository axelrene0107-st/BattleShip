/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BattleShip.core;
import java.util.ArrayList;
/**
 *
 * @author axelr
 */
public class PlayerManager {
    
    private ArrayList<Player> players;
    
    public PlayerManager(){
        players = new ArrayList<>();
    }
    
    public boolean crearPlayer(String username, String password, String imagePath){
        if (username == null || username.isBlank()) return false;
        if (password == null || password.isBlank()) return false;
        if (existeUsuario(username)) return false;

        players.add(new Player(username.trim(), password, imagePath));
        return true;
    }
    
    public Player login(String username, String password){
        for (Player p : players){
            if (p.getUsername().equals(username) && p.getPassword().equals(password)){
                return p;
            }
        }
        return null;
    }
    
    public boolean existeUsuario(String username){
        for (Player p : players){
            if(p.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }
}
