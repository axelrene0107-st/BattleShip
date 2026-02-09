/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BattleShip.UI;

/**
 *
 * @author axelr
 */
import BattleShip.UI.MenuPrincipalWindow;
import BattleShip.core.PlayerManager;
import BattleShip.core.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URL;

public class EditProfileWindow extends JFrame{
    private static final String ASSETS= "/BattleShip/assets/";
    
    private static final String BG = "fondo4.png";
    
    private static final String BTN_CANCEL = "cancelBtn.png";
    private static final String BTN_SAVE = "saveBtn.png";
   
    private final PlayerManager manager;
    private final Player current;
    
    private JLabel bg;
    private JLabel imgPreview;
    private JTextField txtUser;
    private JPasswordField txtPass;
    
    private String selectedImagePath;
    private String originalUsername;
    
    
    public EditProfileWindow(PlayerManager manager, Player current){
        this.manager=manager;
        this.current=current;
        
        configureWindow();
        buildUI();
        preloadData();       
    }
    
    
    private void configureWindow(){
        setTitle("BattleShip - Edit Profile");
        setSize(1100, 620);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    
    private void buildUI(){
        JPanel content = new JPanel(null);
        setContentPane(content);
        
        bg = new JLabel();
        bg.setBounds(0, 0, getWidth(), getHeight());
        bg.setIcon(cargarIconoEscalado(BG, getWidth(), getHeight()));
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
    
    
    private void preloadData(){
        originalUsername =  safe(current.getUsername());
        
        txtUser.setText(originalUsername);
        txtPass.setText(safe(current.getPassword()));
        
        String imgPath= null;
        try{
            imgPath= current.getImagePath();
        }catch (Exception ignored){}
        
        selectedImagePath = imgPath;
        refrescarImagen(selectedImagePath);
    }
    
    
    private void selectPhoto(){
        JFileChooser chooser= new JFileChooser();
        chooser.setDialogTitle("Seleccionar foto del Player");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int op = chooser.showOpenDialog(this);
        
        if(op == JFileChooser.APPROVE_OPTION){
            File f = chooser.getSelectedFile();
            selectedImagePath = f.getAbsolutePath();           
            refrescarImagen(selectedImagePath);
        }
    }
    
    
    private void onCancel(){
        dispose();
        new MenuPrincipalWindow(manager, current).setVisible(true);
    }
    
    
    private void onSave(){
        String newUser = txtUser.getText().trim();
        String newPass = new String(txtPass.getPassword()).trim();
        
        if(newUser.isEmpty() || newPass.isEmpty()){
            JOptionPane.showMessageDialog(this, "Favor ingrese todos los datos.");
            return;
        }
        
        boolean userChanged= !newUser.equalsIgnoreCase(originalUsername);
        
        if(userChanged){
            if(manager.existeUsuario(newUser)){
                JOptionPane.showMessageDialog(this, "El usuario ya existe");
                return;
            }
        } 
        
        current.setUsername(newUser);
        current.setPassword(newPass);
        
        if(selectedImagePath != null){
            try{
                current.setImagePath(selectedImagePath);
            }catch (Exception ignored){}
        }
        
        
        try{
            manager.actualizarPlayer(current);
        }catch(Exception Ignored){}
        
        dispose();
        new MenuPrincipalWindow(manager, current).setVisible(true);
    }
    
    
    private void refrescarImagen(String absolutePath){
        if(absolutePath == null || absolutePath.isBlank()){
            imgPreview.setIcon(null);
            return;
        }
        
        ImageIcon icon = new ImageIcon(absolutePath);
        Image scaled = icon.getImage().getScaledInstance(imgPreview.getWidth(), imgPreview.getHeight(), Image.SCALE_SMOOTH);
        
        imgPreview.setIcon(new ImageIcon(scaled));
    }
    
    
    private JLabel createImageButton(String img, int w, int h, Runnable action){
        JLabel btn = new JLabel(cargarIconoEscalado(img, w, h));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter(){
           @Override
           public void mouseClicked(MouseEvent e){action.run();} 
        });        
        return btn;
    }
    
    
    private void styleField(JTextField field) {
        field.setBackground(new Color(70, 78, 85));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        field.setFont(new Font("Arial", Font.PLAIN, 18));
    }
    
    
    private ImageIcon cargarIconoEscalado(String file, int w, int h){
        URL url = getClass().getResource(ASSETS + file);
        if(url == null) throw new RuntimeException("No se encontro "+ASSETS+file);
        Image img = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_FAST);
        return new ImageIcon(img);
    }
    
    
    private String safe(String s){
        return s == null ? "" : s;
    }
    
}
    

