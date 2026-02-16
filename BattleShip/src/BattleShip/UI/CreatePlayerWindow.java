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
import java.io.File;
import java.net.URL;

public class CreatePlayerWindow extends JFrame{
    
    private static final String ASSETS = "/BattleShip/assets/";
    private static final String BG_CREATE = "fondo2.png";
    private static final String BTN_SELECT= "selectPhotoBTN.png";
    private static final String BTN_SELECT_PRESS= "selectPhotoPRESS_BTN.png";
    private static final String BTN_CANCEL = "cancelBTN.png";
    private static final String BTN_CANCEL_PRESS = "cancelPRESS_BTN.png";
    private static final String BTN_SAVE = "saveBTN.png";
    private static final String BTN_SAVE_PRESS = "savePRESS_BTN.png";

    private final PlayerManager manager;
    private String selectedImagePath = null;
    
    private JLabel bg;
    private JLabel imgPreview;   
    private JTextField txtUser;
    private JPasswordField txtPass;
    
    
    public CreatePlayerWindow (PlayerManager manager){
        this.manager = manager;
        configureWindow();
        buildUI();
    }
    
    
    private void configureWindow(){
        setTitle("BattleShip - CreatePlayer");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);       
        setSize(1100, 620);
        setLocationRelativeTo(null);
    }
    
    
    private void buildUI(){
        JPanel content = new JPanel(null);
        setContentPane(content);
        
        bg = new JLabel();
        bg.setBounds(0, 0, getWidth(), getHeight());
        bg.setIcon(loadScaledIcon(BG_CREATE, getWidth(), getHeight()));
        bg.setLayout(null);
        content.add(bg);
        
        //Preview de imagen
        imgPreview = new JLabel();
        imgPreview.setBounds(240, 50, 200, 160);
        imgPreview.setOpaque(true);
        imgPreview.setBackground(new Color(70, 80, 90));
        imgPreview.setHorizontalAlignment(SwingConstants.CENTER);
        imgPreview.setVerticalAlignment(SwingConstants.CENTER);
        bg.add(imgPreview);
        
        
        int selectW = 210;
        int selectH = 105;

        JLabel btnSelectPhoto = createImageButton(BTN_SELECT, BTN_SELECT_PRESS, selectW, selectH, this::selectPhoto);

        btnSelectPhoto.setBounds(430, 125, selectW, selectH);
        bg.add(btnSelectPhoto);
        
        txtUser = new JTextField();
        txtPass = new JPasswordField();
        
        txtUser.setBounds(240, 270, 620, 42);
        txtPass.setBounds(240, 380, 620, 42);
        
        styleField(txtUser);
        styleField(txtPass);
        
        bg.add(txtUser);
        bg.add(txtPass);
        
        int btnW = 210;
        int btnH = 105;
        
        JLabel btnCancel= createImageButton(BTN_CANCEL, BTN_CANCEL_PRESS, btnW, btnH, this::onCancel);
        JLabel btnSave= createImageButton(BTN_SAVE, BTN_SAVE_PRESS, btnW, btnH, this::onSave);
        
        btnCancel.setBounds(460, 470, btnW, btnH);
        btnSave.setBounds(655, 470, btnW, btnH);
        
        bg.add(btnCancel);
        bg.add(btnSave);
               
    }
    
    
    private void styleField(JTextField field){
        field.setFont(new Font("Minecraft", Font.PLAIN, 18));
        field.setForeground(Color.WHITE);
        field.setBackground(new Color(70, 80, 90));
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
    }
    
    
    private void selectPhoto(){
        JFileChooser chooser= new JFileChooser();
        chooser.setDialogTitle("Seleccionar foto del Player");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        int op = chooser.showOpenDialog(this);
        if(op == JFileChooser.APPROVE_OPTION){
            File f = chooser.getSelectedFile();
            selectedImagePath = f.getAbsolutePath();
            
            ImageIcon preview = new ImageIcon(selectedImagePath);
            Image scaled = preview.getImage().getScaledInstance(imgPreview.getWidth(), imgPreview.getHeight(), Image.SCALE_SMOOTH);
            imgPreview.setIcon(new ImageIcon(scaled));
        }
    }
    
    
    private void onCancel(){
        new MenuWindow(manager).setVisible(true);
        dispose();
    }
    
    
    private void onSave(){
        String user = txtUser.getText().trim();
        String pass= new String(txtPass.getPassword());
        
        if(user.isBlank() || pass.isBlank() || selectedImagePath == null){
            Mensaje.showOk(this, "fondo5.png", "Favor llene todos los espacios.","okBTN.png", "okPRESS_BTN.png");
            new MenuWindow(manager).setVisible(true);
            dispose();
            return;
        }
        
        if (manager.existeUsuario(user)) {
            Mensaje.showOk(this, "fondo5.png", "Nombre en uso, favor utilice otro.","okBTN.png", "okPRESS_BTN.png");
            new MenuWindow(manager).setVisible(true);
            dispose();
            return;
        }
        
        if(user.contains(" ") || user.length()<4){
            Mensaje.showOk(this, "fondo5.png", "Usuario sin espacios y con mas de 3 caracteres.","okBTN.png", "okPRESS_BTN.png");
            new MenuWindow(manager).setVisible(true);
            dispose();
            return;
        }
        
        if(pass.contains(" ") || pass.length()<4){
            Mensaje.showOk(this, "fondo5.png", "Contraseña sin espacios y con mas de 4 caracteres.","okBTN.png", "okPRESS_BTN.png");
            new MenuWindow(manager).setVisible(true);
            dispose();
            return;
        }
        
        Mensaje.showOk(this, "fondo5.png", "JUGADOR CREADO CON EXITO!","okBTN.png", "okPRESS_BTN.png");
        manager.crearPlayer(user, pass, selectedImagePath);
        MenuPrincipalWindow mp= new MenuPrincipalWindow(manager, manager.login(user, pass));
        mp.setVisible(true);
        dispose();
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
    
    
    private ImageIcon loadScaledIcon(String file, int w, int h){
        URL url= getClass().getResource(ASSETS + file);
        if( url == null){
            throw new RuntimeException("No se encontro: " + ASSETS + file);
        }
        Image img = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}
