/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BattleShip.UI;

/**
 *
 * @author axelr
 */
import BattleShip.core.Player;
import BattleShip.core.PlayerManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;

public class MenuPrincipalWindow extends JFrame {
    private static final String ASSETS = "/BattleShip/assets/";
    private static final String BG_GF = "fondoMenuPrincipal.gif";
    private static final String BG_PNG = "fondo7.png";
    private static final String BTN_PLAY   = "playBTN.png";
    private static final String BTN_PLAY_PRESS   = "playPRESS_BTN.png";
    private static final String BTN_CONFIG = "configBTN.png";
    private static final String BTN_CONFIG_PRESS = "configPRESS_BTN.png";
    private static final String BTN_EXIT   = "exitBTN.png";
    private static final String BTN_EXIT_PRESS   = "exitPRESS_BTN.png";
    private static final String BTN_EDIT   = "editBTN.png";
    private static final String BTN_EDIT_PRESS   = "editPRESS_BTN.png";
    private static final String BTN_CHECK  = "checkBTN.png";
    private static final String BTN_CHECK_PRESS  = "checkPRESS_BTN.png";
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
        JLabel bg = new JLabel(cargarIconoEscalado(BG_GF, 1100, 620));
        bg.setBounds(0, 0, 1100, 620);
        bg.setLayout(null);
        setContentPane(bg);
        
        JLabel imagenPerfil= new JLabel();
        imagenPerfil.setIcon(cargarImagenPlayer(current, 50, 35));
        imagenPerfil.setBounds(70, 34, 50, 50);
        imagenPerfil.setHorizontalAlignment(JLabel.CENTER);
        imagenPerfil.setOpaque(false);
        bg.add(imagenPerfil);
        
        JLabel nombreUsuario = new JLabel(current.getUsername());
        nombreUsuario.setBounds(140, 45, 200, 35);
        nombreUsuario.setForeground(Color.WHITE);
        nombreUsuario.setFont(new Font("Minecraft", Font.BOLD, 18));
        nombreUsuario.setHorizontalAlignment(JTextField.LEFT);
        nombreUsuario.setOpaque(false);
        bg.add(nombreUsuario);

        JLabel btnCheck = createImageButton(BTN_CHECK, BTN_CHECK_PRESS, 130, 70, this::onReports);
        btnCheck.setBounds(61, 480, 120, 55);
        bg.add(btnCheck);

        JLabel btnEdit = createImageButton(BTN_EDIT, BTN_EDIT_PRESS, 130, 70, this::onProfile);
        btnEdit.setBounds(922, 480, 120, 55);
        bg.add(btnEdit);
        
        int bigW = 250, bigH = 145;
        int y = 420, xPlay = 200, xConfig = 425, xExit = 650;

        JLabel btnPlay = createImageButton(BTN_PLAY, BTN_PLAY_PRESS, bigW, bigH, this::onPlay);
        btnPlay.setBounds(xPlay, y, bigW, bigH);
        bg.add(btnPlay);

        JLabel btnConfig = createImageButton(BTN_CONFIG, BTN_CONFIG_PRESS, bigW-20, bigH-15, this::onConfig);
        btnConfig.setBounds(xConfig, y, bigW, bigH);
        bg.add(btnConfig);

        JLabel btnExit = createImageButton(BTN_EXIT, BTN_EXIT_PRESS, bigW-15, bigH-15, this::onExit);
        btnExit.setBounds(xExit+5, y+8, bigW-15, bigH-15);
        bg.add(btnExit);

        JLabel bgPNG = new JLabel(cargarIconoEscalado(BG_PNG, 1100, 590));
        bgPNG.setBounds(0, 0, 1100, 590);
        bgPNG.setLayout(null);
        bg.add(bgPNG);
    }

    
    private void onPlay() {
        dispose();
        new SelectPlayerWindow(manager, current).setVisible(true);
    }
    
    
    private void onConfig() {
        dispose();
        new ConfigWindow(manager, current).setVisible(true);  
    }
    
    
    private void onReports() { 
        dispose();
        new StatsWindow(manager, current).setVisible(true);
    }
    
    
    private void onProfile() { 
        dispose();
        new EditProfileWindow(manager, current).setVisible(true);
    }

    private void onExit() {
        Mensaje.showConfirmWithAction(this, "fondo3.png", "Deseas cerrar sesion?","exitBTN.png", "exitPRESS_BTN.png", "cancelBTN.png", "cancelPRESS_BTN.png",() -> {
            MenuWindow mw= new MenuWindow(manager);
            mw.setVisible(true);
            dispose();
        });
    }

    //Formato de creacion de botones
    private JLabel createImageButton(String imgNormal, String imgPressed, int w, int h, Runnable action) {
        ImageIcon normal = cargarIconoEscalado(imgNormal, w, h);
        ImageIcon pressed = cargarIconoEscalado(imgPressed, w, h);

        JLabel btn = new JLabel(normal);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            private boolean inside = false;

            @Override public void mouseEntered(java.awt.event.MouseEvent e) { inside = true; }
            @Override public void mouseExited (java.awt.event.MouseEvent e)  { inside = false; btn.setIcon(normal); }

            @Override public void mousePressed(java.awt.event.MouseEvent e) { btn.setIcon(pressed); }

            @Override public void mouseReleased(java.awt.event.MouseEvent e) {
                btn.setIcon(normal);
                if (inside && SwingUtilities.isLeftMouseButton(e)) action.run();
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
    
    private ImageIcon cargarImagenPlayer(Player p, int w, int h) {
        try {
            String path = p.getImagePath();
            if (path == null || path.isBlank()) return null;

            Image img;
            File f = new File(path);
            if (f.exists()) {
                img = new ImageIcon(path).getImage();
            } else {
                URL url = getClass().getResource(ASSETS + path);
                if (url == null) return null;
                img = new ImageIcon(url).getImage();
            }

            img = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception ex) {
            return null;
        }
    }
}

