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
import javax.swing.border.LineBorder;

public class ColocarBarcosWindow extends JFrame {

    private static final String ASSETS = "/BattleShip/assets/";
    private static final String BG = "fondo12.png";          
    private static final String BTN_COLOCAR = "colocarBTN.png";
    private static final String BTN_COLOCAR_PRESS = "colocarPRESS_BTN.png";
    private static final String BTN_QUITAR  = "quitarBTN.png";
    private static final String BTN_QUITAR_PRESS  = "quitarPRESS_BTN.png";
    private static final String BTN_EXIT    = "exitBTN.png";
    private static final String BTN_EXIT_PRESS    = "exitPRESS_BTN.png";
    private static final String BTN_LISTO   = "listoBTN.png";
    private static final String BTN_LISTO_PRESS   = "listoPRESS_BTN.png";
    private static final String INDEX_PLAYER1   = "switch_jugador1.png";
    private static final String INDEX_PLAYER2   = "switch_jugador2.png";

    private static final int W = 1100;
    private static final int H = 620;

    private static final int CELL = 60;
    private static final int N = Tablero.SIZE;

    private final PlayerManager manager;
    private final Player player1;
    private final Player player2;
    
    private Player actual;
    private Tablero tablero;
    
    private boolean turnoPlayer1;

    private final JLabel[][] grid = new JLabel[N][N];

    private JTextField txtCodigo;
    private JTextField txtFila;
    private JTextField txtColumna;
    private JComboBox<Direccion> cmbDireccion;
    private JTextField txtRestantes;
    private JLabel index;

    public ColocarBarcosWindow(PlayerManager manager, Player player1, Player player2) {
        this.manager = manager;
        this.player1 = player1;
        this.player2 = player2;
        
        this.turnoPlayer1 = true;
        this.actual = player1;
        this.tablero = player1.getTablero();

        configureWindow();
        buildUI();
        refreshBoard();
        refreshRestantes();
    }

    private void configureWindow() {
        setTitle("Battleship - Colocar Barcos");
        setSize(W, H);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void buildUI() {
        JLabel bg = new JLabel(loadScaledIcon(BG, W, H));
        bg.setLayout(null);
        setContentPane(bg);

        int startX = 70;
        int startY = 57;

        for (int c = 0; c < N; c++) {
            JLabel top = new JLabel(String.valueOf(c + 1), SwingConstants.CENTER);
            top.setForeground(Color.WHITE);
            top.setFont(new Font("Minecraft", Font.BOLD, 18));
            top.setBounds(startX + c * CELL, 35, CELL, 20);
            bg.add(top);
        }

        for (int r = 0; r < N; r++) {
            JLabel left = new JLabel(String.valueOf(r + 1), SwingConstants.CENTER);
            left.setForeground(Color.WHITE);
            left.setFont(new Font("Minecraft", Font.BOLD, 18));
            left.setBounds(25, startY + r * CELL, 45, CELL);
            bg.add(left);
        }

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                JLabel cell = new JLabel();
                cell.setBounds(startX + c * CELL, startY + r * CELL, CELL, CELL);
                cell.setBackground(Color.GRAY);
                cell.setHorizontalAlignment(SwingConstants.CENTER);
                cell.setVerticalAlignment(SwingConstants.CENTER);
                // ❌ sin interacción
                grid[r][c] = cell;
                bg.add(cell);
            }
        }

        int px = 600;
        int py = 110;

        index = new JLabel(loadScaledIcon(INDEX_PLAYER1, 290, 180));
        index.setBounds(px+70, py-60, 290, 50);
        bg.add(index);
        
        txtCodigo  = makeField(bg, px + 180, py + 0,   220, 40);
        txtCodigo.setText("DT/SM/AZ/PA");
        txtFila    = makeField(bg, px + 180, py + 55,  220, 40);
        txtFila.setText("1-8");
        txtColumna = makeField(bg, px + 180, py + 110, 220, 40);
        txtColumna.setText("1-8");

        cmbDireccion = new JComboBox<>(Direccion.values());
        cmbDireccion.setBounds(px + 180, py + 167, 220, 40);
        cmbDireccion.setForeground(Color.WHITE);
        cmbDireccion.setBackground(new Color(70, 75, 80));
        cmbDireccion.setFont(new Font("Minecraft", Font.BOLD, 16));
        bg.add(cmbDireccion);

