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

public class MessageDialog extends JDialog{
    
    private static final String ASSETS = "/BattleShip/assets/";
    private static final String BTN_OK = "okBTN.png";
    private PlayerManager manager;
    
    public MessageDialog(JFrame parent, PlayerManager manager, String backgroundFile) {
        super(parent, true); // modal
        this.manager = manager;
        configureDialog();
        buildUI(backgroundFile);
    }
    
    private void configureDialog() {
        setUndecorated(true);
        setSize(700, 280); // tamaño cómodo para sus fondos de texto
        setLocationRelativeTo(getParent());
    }
    
    private void buildUI(String backgroundFile) {
        JPanel content = new JPanel(null);
        setContentPane(content);

        JLabel background = new JLabel();
        background.setBounds(0, 0, getWidth(), getHeight());
        background.setIcon(loadScaledIcon(backgroundFile, getWidth(), getHeight()));
        background.setLayout(null);
        content.add(background);

        // Botón OK
        int btnW = 190;
        int btnH = 90;

        JLabel btnOk = createImageButton(BTN_OK, btnW, btnH, this::goToMenu);

        // Centrado abajo
        btnOk.setBounds((getWidth() - btnW) / 2, getHeight() - btnH - 18, btnW, btnH);

        background.add(btnOk);
    }
    
    private void goToMenu() {
        dispose(); // cerrar dialog
        new MenuWindow(manager).setVisible(true);
    }
    
    private JLabel createImageButton(String img, int w, int h, Runnable action) {
        JLabel btn = new JLabel();
        btn.setIcon(loadScaledIcon(img, w, h));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                action.run();
            }
        });
        return btn;
    }
    
    private ImageIcon loadScaledIcon(String file, int w, int h) {
        URL url = getClass().getResource(ASSETS + file);
        if (url == null) {
            throw new RuntimeException("No se encontró: " + ASSETS + file);
        }
        Image img = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}
