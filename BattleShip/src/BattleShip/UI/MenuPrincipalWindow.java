/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BattleShip.UI;

/**
 *
 * @author axelr
 */
import BattleShip.UI.MenuWindow;
import BattleShip.UI.SelectPlayerWindow;
import BattleShip.core.Player;
import BattleShip.core.PlayerManager;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MenuPrincipalWindow extends JFrame {
    //Path
    private static final String ASSETS = "/BattleShip/assets/";

    //Fondos
    private static final String BG_GF = "fondoMenuPrincipal.gif";
    private static final String BG_PNG = "fondo7.png";

    //Botones
    private static final String BTN_PLAY   = "playBTN.png";
    private static final String BTN_CONFIG = "configBTN.png";
    private static final String BTN_EXIT   = "exitBTN.png";
    private static final String BTN_EDIT   = "editBTN.png";
    private static final String BTN_CHECK  = "checkBTN.png";

    //Variables de logica de jugadores
    private final PlayerManager manager;
    private final Player current;

    public MenuPrincipalWindow(PlayerManager manager, Player current) {
        this.manager = manager;
        this.current = current;
        configureWindow();
        buildUI();
    }

    private void configureWindow() {
        setTitle("Battleship - Menu Principal");
        setSize(1100, 620);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void buildUI() {
        //Fondo Gif con contentPane para sobrescribir sobre el
        JLabel bg = new JLabel(cargarIconoEscalado(BG_GF, 1100, 620));
        bg.setBounds(0, 0, 1100, 620);
        bg.setLayout(null);
        setContentPane(bg);
        
        //Botones
        JLabel btnCheck = createImageButton(BTN_CHECK, 130, 70, this::onReports);
        btnCheck.setBounds(61, 480, 120, 55);
        bg.add(btnCheck);

        JLabel btnEdit = createImageButton(BTN_EDIT, 130, 70, this::onProfile);
        btnEdit.setBounds(922, 480, 120, 55);
        bg.add(btnEdit);
        
        //Tamaños de botones de enmedio
        int bigW = 250, bigH = 145;
        int y = 420, xPlay = 200, xConfig = 425, xExit = 650;

        JLabel btnPlay = createImageButton(BTN_PLAY, bigW, bigH, this::onPlay);
        btnPlay.setBounds(xPlay, y, bigW, bigH);
        bg.add(btnPlay);

        JLabel btnConfig = createImageButton(BTN_CONFIG, bigW-20, bigH-15, this::onConfig);
        btnConfig.setBounds(xConfig, y, bigW, bigH);
        bg.add(btnConfig);

        JLabel btnExit = createImageButton(BTN_EXIT, bigW, bigH, this::onExit);
        btnExit.setBounds(xExit, y, bigW, bigH);
        bg.add(btnExit);
        
        //Fondo con logo y panaeles grises
        JLabel bgPNG = new JLabel(cargarIconoEscalado(BG_PNG, 1100, 590));
        bgPNG.setBounds(0, 0, 1100, 590);
        bgPNG.setLayout(null);
        bg.add(bgPNG);
    }

    
    private void onPlay() {
        dispose();
        new SelectPlayerWindow(manager, current).setVisible(true);
    }
    
    
    private void onConfig() { System.out.println("CONFIG"); }
    private void onReports() { System.out.println("REPORTS"); }
    
    
    private void onProfile() { 
        dispose();
        new EditProfileWindow(manager, current).setVisible(true);
    }

    private void onExit() {
       MenuWindow mw= new MenuWindow(manager);
       mw.setVisible(true);
       dispose();
    }

    //Formato de creacion de botones
    private JLabel createImageButton(String img, int w, int h, Runnable action) {
        JLabel btn = new JLabel(cargarIconoEscalado(img, w, h));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) { 
                action.run(); 
            }
        });
        return btn;
    }

    //Formato de escalado de imagenes(iconos)
    private ImageIcon cargarIconoEscalado(String file, int w, int h) {
        URL url = getClass().getResource(ASSETS + file);
        if (url == null) throw new RuntimeException("No se encontró: " + ASSETS + file);
        Image img = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_FAST);
        return new ImageIcon(img);
    }
}