        txtRestantes = makeField(bg, px + 180, py + 220, 220, 40);
        txtRestantes.setEditable(false);

        JLabel btnQuitar = createImageButton(BTN_QUITAR, BTN_QUITAR_PRESS, 140, 70, this::onQuitar);
        btnQuitar.setBounds(px + 50, py + 275, 140, 70);
        bg.add(btnQuitar);

        JLabel btnColocar = createImageButton(BTN_COLOCAR, BTN_COLOCAR_PRESS, 140, 70, this::onColocar);
        btnColocar.setBounds(px + 240, py + 280, 140, 60);
        bg.add(btnColocar);

        JLabel btnExit = createImageButton(BTN_EXIT, BTN_EXIT_PRESS, 180, 80, this::onExit);
        btnExit.setBounds(px + 12, 465, 179, 80);
        bg.add(btnExit);

        JLabel btnListo = createImageButton(BTN_LISTO, BTN_LISTO_PRESS, 168, 75, this::onListo);
        btnListo.setBounds(px + 250, 470, 168, 71);
        bg.add(btnListo);
    }

    private void onColocar() {
        String codigo = safeUpper(txtCodigo.getText());
        Integer fila = parse1to8(txtFila.getText());
        Integer col  = parse1to8(txtColumna.getText());
        Direccion dir = (Direccion) cmbDireccion.getSelectedItem();

        if (codigo.isBlank() || fila == null || col == null || dir == null) {
            Mensaje.showOk(this, "fondo5.png", "Coordenadas invalidas, solo numeros (1-8) y codigos {DT, SM, AZ, PA}!","okBTN.png", "okPRESS_BTN.png");
            return;
        }

        Barco tipo = Barco.fromCodigo(codigo);
        int repetidos = tablero.contarPorTipo(tipo);

        if (player1.getDifficulty() == Player.Difficulty.EASY) {
            if (tipo == Barco.DESTRUCTOR) {
                if (repetidos >= 2) {
                    Mensaje.showOk(this, "fondo5.png", "Solo se permiten 2 Destructores(DT)","okBTN.png", "okPRESS_BTN.png");
                    return;
                }
            } else {
                if (repetidos >= 1) {
                    Mensaje.showOk(this, "fondo5.png", "No puede poner mas de un " + tipo.getNombre()+ ".","okBTN.png", "okPRESS_BTN.png");
                    return;
                }
            }
        } else {
            if (repetidos >= 1) {
                Mensaje.showOk(this, "fondo5.png", "No puede poner mas de un " + tipo.getNombre() + ".","okBTN.png", "okPRESS_BTN.png");
                return;
            }
        }
        if (tipo == null) {
            Mensaje.showOk(this, "fondo5.png", "Codigo invalido. Utiliza: {DY, SM, AZ, PA}. ","okBTN.png", "okPRESS_BTN.png");
            return;
        }

        int max = maxBarcosPorDificultad(player1.getDifficulty());
        if (tablero.barcosRestantes() >= max) {
            Mensaje.showOk(this, "fondo5.png", "Maximo de barcos: " + max,"okBTN.png", "okPRESS_BTN.png");
            return;
        }

        boolean ok = tablero.colocarBarco(fila - 1, col - 1, dir, tipo);
        if (!ok) {
            Mensaje.showOk(this, "fondo5.png", "Coordenadas ocupadas o fuera de los limites.","okBTN.png", "okPRESS_BTN.png");
            return;
        }
      
        refreshBoard();
        refreshRestantes();
        refreshFields();
    }

    private void onQuitar() {
        Integer fila = parse1to8(txtFila.getText());
        Integer col  = parse1to8(txtColumna.getText());

        if (fila == null || col == null) {
            Mensaje.showOk(this, "fondo5.png", "Favor colocar las coordenadas!","okBTN.png", "okPRESS_BTN.png");
            return;
        }

        boolean ok = tablero.quitarBarcoEn(fila - 1, col - 1);
        if (!ok) {
            Mensaje.showOk(this, "fondo5.png", "No hay barco en esas coordenadas.","okBTN.png", "okPRESS_BTN.png");
            return;
        }

        refreshBoard();
        refreshRestantes();
    }

    private void onListo() {
        int max = maxBarcosPorDificultad(player1.getDifficulty());
        
        if(tablero.barcosRestantes() < max){
            Mensaje.showOk(this, "fondo5.png", "Coloca todos tus barcos 7_7.","okBTN.png", "okPRESS_BTN.png");
            return;
        }
          
        if(turnoPlayer1){
            Mensaje.showConfirmWithAction(this, "fondo3.png", "Pasar al siguiente jugador? ","listoBTN.png", "listoPRESS_BTN.png", "cancelBTN.png", "cancelPRESS_BTN.png",() -> {
                turnoPlayer1 = false;
                actual = player2;
                tablero = player2.getTablero();       
        
                index.setIcon(loadScaledIcon(INDEX_PLAYER2, 290, 180));
                refreshBoard();
                refreshRestantes();
                refreshFields();
            });
            return;
        }
        Mensaje.showConfirmWithAction(this, "fondo3.png", "Iniciar BATTLESHIP? ","listoBTN.png", "listoPRESS_BTN.png", "cancelBTN.png", "cancelPRESS_BTN.png",() -> {
            dispose();
            new PartidaBattleshipWindow(manager, player1, player2).setVisible(true);
        });
    }

    private void onExit() {
        Mensaje.showConfirmWithAction(this, "fondo3.png", "Deseas cancelar la partida contra " + player2.getUsername() + "?","exitBTN.png", "exitPRESS_BTN.png", "cancelBTN.png", "cancelPRESS_BTN.png",() -> {
            player1.getTablero().reset();
            player2.getTablero().reset();
            dispose();
            new MenuPrincipalWindow(manager, player1).setVisible(true);
        });
    }

    private void refreshBoard() {
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                Instance inst = tablero.getOcupacion(r, c);
                if (inst == null) {
                    grid[r][c].setForeground(Color.WHITE);
                    grid[r][c].setBackground(new Color(35, 35, 40));
                    grid[r][c].setBorder(new LineBorder(new Color(80, 80, 90), 2));
                    grid[r][c].setFont(new Font("Minecraft", Font.BOLD, 14));
                    grid[r][c].setText("~");
                } else {
                    grid[r][c].setIcon(null);
                    grid[r][c].setForeground(Color.WHITE);
                    grid[r][c].setBackground(new Color(35, 35, 40));
                    grid[r][c].setBorder(new LineBorder(new Color(80, 80, 90), 2));
                    grid[r][c].setFont(new Font("Minecraft", Font.BOLD, 14));
                    grid[r][c].setText(inst.getTipo().getCodigo());
                }
            }
        }
        repaint();
    }

    private void refreshRestantes() {
        int max = maxBarcosPorDificultad(player1.getDifficulty());
        int colocados = tablero.barcosRestantes();
        int restantes = Math.max(0, max - colocados);
        txtRestantes.setText(String.valueOf(restantes));
    }

    private int maxBarcosPorDificultad(Player.Difficulty diff) {
        return switch (diff) {
            case EASY   -> 5;
            case NORMAL -> 4;
            case EXPERT -> 2;
            case GENIUS -> 1;
        };
    }
    
    private void refreshFields(){
        txtCodigo.setText("DT/SM/AZ/PA");
        txtFila.setText("1-8");
        txtColumna.setText("1-8");
        cmbDireccion.setSelectedItem(Direccion.NORTE);
    }
    
    private JTextField makeField(JComponent parent, int x, int y, int w, int h) {
        JTextField t = new JTextField();
        t.setBounds(x, y, w, h);
        t.setHorizontalAlignment(SwingConstants.CENTER);
        t.setFont(new Font("Minecraft", Font.BOLD, 18));
        t.setBackground(new Color(70, 75, 80));
        t.setForeground(Color.WHITE);
        t.setCaretColor(Color.WHITE);
        t.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        parent.add(t);
        return t;
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

    private ImageIcon loadScaledIcon(String file, int w, int h) {
        URL url = getClass().getResource(ASSETS + file);
        if (url == null) throw new RuntimeException("No se encontró: " + ASSETS + file);
        Image img = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_FAST);
        return new ImageIcon(img);
    }

    private ImageIcon tryIcon(String file, int w, int h) {
        if (file == null) return null;
        URL url = getClass().getResource(ASSETS + file);
        if (url == null) return null;
        Image img = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_FAST);
        return new ImageIcon(img);
    }

    private String safeUpper(String s) {
        return s == null ? "" : s.trim().toUpperCase();
    }

    private Integer parse1to8(String s) {
        try {
            int v = Integer.parseInt(s.trim());
            return (v >= 1 && v <= 8) ? v : null;
        } catch (Exception e) {
            return null;
        }
    }
}
