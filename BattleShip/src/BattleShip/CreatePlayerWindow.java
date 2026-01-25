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
import java.io.File;
import java.net.URL;

public class CreatePlayerWindow extends JFrame{
    
    private static final String ASSETS = "/BattleShip/assets/";
    private static final String BG_CREATE = "fondo4.png";
    private static final String BTN_SELECT= "selectPhotoBTN.png";
    private static final String BTN_CANCEL = "cancelBTN.png";
    private static final String BTN_SAVE = "saveBTN.png";

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
        imgPreview.setBounds(220, 50, 200, 160);
        imgPreview.setOpaque(true);
        imgPreview.setBackground(new Color(70, 80, 90));
        imgPreview.setHorizontalAlignment(SwingConstants.CENTER);
        imgPreview.setVerticalAlignment(SwingConstants.CENTER);
        bg.add(imgPreview);
        
        
        int selectW = 210;
        int selectH = 105;

        JLabel btnSelectPhoto = createImageButton("selectPhotoBTN.png", selectW, selectH, this::selectPhoto);

        btnSelectPhoto.setBounds(430, 125, selectW, selectH);
        bg.add(btnSelectPhoto);
        
        txtUser = new JTextField();
        txtPass = new JPasswordField();
        
        txtUser.setBounds(220, 280, 620, 42);
        txtPass.setBounds(220, 410, 620, 42);
        
        styleField(txtUser);
        styleField(txtPass);
        
        bg.add(txtUser);
        bg.add(txtPass);
        
        int btnW = 210;
        int btnH = 105;
        
        JLabel btnCancel= createImageButton(BTN_CANCEL, btnW, btnH, this::onCancel);
        JLabel btnSave= createImageButton(BTN_SAVE, btnW, btnH, this::onSave);
        
        btnCancel.setBounds(460, 470, btnW, btnH);
        btnSave.setBounds(655, 470, btnW, btnH);
        
        bg.add(btnCancel);
        bg.add(btnSave);
               
    }
    
    
    private void styleField(JTextField field){
        field.setFont(new Font("Arial", Font.PLAIN, 18));
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
            new MessageDialog(this, manager, "fondoError2.png").setVisible(true);
            dispose();
            return;
        }
        
        if (manager.existeUsuario(user)) {
            new MessageDialog(this, manager, "fondoError1.png").setVisible(true);
            dispose();
            return;
        }
        
        manager.crearPlayer(user, pass, selectedImagePath);
        new MessageDialog(this, manager, "fondo5.png").setVisible(true);
        dispose();
    }
    
    
    private JLabel createImageButton(String img, int w, int h, Runnable action){
        JLabel btn = new JLabel();
        btn.setIcon(loadScaledIcon(img, w, h));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter(){
            @Override public void  mouseClicked(java.awt.event.MouseEvent e){
                action.run();
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
