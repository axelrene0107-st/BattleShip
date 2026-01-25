/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BattleShip;

/**
 *
 * @author axelr
 */
import BattleShip.core.Player;
import BattleShip.core.PlayerManager;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MenuPrincipalWindow extends JFrame{
    private static final String ASSETS = "/BattleShip/assets/";

    // Fondo animado
    private static final String BG_GIF = "fondoMenuPrincipal.gif";

    // Labels laterales
    private static final String LB_PROFILE = "profileLB.png";
    private static final String LB_REPORTS = "reportsLB.png";

    // Botones (ajusta nombres si aplica)
    private static final String BTN_PLAY   = "playBTN.png";
    private static final String BTN_CONFIG = "configBTN.png";  
    private static final String BTN_EXIT   = "exitBTN.png";
    private static final String BTN_EDIT   = "editBTN.png";
    private static final String BTN_CHECK  = "checkBTN.png";

    private final PlayerManager manager;
    private final Player current; // opcional, para mostrar perfil / sesión

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
        // Usamos JLayeredPane para poner el GIF detrás y los botones encima
        JLayeredPane layers = new JLayeredPane();
        layers.setLayout(null);
        setContentPane(layers);

        // =========================
        // FONDO GIF (animado)
        // =========================
        JLabel bg = new JLabel(loadGifIcon(BG_GIF));
        bg.setBounds(0, 0, getWidth(), getHeight());
        layers.add(bg, Integer.valueOf(0));

        // Panel "overlay" encima del fondo
        JPanel ui = new JPanel(null);
        ui.setOpaque(false);
        ui.setBounds(0, 0, getWidth(), getHeight());
        layers.add(ui, Integer.valueOf(1));

        // =========================
        // LATERAL IZQUIERDO: REPORTS + CHECK
        // =========================
        JLabel reportsLB = new JLabel(loadScaledIcon(LB_REPORTS, 170, 170));
        reportsLB.setBounds(40, 365, 170, 170);
        ui.add(reportsLB);

        JLabel btnCheck = createImageButton(BTN_CHECK, 120, 55, this::onReports);
        btnCheck.setBounds(65, 455, 120, 55);
        ui.add(btnCheck);

        // =========================
        // LATERAL DERECHO: PROFILE + EDIT
        // =========================
        JLabel profileLB = new JLabel(loadScaledIcon(LB_PROFILE, 170, 170));
        profileLB.setBounds(890, 365, 170, 170);
        ui.add(profileLB);

        JLabel btnEdit = createImageButton(BTN_EDIT, 120, 55, this::onProfile);
        btnEdit.setBounds(915, 455, 120, 55);
        ui.add(btnEdit);

        // =========================
        // BOTONES CENTRALES: PLAY / CONFIG / EXIT
        // =========================
        // Tamaños aproximados al mockup
        int bigW = 210;
        int bigH = 85;

        int y = 410; // franja inferior
        int xPlay = 270;
        int xConfig = 455;
        int xExit = 665;

        JLabel btnPlay = createImageButton(BTN_PLAY, bigW, bigH, this::onPlay);
        btnPlay.setBounds(xPlay, y, bigW, bigH);
        ui.add(btnPlay);

        JLabel btnConfig = createImageButton(BTN_CONFIG, bigW, bigH, this::onConfig);
        btnConfig.setBounds(xConfig, y, bigW, bigH);
        ui.add(btnConfig);

        JLabel btnExit = createImageButton(BTN_EXIT, bigW, bigH, this::onExit);
        btnExit.setBounds(xExit, y, bigW, bigH);
        ui.add(btnExit);
    }
    
    private void onPlay() {
        System.out.println("PLAY");
    }
    
    private void onConfig() {
        System.out.println("CONFIG");
    }
    
    private void onReports() {
        System.out.println("REPORTS");
    }
    
    private void onProfile() {
        System.out.println("PROFILE");
    }
    
    private void onExit() {
        dispose();
        new MenuWindow(manager).setVisible(true);
    }
    
    private JLabel createImageButton(String img, int w, int h, Runnable action) {
        JLabel btn = new JLabel(loadScaledIcon(img, w, h));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                action.run();
            }
        });
        return btn;
    }
    
    private ImageIcon loadScaledIcon(String file, int w, int h) {
        URL url = getClass().getResource(ASSETS + file);
        if (url == null) throw new RuntimeException("No se encontró: " + ASSETS + file);
        Image img = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
    
     private ImageIcon loadGifIcon(String file) {
        URL url = getClass().getResource(ASSETS + file);
        if (url == null) throw new RuntimeException("No se encontró: " + ASSETS + file);
        return new ImageIcon(url); // mantiene animación
    }
}
