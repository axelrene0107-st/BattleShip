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

public class SelectPlayerWindow extends JFrame{
    private static final String ASSETS = "/BattleShip/assets/";
    private static final String BG = "fondo11.png";
    private  static  final String BTN_PLAY = "playBTN.png";
    private  static  final String BTN_PLAY_PRESS = "playPRESS_BTN.png";
    private static final String BTN_BACK = "backBTN.png";
    private static final String BTN_BACK_PRESS = "backPRESS_BTN.png";
    private static final String BTN_SELECT = "selectBTN.png";
    private static final String BTN_SELECT_PRESS = "selectPRESS_BTN.png";
    
    private final PlayerManager manager;
    private final Player current;   
    private Player player2Selected = null;
    
    private JLabel lblP1Photo;
    private JLabel lblP2Photo;
    private JTextField txtUser2;
    
    public SelectPlayerWindow(PlayerManager manager, Player current){
        this.manager = manager;
        this.current = current;
        
        configureWindow();
        buildUI();
        cargarJugador1();
    }
    
    
    private void configureWindow(){
        setTitle("Battleship - Select Player");
        setSize(1100, 620);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    private void buildUI(){
        JLabel bg = new JLabel(cargarIconoEscalado(BG, 1100, 620));
        bg.setLayout(null);
        setContentPane(bg);             
              
        lblP1Photo= new JLabel();
        lblP1Photo.setBounds(135, 160, 260, 220);
        lblP1Photo.setHorizontalAlignment(JLabel.CENTER);
        lblP1Photo.setOpaque(false);
        bg.add(lblP1Photo);
        
        JLabel lblP1Name = new JLabel(current.getUsername());
        lblP1Name.setBounds(115, 430, 300, 45);
        lblP1Name.setForeground(Color.WHITE);
        lblP1Name.setFont(new Font("Minecraft", Font.BOLD, 28));
        lblP1Name.setHorizontalAlignment(JTextField.CENTER);
        lblP1Name.setOpaque(false);
        bg.add(lblP1Name);
        
        lblP2Photo = new JLabel();
        lblP2Photo.setBounds(705, 160, 260, 220);
        lblP2Photo.setHorizontalAlignment(JLabel.CENTER);
        lblP2Photo.setOpaque(false);
        bg.add(lblP2Photo);
        
        txtUser2 = new JTextField();
        txtUser2.setBounds(678, 428, 300, 45);
        txtUser2.setText("*ingrese jugador 2*");
        txtUser2.setFont(new Font("Minecraft", Font.BOLD, 22));
        txtUser2.setForeground(Color.WHITE);
        txtUser2.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        txtUser2.setOpaque(false);
        txtUser2.setHorizontalAlignment(JTextField.CENTER);
        bg.add(txtUser2);
              
        JLabel btnSelect = crearBotonImagen(BTN_SELECT, BTN_SELECT_PRESS, 140, 65, this::seleccionarPlayer2);
        btnSelect.setBounds(757, 474, 140, 65);
        bg.add(btnSelect);

        // ====== Botones centrales PLAY / BACK
        JLabel btnPlay = crearBotonImagen(BTN_PLAY, BTN_PLAY_PRESS, 240, 130, this::onPlay);
        btnPlay.setBounds(425, 157, 240, 130);
        bg.add(btnPlay);

        JLabel btnBack = crearBotonImagen(BTN_BACK, BTN_BACK_PRESS, 140, 80, this::onBack);
        btnBack.setBounds(475, 318, 140, 80);
        bg.add(btnBack);
    }
    
    
    private void cargarJugador1(){
        ImageIcon p1Icon = cargarImagenPlayer(current, 280, 250);
        if(p1Icon != null)
            lblP1Photo.setIcon(p1Icon);
    }
    
    private void seleccionarPlayer2(){
        String u2 = txtUser2.getText() == null ? "" : txtUser2.getText().trim();

        if (u2.isEmpty()) {
            Mensaje.showOk(this, "fondo5.png", "Favor seleccione un jugador.","okBTN.png", "okPRESS_BTN.png");
            return;
        }

        Player found = manager.conseguirPorNombre(u2); 
        if (found == null) {
            Mensaje.showOk(this, "fondo5.png", "Usuario no encontrado.","okBTN.png", "okPRESS_BTN.png");
            player2Selected = null;
            lblP2Photo.setIcon(null);
            return;
        }

        if (found.getUsername().equalsIgnoreCase(current.getUsername())) {
            Mensaje.showOk(this, "fondo5.png", "Favor no te selecciones a ti mismo 7_7.","okBTN.png", "okPRESS_BTN.png");
            player2Selected = null;
            lblP2Photo.setIcon(null);
            return;
        }

        player2Selected = found;
        ImageIcon p2Icon = cargarImagenPlayer(found, 280, 250);
        lblP2Photo.setIcon(p2Icon);

    }
    
    
    private void onPlay(){
        if(player2Selected == null){
            Mensaje.showOk(this, "fondo5.png", "Favor seleccione un jugador.","okBTN.png", "okPRESS_BTN.png");
            return;
        }
        
        Mensaje.showConfirmWithAction(this, "fondo3.png", "Iniciar partida contra "+ player2Selected.getUsername() + "?","playBTN.png", "playPRESS_BTN.png", "cancelBTN.png", "cancelPRESS_BTN.png",() -> {
            dispose();
            ColocarBarcosWindow colocar = new ColocarBarcosWindow(manager, current, player2Selected);
            colocar.setVisible(true);
        });
    }
    
    private void onBack(){
        dispose();
        new MenuPrincipalWindow(manager, current).setVisible(true);
    }
    
    
    private JLabel crearBotonImagen(String imgNormal, String imgPressed, int w, int h, Runnable action) {
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
