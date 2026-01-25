/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BattleShip;

/**
 *
 * @author axelr
 */
import BattleShip.core.PlayerManager;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MenuWindow extends JFrame {
    PlayerManager manager;

    private static final String ASSETS_PATH = "/BattleShip/assets/";

    // Recursos (según sus nombres)
    private static final String IMG_BACKGROUND = "fondo1.png"; // o "fondo1.png" si desea
    private static final String IMG_LOGO = "Logo.png";

    private static final String BTN_LOGIN  = "loginBTN.png";
    private static final String BTN_CREATE = "createBTN.png";
    private static final String BTN_EXIT   = "exitBTN.png";

    // Tamaño ventana (ajústelo a su gusto)
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

        // Fondo escalado al tamaño de ventana
        background = new JLabel();
        background.setBounds(0, 0, WIN_W, WIN_H);
        background.setIcon(loadScaledIcon(IMG_BACKGROUND, WIN_W, WIN_H));
        background.setLayout(null);
        content.add(background);

        // =========================
        // LOGO "BattleShip"
        // =========================
        // Posición aproximada al mockup: zona inferior izquierda.
        // Usamos porcentajes para que funcione con cualquier tamaño de ventana.
        int logoW = (int) (WIN_W * 0.45);
        int logoH = (int) (WIN_H * 0.30);
        int logoX = (int) (WIN_W * 0.03);
        int logoY = (int) (WIN_H * 0.63);

        JLabel logo = new JLabel();
        logo.setBounds(logoX, logoY, logoW, logoH);
        logo.setIcon(loadScaledIcon(IMG_LOGO, logoW, logoH));
        background.add(logo);

        // =========================
        // BOTONES (escalados)
        // =========================
        // Tamaños relativos (ajustables)
        int btnW = (int) (WIN_W * 0.30);
        int btnH = (int) (WIN_H * 0.30);

        // Columna derecha como en el mockup
        int btnX = (int) (WIN_W * 0.60);
        int btnY1 = (int) (WIN_H * 0.02);
        int gap = (int) (WIN_H * 0.000001);

        JButton btnLogin = createScaledImageButton(BTN_LOGIN, btnW, btnH);
        JButton btnCreate = createScaledImageButton(BTN_CREATE, btnW, btnH);
        JButton btnExit = createScaledImageButton(BTN_EXIT, btnW, btnH);

        btnLogin.setBounds(btnX, btnY1, btnW, btnH);
        btnCreate.setBounds(btnX, btnY1 + btnH + gap, btnW, btnH);
        btnExit.setBounds(btnX, btnY1 + 2 * (btnH + gap), btnW, btnH);

        background.add(btnLogin);
        background.add(btnCreate);
        background.add(btnExit);

        // Acciones
        btnLogin.addActionListener(e -> onLogin());
        btnCreate.addActionListener(e -> onCreate());
        btnExit.addActionListener(e -> onExit());
    }

    private JButton createScaledImageButton(String fileName, int w, int h) {
        JButton btn = new JButton();
        btn.setIcon(loadScaledIcon(fileName, w, h));

        // Para que se vea solo la imagen
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Evita que Swing intente “rellenar” el botón en algunos Look&Feel
        btn.setMargin(new Insets(0, 0, 0, 0));
        btn.setBorder(null);

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

    // ====== Acciones (por ahora) ======
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
        ExitDialog dialog= new ExitDialog(this);
        dialog.setVisible(true);
    }

}

