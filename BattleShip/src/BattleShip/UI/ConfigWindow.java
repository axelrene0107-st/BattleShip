/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BattleShip.UI;

/**
 *
 * @author axelr
 */
import BattleShip.core.*; // ajusta el paquete si es distinto

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ConfigWindow extends JFrame{
    private static final String ASSETS = "/BattleShip/assets/";
    private static final String BG = "fondo9.png";
    private static final String SS = "selectionSLOT.png";
    private static final String BTN_BACK = "backBTN.png";
    private static final String BTN_BACK_PRESS = "backPRESS_BTN.png";
    private static final String BTN_DIFFICULTY = "difficultyBTN.png";
    private static final String BTN_DIFFICULTY_PRESS = "difficultyPRESS_BTN.png";
    private static final String BTN_MODE = "modeBTN.png";
    private static final String BTN_MODE_PRESS = "modePRESS_BTN.png";
    private static final String BTN_SELECT = "selectBTN.png";
    private static final String BTN_SELECT_PRESS = "selectPRESS_BTN.png";

    private static final int W = 1100;
    private static final int H = 620;

    private final PlayerManager manager;
    private final Player playerLogged;

    private JPanel contentPanel;
    private JLabel seleccion;

    public ConfigWindow(PlayerManager manager, Player playerLogged) {
        this.manager = manager;
        this.playerLogged = playerLogged;

        configureWindow();
        buildUI();
        showDifficulty();
    }

    private void configureWindow() {
        setTitle("Battleship - Configuración");
        setSize(W, H);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void buildUI() {
        JLabel bg = new JLabel(loadScaledIcon(BG, W, H));
        bg.setLayout(null);
        setContentPane(bg);

        int btnW = 180, btnH = 100, topY = 75;

        JLabel btnBack = createImageButton(
                BTN_BACK, BTN_BACK_PRESS, btnW, btnH,
                this::onBack
        );
        btnBack.setBounds(230, topY, btnW, btnH);
        bg.add(btnBack);

        JLabel btnDiff = createImageButton(
                BTN_DIFFICULTY, BTN_DIFFICULTY_PRESS, btnW, btnH,
                this::showDifficulty
        );
        btnDiff.setBounds(450, topY, btnW, btnH);
        bg.add(btnDiff);

        JLabel btnMode = createImageButton(
                BTN_MODE, BTN_MODE_PRESS, btnW, btnH,
                this::showMode
        );
        btnMode.setBounds(670, topY, btnW, btnH);
        bg.add(btnMode);

        contentPanel = new JPanel(null);
        contentPanel.setOpaque(false);
        contentPanel.setBounds(50, 150, 1000, 420);
        bg.add(contentPanel);
    }

    private void showDifficulty() {
        contentPanel.removeAll();

        int startX = 160;
        int startY = 90;
        int w = 150;
        int gap = 20;

        Player.Difficulty[] diffs = {
                Player.Difficulty.EASY,
                Player.Difficulty.NORMAL,
                Player.Difficulty.EXPERT,
                Player.Difficulty.GENIUS
        };

        String[] titles = {"EASY", "NORMAL", "EXPERT", "GENIUS"};
        String[] ships = {"5 BARCOS", "4 BARCOS", "2 BARCOS", "1 BARCOS"};
        String[] descs = {
                "\n Para principiantes",
                "\n   Para acoplados",
                "\n   Para adaptados",
                "\n    Para viciados"
        };

        for (int i = 0; i < diffs.length; i++) {
            int x = startX + i * (w + gap);

            JLabel title = new JLabel(titles[i], SwingConstants.CENTER);
            title.setBounds(x, startY, w, 30);
            title.setForeground(Color.WHITE);
            title.setFont(new Font("Minecraft", Font.BOLD, 20));
            contentPanel.add(title);

            JLabel lblShips = new JLabel(ships[i], SwingConstants.CENTER);
            lblShips.setBounds(x, startY + 40, w, 30);
            lblShips.setFont(new Font("Minecraft", Font.PLAIN, 16));
            lblShips.setOpaque(true);
            lblShips.setBackground(new Color(35, 40, 45));
            lblShips.setForeground(Color.WHITE);
            contentPanel.add(lblShips);

            JTextArea info = new JTextArea(descs[i]);
            info.setBounds(x, startY + 80, w, 80);
            info.setFont(new Font("Minecraft", Font.PLAIN, 16));
            info.setOpaque(true);
            info.setEditable(false);
            info.setBackground(new Color(35, 40, 45));
            info.setForeground(Color.WHITE);
            contentPanel.add(info);

            Player.Difficulty diff = diffs[i];

            JLabel btnSelect = createImageButton(
                    BTN_SELECT, BTN_SELECT_PRESS, 130, 70,
                    () -> {
                        playerLogged.setDifficulty(diff);
                        showDifficulty();
                    }
            );
            btnSelect.setBounds(x + 10, startY + 180, 130, 70);
            contentPanel.add(btnSelect);
        }

        seleccion = new JLabel(loadScaledIcon(SS, w, 60));
        seleccion.setBounds(getDifficultyX(startX, w, gap), startY - 35, w, 90);
        contentPanel.add(seleccion);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private int getDifficultyX(int startX, int w, int gap) {
        return startX + switch (playerLogged.getDifficulty()) {
            case EASY -> 0;
            case NORMAL -> 1 * (w + gap);
            case EXPERT -> 2 * (w + gap);
            case GENIUS -> 3 * (w + gap);
        };
    }

    private void showMode() {
        contentPanel.removeAll();

        int startX = 260;
        int startY = 100;
        int w = 200;
        int gap = 50;

        Player.Mode[] modes = {
                Player.Mode.ARCADE,
                Player.Mode.TUTORIAL
        };

        String[] titles = {"ARCADE", "TUTORIAL"};
        String[] descs = {
                "\n          Modo normal\n  con barcos escondidos",
                "\n          Modo tutorial\n     con barcos visibles"
        };

        for (int i = 0; i < modes.length; i++) {
            int x = startX + i * (w + gap);

            JLabel title = new JLabel(titles[i], SwingConstants.CENTER);
            title.setBounds(x, startY, w, 30);
            title.setForeground(Color.WHITE);
            title.setFont(new Font("Minecraft", Font.BOLD, 22));
            contentPanel.add(title);

            JTextArea info = new JTextArea(descs[i]);
            info.setBounds(x, startY + 60, w, 80);
            info.setFont(new Font("Minecraft", Font.PLAIN, 16));
            info.setOpaque(true);
            info.setEditable(false);
            info.setBackground(new Color(35, 40, 45));
            info.setForeground(Color.WHITE);
            contentPanel.add(info);

            Player.Mode mode = modes[i];

            JLabel btnSelect = createImageButton(
                    BTN_SELECT, BTN_SELECT_PRESS, 130, 70,
                    () -> {
                        playerLogged.setMode(mode);
                        showMode();
                    }
            );
            btnSelect.setBounds(x + 35, startY + 160, 130, 70);
            contentPanel.add(btnSelect);
        }

        seleccion = new JLabel(loadScaledIcon(SS, w, 60));
        seleccion.setBounds(getModeX(startX, w, gap), startY - 35, w, 90);
        contentPanel.add(seleccion);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private int getModeX(int startX, int w, int gap) {
        return (playerLogged.getMode() == Player.Mode.ARCADE)
                ? startX
                : startX + w + gap;
    }

    private void onBack() {
        dispose();
        new MenuPrincipalWindow(manager, playerLogged).setVisible(true);
    }

    private JLabel createImageButton(String normal, String pressed, int w, int h, Runnable action) {
        ImageIcon n = loadScaledIcon(normal, w, h);
        ImageIcon p = loadScaledIcon(pressed, w, h);

        JLabel btn = new JLabel(n);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mousePressed(java.awt.event.MouseEvent e) { btn.setIcon(p); }
            @Override public void mouseReleased(java.awt.event.MouseEvent e) {
                btn.setIcon(n);
                action.run();
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) { btn.setIcon(n); }
        });
        return btn;
    }

    private ImageIcon loadScaledIcon(String file, int w, int h) {
        URL url = getClass().getResource(ASSETS + file);
        if (url == null) throw new RuntimeException("No se encontró: " + file);
        Image img = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_FAST);
        return new ImageIcon(img);
    }
}
