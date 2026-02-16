/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BattleShip.UI;

/**
 *
 * @author axelr
 */
import BattleShip.core.PlayerManager;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MenuWindow extends JFrame {
    private static final String ASSETS_PATH = "/BattleShip/assets/";
    private static final String IMG_BACKGROUND = "fondo1.png"; 
    private static final String BTN_LOGIN  = "loginBTN.png";
    private static final String BTN_LOGIN_PRESS  = "loginPRESS_BTN.png";
    private static final String BTN_CREATE = "createBTN.png";
    private static final String BTN_CREATE_PRESS = "createPRESS_BTN.png";
    private static final String BTN_EXIT   = "exitBTN.png";
    private static final String BTN_EXIT_PRESS   = "exitPRESS_BTN.png";

    PlayerManager manager;
    
    private static final int WIN_W = 1100;
    private static final int WIN_H = 620;

    private JLabel background;

    public MenuWindow(PlayerManager manager) {
        this.manager= manager;
        configureWindow();
        buildUI();
    }

    private void configureWindow() {
        setTitle("Battleship - Menú de Inicio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        setSize(WIN_W, WIN_H);
        setLocationRelativeTo(null);
    }

    private void buildUI() {
        JPanel content = new JPanel(null);
        setContentPane(content);

        background = new JLabel();
        background.setBounds(0, 0, WIN_W, WIN_H);
        background.setIcon(loadScaledIcon(IMG_BACKGROUND, WIN_W, WIN_H));
        background.setLayout(null);
        content.add(background);

        int btnW = (int) (WIN_W * 0.30);
        int btnH = (int) (WIN_H * 0.30);

        int btnX = 670;
        int btnY1 = 70;

        JLabel btnLogin = createImageButton(BTN_LOGIN, BTN_LOGIN_PRESS, btnW, btnH, this::onLogin);
        JLabel btnCreate = createImageButton(BTN_CREATE, BTN_CREATE_PRESS, btnW, btnH, this::onCreate);
        JLabel btnExit = createImageButton(BTN_EXIT, BTN_EXIT_PRESS, btnW-40, btnH-20, this::onExit);

        btnLogin.setBounds(btnX, btnY1, btnW, btnH);
        btnCreate.setBounds(btnX, btnY1+145, btnW, btnH);
        btnExit.setBounds(btnX+20, btnY1 + 300, btnW-40, btnH-20);

        background.add(btnLogin);
        background.add(btnCreate);
        background.add(btnExit);

    }

    private JLabel createImageButton(String imgNormal, String imgPressed, int w, int h, Runnable action) {
        ImageIcon normal = loadScaledIcon(imgNormal, w, h);
        ImageIcon pressed = loadScaledIcon(imgPressed, w, h);

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

    private ImageIcon loadIcon(String fileName) {
        URL url = getClass().getResource(ASSETS_PATH + fileName);
        if (url == null) {
            throw new RuntimeException("No se encontró el recurso: " + ASSETS_PATH + fileName);
        }
        return new ImageIcon(url);
    }

    private ImageIcon loadScaledIcon(String fileName, int w, int h) {
        ImageIcon icon = loadIcon(fileName);
        Image scaled = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private void onLogin() {
        LoginWindow w = new LoginWindow(manager);
        w.setVisible(true);
        dispose();
    }

    private void onCreate() {
        CreatePlayerWindow w = new CreatePlayerWindow(manager);
        w.setVisible(true);
        dispose();
    }

    private void onExit() {
        boolean ok = Mensaje.showConfirm(this, "fondo3.png", "Seguro que deseas salir?","exitBTN.png", "exitPRESS_BTN.png", "cancelBTN.png", "cancelPRESS_BTN.png");
        if(ok){
            dispose();
        }
    }

}

