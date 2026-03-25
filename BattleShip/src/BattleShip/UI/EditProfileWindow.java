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
import BattleShip.core.Player;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;

public class EditProfileWindow extends JFrame{
    private static final String ASSETS= "/BattleShip/assets/";
    private static final String BG = "fondo4.png";
    private static final String BTN_SELECTPHOTO = "selectPhotoBTN.png";
    private static final String BTN_SELECTPHOTO_PRESS = "selectPhotoPRESS_BTN.png";
    private static final String BTN_DELETE = "deleteBTN.png";
    private static final String BTN_DELETE_PRESS = "deletePRESS_BTN.png";
    private static final String BTN_CANCEL = "cancelBtn.png";
    private static final String BTN_CANCEL_PRESS = "cancelPRESS_Btn.png";
    private static final String BTN_SAVE = "saveBTN.png";
    private static final String BTN_SAVE_PRESS = "savePRESS_Btn.png";
   
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
        imgPreview.setBounds(240, 50, 200, 160);
        imgPreview.setOpaque(true);
        imgPreview.setBackground(new Color(70, 80, 90));
        imgPreview.setHorizontalAlignment(SwingConstants.CENTER);
        imgPreview.setVerticalAlignment(SwingConstants.CENTER);
        bg.add(imgPreview);
        
        
        int selectW = 210;
        int selectH = 105;

        JLabel btnSelectPhoto = createImageButton(BTN_SELECTPHOTO, BTN_SELECTPHOTO_PRESS, selectW, selectH, this::selectPhoto);
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
        
        JLabel btnDelete= createImageButton(BTN_DELETE, BTN_DELETE_PRESS, btnW, btnH, this::onDelete);
        btnDelete.setBounds(255, 470, btnW, btnH);
        bg.add(btnDelete);
        
        JLabel btnCancel= createImageButton(BTN_CANCEL, BTN_CANCEL_PRESS, btnW, btnH, this::onCancel);
        btnCancel.setBounds(450, 470, btnW, btnH);
        
        JLabel btnSave= createImageButton(BTN_SAVE, BTN_SAVE_PRESS, btnW, btnH, this::onSave);
        btnSave.setBounds(645, 470, btnW, btnH);
        
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
            Mensaje.showOk(this, "fondo5.png", "Favor llene todos los espacios.","okBTN.png", "okPRESS_BTN.png");
            return;
        }
        
        boolean userChanged= !newUser.equalsIgnoreCase(originalUsername);
        
        if(userChanged){
            if(manager.existeUsuario(newUser)){
                Mensaje.showOk(this, "fondo5.png", "Nombre en uso.","okBTN.png", "okPRESS_BTN.png");
                return;
            }
        } 
        
        if(newPass.contains(" ") || newPass.length()<4){
            Mensaje.showOk(this, "fondo5.png", "Contraseña sin espacios y con mas de 4 caracteres.","okBTN.png", "okPRESS_BTN.png");
            return;
        }
        
        Mensaje.showConfirmWithAction(this, "fondo3.png", "Deseas guardar los cambios? ","saveBTN.png", "savePRESS_BTN.png", "cancelBTN.png", "cancelPRESS_BTN.png",() -> {
            current.setUsername(newUser);
            current.setPassword(newPass);
        });
        
        if(selectedImagePath != null){
            //Trycatch del metodo setImagePath() de player
            try{
                current.setImagePath(selectedImagePath);
            }catch (Exception ignored){
                    Mensaje.showOk(this, "fondo5.png", "Favor seleccione una imagen.","okBTN.png", "okPRESS_BTN.png");
                return;
            }
        }        
  
        //Try catch del metodo actualizarPlayer() IllegalArgumetnException
        try{
            manager.actualizarPlayer(current);
        }catch(Exception Ignored){
            Mensaje.showOk(this, "fondo5.png", "No se pudieron guardar los cambios.","okBTN.png", "okPRESS_BTN.png");
        }
        
        
        dispose();
        new MenuPrincipalWindow(manager, current).setVisible(true);
    }
    
    private void onDelete(){
        Mensaje.showConfirmWithAction(this, "fondo3.png", "Seguro deseas eliminar tu cuenta?","deleteBTN.png", "deletePRESS_BTN.png", "cancelBTN.png", "cancelPRESS_BTN.png",() -> {
            manager.eliminarPlayer(current.getUsername());
            dispose();
            new MenuWindow(manager).setVisible(true);
        });
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
    
    
    private void styleField(JTextField field) {
        field.setBackground(new Color(70, 78, 85));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        field.setFont(new Font("Minecraft", Font.PLAIN, 18));
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
    

