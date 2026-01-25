/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BattleShip;

/**
 *
 * @author axelr
 */
import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ExitDialog extends JDialog {

    private static final String ASSETS = "/BattleShip/assets/";

    private static final String BG_EXIT = "fondo3.png";
    private static final String BTN_CANCEL = "cancelBTN.png";
    private static final String BTN_EXIT = "exitBTN.png";

    // Tamaño del diálogo (ajustado al mockup)
    private static final int DIALOG_W = 420;
    private static final int DIALOG_H = 200;

    public ExitDialog(JFrame parent) {
        super(parent, true); // modal
        configureDialog();
        buildUI();
    }

    private void configureDialog() {
        setUndecorated(true);
        setSize(DIALOG_W, DIALOG_H);
        setLocationRelativeTo(getParent());
    }

    private void buildUI() {
        JPanel content = new JPanel(null);
        setContentPane(content);

        // Fondo
        JLabel background = new JLabel();
        background.setBounds(0, 0, DIALOG_W, DIALOG_H);
        background.setIcon(loadScaledIcon(BG_EXIT, DIALOG_W, DIALOG_H));
        background.setLayout(null);
        content.add(background);

        // =========================
        // BOTONES (ESCALADOS)
        // =========================
        int btnW = 170;
        int btnH = 90;
        int y = 90;

        JButton btnCancel = createScaledButton(BTN_CANCEL, btnW, btnH);
        JButton btnExit = createScaledButton(BTN_EXIT, btnW, btnH);

        btnCancel.setBounds(30, y, btnW, btnH);
        btnExit.setBounds(220, y, btnW, btnH);

        background.add(btnCancel);
        background.add(btnExit);

        // Acciones
        btnCancel.addActionListener(e -> dispose());
        btnExit.addActionListener(e -> System.exit(0));
    }

    private JButton createScaledButton(String img, int w, int h) {
        JButton btn = new JButton();
        btn.setIcon(loadScaledIcon(img, w, h));

        btn.setBorder(null);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // IMPORTANTE: evita márgenes internos de Swing
        btn.setMargin(new Insets(0, 0, 0, 0));

        return btn;
    }

    private ImageIcon loadScaledIcon(String file, int w, int h) {
        URL url = getClass().getResource(ASSETS + file);
        if (url == null) {
            throw new RuntimeException("No se encontró: " + ASSETS + file);
        }

        Image img = new ImageIcon(url)
                .getImage()
                .getScaledInstance(w, h, Image.SCALE_SMOOTH);

        return new ImageIcon(img);
    }
}