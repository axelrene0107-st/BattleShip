/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BattleShip;

/**
 *
 * @author axelr
 */
import BattleShip.UI.MenuWindow;
import BattleShip.core.PlayerManager;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) { 
        //Accion principal utilizando swingutilities para llamar ventanas
        SwingUtilities.invokeLater(() -> {
            PlayerManager manager = new PlayerManager();
            MenuWindow mw = new MenuWindow(manager);
            mw.setVisible(true);
        });
    }
}
