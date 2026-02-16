/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BattleShip.UI;

/**
 *
 * @author axelr
 */
import BattleShip.core.*;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;


public class StatsWindow extends JFrame{
    private static final String ASSETS = "/BattleShip/assets/";
    private static final String BG = "fondo9.png"; 
    private static final String BTN_BACK = "backBTN.png";
    private static final String BTN_BACK_PRESS = "backPRESS_BTN.png";
    private static final String BTN_REPORTS = "reportsBTN.png";
    private static final String BTN_REPORTS_PRESS = "reportsPRESS_BTN.png";
    private static final String BTN_RANKING = "rankingBTN.png";
    private static final String BTN_RANKING_PRESS = "rankingPRESS_BTN.png";

    private static final int W = 1100;
    private static final int H = 620;

    private final PlayerManager manager;
    private final Player current;

    private JPanel contentPanel;

    public StatsWindow(PlayerManager manager, Player current) {
        this.manager = manager;
        this.current = current;

        configureWindow();
        buildUI();
        showReports(); 
    }

    private void configureWindow() {
        setTitle("Battleship - Stats");
        setSize(W, H);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void buildUI() {
        JLabel bg = new JLabel(loadScaledIcon(BG, W, H));
        bg.setLayout(null);
        setContentPane(bg);

        int topY = 75;
        int btnW = 180;
        int btnH = 100;

        int xBack = 230;
        int xReports = 450;
        int xRanking = 670;

        JLabel btnBack = createImageButton(BTN_BACK, BTN_BACK_PRESS, btnW, btnH, this::onBack);
        btnBack.setBounds(xBack, topY, btnW, btnH);
        bg.add(btnBack);

        JLabel btnReports = createImageButton(BTN_REPORTS, BTN_REPORTS_PRESS, btnW, btnH, this::showReports);
        btnReports.setBounds(xReports, topY, btnW, btnH);
        bg.add(btnReports);

        JLabel btnRanking = createImageButton(BTN_RANKING, BTN_RANKING_PRESS, btnW, btnH, this::showRanking);
        btnRanking.setBounds(xRanking, topY, btnW, btnH);
        bg.add(btnRanking);

        contentPanel = new JPanel(null);
        contentPanel.setOpaque(false);
        contentPanel.setBounds(220, 220, 640, 290);
        bg.add(contentPanel);
    }

    private void showReports() {
        contentPanel.removeAll();

        JPanel inner = new JPanel(new BorderLayout());
        inner.setOpaque(false);
        inner.setBackground(new Color(35, 40, 45));
        inner.setBounds(0, 0, contentPanel.getWidth(), contentPanel.getHeight());
        inner.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        contentPanel.add(inner);

        JTextArea txt = new JTextArea();
        txt.setEditable(false);
        txt.setLineWrap(true);
        txt.setFont(new Font("Minecraft", Font.PLAIN, 18));
        txt.setForeground(Color.WHITE);
        txt.setBackground(new Color(50, 55, 60));
        txt.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 90), 2));

        ArrayList<Partida> ultimos = current.getUltimos10Juegos();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append("  ").append(i + 1).append("- ");
            if (i < ultimos.size()) {
                sb.append(ultimos.get(i).resumen());
            }
            sb.append("\n\n");
        }

        txt.setText(sb.toString());

        JScrollPane sp = new JScrollPane(txt);
        sp.setBorder(null);
        inner.add(sp, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showRanking() {
        contentPanel.removeAll();

        JPanel inner = new JPanel(new BorderLayout());
        inner.setOpaque(false);
        inner.setBackground(new Color(35, 40, 45));
        inner.setBounds(0, 0, contentPanel.getWidth(), contentPanel.getHeight());
        inner.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        contentPanel.add(inner);

        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);

        java.util.ArrayList<Player> ranking = manager.getRanking(); 

        int puesto = 1;
        for (Player p : ranking) {
            list.add(buildRankingRow(puesto, p));
            list.add(Box.createVerticalStrut(8));
            puesto++;
        }

        JScrollPane sp = new JScrollPane(list);
        sp.setBorder(null);
        sp.getViewport().setOpaque(false);
        sp.setOpaque(false);

        inner.add(sp, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel buildRankingRow(int puesto, Player p) {
        JPanel row = new JPanel(null);
        row.setPreferredSize(new Dimension(600, 70));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        row.setOpaque(true);
        row.setBackground(new Color(50, 55, 60));
        row.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 90), 2));

        JLabel lblPuesto = new JLabel("#" + puesto, SwingConstants.CENTER);
        lblPuesto.setForeground(Color.WHITE);
        lblPuesto.setFont(new Font("Minecraft", Font.BOLD, 18));
        lblPuesto.setBounds(10, 10, 70, 50);
        row.add(lblPuesto);

        
        JLabel pic = new JLabel();
        pic.setBounds(90, 10, 50, 50);
        pic.setIcon(loadProfileIcon(p.getImagePath(), 50, 50));
        row.add(pic);

        JLabel user = new JLabel(p.getUsername());
        user.setForeground(Color.WHITE);
        user.setFont(new Font("Minecraft", Font.BOLD, 18));
        user.setBounds(155, 10, 280, 50);
        row.add(user);

        JLabel pts = new JLabel(p.getPuntos() + " pts", SwingConstants.RIGHT);
        pts.setForeground(Color.WHITE);
        pts.setFont(new Font("Minecraft", Font.BOLD, 18));
        pts.setBounds(370, 10, 200, 50);
        row.add(pts);

        return row;
    }

    @SuppressWarnings("unused")
    private String formatPartida(Partida p) {
        return p.toString(); 
    }

    private void onBack() {
        dispose();
        new MenuPrincipalWindow(manager, current).setVisible(true);
    }

    private JLabel createImageButton(String imgNormal, String imgPressed, int w, int h, Runnable action) {
        ImageIcon normal = loadScaledIcon(imgNormal, w, h);
        ImageIcon pressed = loadScaledIcon(imgPressed, w, h);

        JLabel btn = new JLabel(normal);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            private boolean inside = false;

            @Override public void mouseEntered(java.awt.event.MouseEvent e) { inside = true; }
            @Override public void mouseExited (java.awt.event.MouseEvent e) { inside = false; btn.setIcon(normal); }
            @Override public void mousePressed(java.awt.event.MouseEvent e) { btn.setIcon(pressed); }

            @Override public void mouseReleased(java.awt.event.MouseEvent e) {
                btn.setIcon(normal);
                if (inside && SwingUtilities.isLeftMouseButton(e) && action != null) action.run();
            }
        });

        return btn;
    }

    private ImageIcon loadScaledIcon(String file, int w, int h) {
        URL url = getClass().getResource(ASSETS + file);
        if (url == null) throw new RuntimeException("No se encontró: " + ASSETS + file);
        Image img = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_FAST);
        return new ImageIcon(img);
    }

    private ImageIcon loadProfileIcon(String path, int w, int h) {
        if (path == null || path.isBlank())     
            return null;

        Image img = null;

        try {
            if (path.startsWith("/")) {
                URL url = getClass().getResource(path);
                if (url != null) 
                    img = new ImageIcon(url).getImage();
            } else {
                img = new ImageIcon(path).getImage();
            }
        } catch (Exception ignored) {}

        if (img == null) 
            return null;
        return new ImageIcon(img.getScaledInstance(w, h, Image.SCALE_FAST));
    }
}
