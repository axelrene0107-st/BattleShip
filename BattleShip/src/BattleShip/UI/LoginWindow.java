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
import java.net.URL;

public class LoginWindow extends JFrame{
    private static final String ASSETS = "/BattleShip/assets/";

    private static final String BG_LOGIN = "fondo6.png";
    private static final String BTN_CANCEL = "cancelBTN.png";
    private static final String BTN_CANCEL_PRESS = "cancelPRESS_BTN.png";
    private static final String BTN_LOGIN = "loginBTN.png";
    private static final String BTN_LOGIN_PRESS = "loginPRESS_BTN.png";

    private final PlayerManager manager;

    private JTextField txtUser;
    private JPasswordField txtPass;

    public LoginWindow(PlayerManager manager) {
        this.manager = manager;
        configureWindow();
        buildUI();
    }
    
    private void configureWindow() {
        setTitle("Battleship - Login");
        setSize(1100, 620);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    private void buildUI() {
        JPanel content = new JPanel(null);
        setContentPane(content);

        // =========================
        // FONDO (fondo6.png)
        // =========================
        JLabel bg = new JLabel();
        bg.setBounds(0, 0, getWidth(), getHeight());
        bg.setIcon(loadScaledIcon(BG_LOGIN, getWidth(), getHeight()));
        bg.setLayout(null);
        content.add(bg);

        // =========================
        // CAMPOS (encima del fondo)
        // =========================
        txtUser = new JTextField();
        txtPass = new JPasswordField();

        styleField(txtUser);
        styleField(txtPass);

        // Posiciones alineadas al mockup
        txtUser.setBounds(340, 295, 430, 42);
        txtPass.setBounds(340, 425, 430, 42);

        bg.add(txtUser);
        bg.add(txtPass);

        // =========================
        // BOTONES (JLabel)
        // =========================
        int btnW = 220;
        int btnH = 110;

        JLabel btnCancel = createImageButton(BTN_CANCEL, BTN_CANCEL_PRESS, btnW, btnH, this::onCancel);
        JLabel btnLogin = createImageButton(BTN_LOGIN, BTN_LOGIN_PRESS, btnW, btnH, this::onLogin);

        btnCancel.setBounds(350, 475, btnW, btnH);
        btnLogin.setBounds(550, 475, btnW, btnH);

        bg.add(btnCancel);
        bg.add(btnLogin);
    }
    
     private void onLogin() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword());

        if (user.isBlank() || pass.isBlank()) {
            Mensaje.showOk(this, "fondo5.png", "Favor llene todos los espacios.","okBTN.png", "okPRESS_BTN.png");
            new MenuWindow(manager).setVisible(true);
            dispose();
            return;
        }

        Player p = manager.login(user, pass);

        if (p == null) {
            Mensaje.showOk(this, "fondo5.png", "Usuario o contraseña incorrectos.","okBTN.png", "okPRESS_BTN.png");
            new MenuWindow(manager).setVisible(true);
            dispose();
            return;
        }

        Mensaje.showOk(this, "fondo5.png", "Se ingreso con exito, bienvenido " + user,"okBTN.png", "okPRESS_BTN.png");
        new MenuPrincipalWindow(manager, p).setVisible(true);
        dispose();
    }
     
     private void onCancel() {
        new MenuWindow(manager).setVisible(true);
        dispose();
    }
     
     private void styleField(JTextField field) {
        field.setFont(new Font("Minecraft", Font.PLAIN, 18));
        field.setForeground(Color.WHITE);
        field.setBackground(new Color(70, 80, 90));
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
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
     
     private ImageIcon loadScaledIcon(String file, int w, int h) {
        URL url = getClass().getResource(ASSETS + file);
        if (url == null) {
            throw new RuntimeException("No se encontró: " + ASSETS + file);
        }
        Image img = new ImageIcon(url).getImage()
                .getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}
