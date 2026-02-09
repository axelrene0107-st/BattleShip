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
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;

public class SelectPlayerWindow extends JFrame{
    private static final String ASSETS = "/BattleShip/assets/";
    private static final String BG = "fondo11.png";
    private static final String LOGO = "Logo.png";
    private  static  final String BTN_PLAY = "playBTN.png";
    private static final String BTN_BACK = "backBTN.png";
    private static final String BTN_SELECT = "selectBTN.png";
    
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
               
        JLabel logo = new JLabel(cargarIconoEscalado(LOGO, 240, 110));
        logo.setBounds(420, 100, 240, 110);
        bg.add(logo);
              
        lblP1Photo= new JLabel();
        lblP1Photo.setBounds(100, 120, 320, 280);
        lblP1Photo.setBackground(new Color(35, 35, 40));
        lblP1Photo.setHorizontalAlignment(JLabel.CENTER);
        lblP1Photo.setOpaque(true);
        bg.add(lblP1Photo);
        
        JLabel lblP1Name = new JLabel(current.getUsername());
        lblP1Name.setBounds(100, 425, 320, 45);
        lblP1Name.setForeground(Color.WHITE);
        lblP1Name.setBackground(new Color(35, 35, 40));
        lblP1Name.setBorder(new LineBorder(new Color(80, 80, 90), 2));
        lblP1Name.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblP1Name.setHorizontalAlignment(JTextField.CENTER);
        lblP1Name.setOpaque(true);
        bg.add(lblP1Name);
        
        lblP2Photo = new JLabel();
        lblP2Photo.setBounds(660, 120, 320, 280);
        lblP2Photo.setBackground(new Color(35, 35, 40));
        lblP2Photo.setHorizontalAlignment(JLabel.CENTER);
        lblP2Photo.setOpaque(true);
        bg.add(lblP2Photo);
        
        txtUser2 = new JTextField();
        txtUser2.setBounds(660, 425, 320, 45);
        txtUser2.setText("*ingrese jugador 2*");
        txtUser2.setFont(new Font("SansSerif", Font.BOLD, 22));
        txtUser2.setForeground(Color.WHITE);
        txtUser2.setCaretColor(Color.WHITE);
        txtUser2.setBackground(new Color(35, 35, 40));
        txtUser2.setBorder(new LineBorder(new Color(80, 80, 90), 2));
        txtUser2.setOpaque(true);
        txtUser2.setHorizontalAlignment(JTextField.CENTER);
        bg.add(txtUser2);
              
        JLabel btnSelect = crearBotonImagen(BTN_SELECT, 140, 70, this::seleccionarPlayer2);
        btnSelect.setBounds(750, 470, 140, 70);
        bg.add(btnSelect);

        // ====== Botones centrales PLAY / BACK
        JLabel btnPlay = crearBotonImagen(BTN_PLAY, 240, 130, this::onPlay);
        btnPlay.setBounds(420, 210, 240, 130);
        bg.add(btnPlay);

        JLabel btnBack = crearBotonImagen(BTN_BACK, 140, 80, this::onBack);
        btnBack.setBounds(470, 310, 140, 80);
        bg.add(btnBack);
    }
    
    
    private void cargarJugador1(){
        ImageIcon p1Icon = cargarImagenPlayer(current, 260, 250);
        if(p1Icon != null)
            lblP1Photo.setIcon(p1Icon);
    }
    
    private void seleccionarPlayer2(){
        String u2 = txtUser2.getText() == null ? "" : txtUser2.getText().trim();

        if (u2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el usuario del jugador 2.");
            return;
        }

        Player found = manager.conseguirPorNombre(u2); 
        if (found == null) {
            JOptionPane.showMessageDialog(this, "Usuario inexistente.");
            player2Selected = null;
            lblP2Photo.setIcon(null);
            return;
        }

        if (found.getUsername().equalsIgnoreCase(current.getUsername())) {
            JOptionPane.showMessageDialog(this, "No puedes seleccionar al mismo usuario como jugador 2.");
            player2Selected = null;
            lblP2Photo.setIcon(null);
            return;
        }

        player2Selected = found;
        ImageIcon p2Icon = cargarImagenPlayer(found, 260, 250);
        lblP2Photo.setIcon(p2Icon);

    }
    
    
    private void onPlay(){
        if(player2Selected == null){
            JOptionPane.showMessageDialog(this, "Por favor seleccione un jugador.");
            return;
        }
        
        System.out.println("PLAY -> P1=" + current.getUsername() + " vs P2=" + player2Selected.getUsername());
    }
    
    private void onBack(){
        dispose();
        new MenuPrincipalWindow(manager, current).setVisible(true);
    }
    
    
    private JLabel crearBotonImagen(String img, int w, int h, Runnable action) {
        JLabel btn = new JLabel(cargarIconoEscalado(img, w, h));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { action.run(); }
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
