/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BattleShip.UI;

/**
 *
 * @author axelr
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class Mensaje extends JDialog{
    private static final String ASSETS = "/BattleShip/assets/";

    private static final int D_W = 400;
    private static final int D_H = 200;

    private static final int BTN_W = 120;
    private static final int BTN_H = 65;

    private static final int Y_BTN = 118;

    private static final int X_OK  = (D_W - BTN_W) / 2;     
    private static final int X_NO  = 55;                    
    private static final int X_YES = D_W - 55 - BTN_W;      

    private static final int MSG_X = 30;
    private static final int MSG_Y = 40;
    private static final int MSG_W = D_W - 60;
    private static final int MSG_H = 70;

    private final JLabel bg = new JLabel();
    private final JLabel lblMsg = new JLabel("", SwingConstants.CENTER);

    private boolean accepted = false;

    private Mensaje(Window owner, String bgFile, String message,
                    ButtonKind primaryKind, Runnable onPrimary,
                    ButtonKind secondaryKind, Runnable onSecondary) {

        super(owner, ModalityType.APPLICATION_MODAL);
        setUndecorated(true);
        setSize(D_W, D_H);
        setResizable(false);
        setLocationRelativeTo(owner);

        bg.setLayout(null);
        bg.setBounds(0, 0, D_W, D_H);
        bg.setIcon(loadScaled(bgFile, D_W, D_H));
        setContentPane(bg);

        lblMsg.setText(toHtmlCentered(message));
        lblMsg.setForeground(Color.WHITE);
        lblMsg.setFont(new Font("Minecraft", Font.BOLD, 16));
        lblMsg.setBounds(MSG_X, MSG_Y, MSG_W, MSG_H);
        bg.add(lblMsg);
        
        if (primaryKind != null) {
            JLabel b1 = buildButton(primaryKind, () -> {
                accepted = true;
                if (onPrimary != null) onPrimary.run();
                dispose();
            });
            b1.setBounds(primaryKind.x, Y_BTN, BTN_W, BTN_H);
            bg.add(b1);
        }

        if (secondaryKind != null) {
            JLabel b2 = buildButton(secondaryKind, () -> {
                accepted = false;
                if (onSecondary != null) onSecondary.run();
                dispose();
            });
            b2.setBounds(secondaryKind.x, Y_BTN, BTN_W, BTN_H);
            bg.add(b2);
        }
    }

    public static void showOk(Window owner, String bgFile, String message, String okNormal, String okPressed) {
        ButtonKind OK = new ButtonKind(okNormal, okPressed, X_OK);
        Mensaje d = new Mensaje(owner, bgFile, message, OK, null, null, null);
        d.setVisible(true);
    }

    public static boolean showConfirm(Window owner, String bgFile, String message,String yesNormal, String yesPressed,String noNormal, String noPressed) {

        ButtonKind YES = new ButtonKind(yesNormal, yesPressed, X_YES);
        ButtonKind NO  = new ButtonKind(noNormal,  noPressed,  X_NO);

        Mensaje d = new Mensaje(owner, bgFile, message, YES, null, NO, null);
        d.setVisible(true);
        return d.accepted;
    }

    public static boolean showConfirmWithAction(Window owner, String bgFile, String message, String yesNormal, String yesPressed, String noNormal, String noPressed, Runnable onYes) {

        ButtonKind YES = new ButtonKind(yesNormal, yesPressed, X_YES);
        ButtonKind NO  = new ButtonKind(noNormal,  noPressed,  X_NO);

        Mensaje d = new Mensaje(owner, bgFile, message, YES, onYes, NO, null);
        d.setVisible(true);
        return d.accepted;
    }

    private static class ButtonKind {
        final String normal;
        final String pressed;
        final int x;

        ButtonKind(String normal, String pressed, int x) {
            this.normal = normal;
            this.pressed = pressed;
            this.x = x;
        }
    }


    private static JLabel buildButton(ButtonKind kind, Runnable action) {
        ImageIcon normal = loadScaled(kind.normal, BTN_W, BTN_H);
        ImageIcon pressed = (kind.pressed != null) ? loadScaled(kind.pressed, BTN_W, BTN_H) : normal;

        JLabel btn = new JLabel(normal);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            private boolean inside = false;

            @Override public void mouseEntered(MouseEvent e) { inside = true; }
            @Override public void mouseExited(MouseEvent e)  { inside = false; btn.setIcon(normal); }

            @Override public void mousePressed(MouseEvent e) { btn.setIcon(pressed); }

            @Override public void mouseReleased(MouseEvent e) {
                btn.setIcon(normal);
                if (inside && SwingUtilities.isLeftMouseButton(e)) action.run();
            }
        });

        return btn;
    }

    private static ImageIcon loadScaled(String file, int w, int h) {
        URL url = Mensaje.class.getResource(ASSETS + file);
        if (url == null) throw new RuntimeException("No se encontró: " + ASSETS + file);
        Image img = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_FAST);
        return new ImageIcon(img);
    }

    private static String toHtmlCentered(String msg) {
        if (msg == null) msg = "";
        msg = msg.replace("\n", "<br>");
        return "<html><div style='text-align:center;'>" + msg + "</div></html>";
    }
}
    
