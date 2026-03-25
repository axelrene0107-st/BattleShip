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
        if (username == null || username.isBlank()) 
            return false;
        if (password == null || password.isBlank()) 
            return false;
        if (existeUsuario(username)) 
            return false;

        players.add(new Player(username.trim(), password, imagePath));
        return true;
    }
    
    //METODOS CON FOR EACH
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
    
    public Player conseguirPorNombre(String username) {
        if (username == null) return null;
        for (Player p : players) { 
            if (p.getUsername().equalsIgnoreCase(username.trim())) {
                return p;
            }
        }
        return null;
    }
    
    public void actualizarPlayer(Player updated) {
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);

            if (p.getUsername().equalsIgnoreCase(updated.getUsername())) {
                players.set(i, updated);
                return;
            }
        }

        throw new IllegalStateException("Player no encontrado para actualizar");
    }
    
    public boolean eliminarPlayer(String username) {
        if (username == null) 
            return false;

        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getUsername().equalsIgnoreCase(username.trim())) {
                players.remove(i);
                return true;
            }
        }
        return false;
    }
    
    public ArrayList<Player> getRanking() {
        ArrayList<Player> ranking = new ArrayList<>(players);
        ranking.sort((a, b) -> Integer.compare(b.getPuntos(), a.getPuntos()));
        return ranking;
    }

}
