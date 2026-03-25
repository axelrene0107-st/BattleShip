/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BattleShip.UI;

/**
 *
 * @author axelr
 */
import BattleShip.Battleship;
import BattleShip.core.*;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import javax.swing.border.LineBorder;

public class PartidaBattleshipWindow extends JFrame{
    private static final String ASSETS = "/BattleShip/assets/";
    private static final String BG = "fondo13.png";
    private static final String BTN_SHOT = "shotBTN.png";
    private static final String BTN_SHOT_PRESS = "shotPRESS_BTN.png";
    private static final String BTN_RETIRAR = "retirarBTN.png";
    private static final String BTN_RETIRAR_PRESS = "retirarPRESS_BTN.png";
    private static final String BTN_PASAR = "pasarBTN.png";
    private static final String BTN_PASAR_PRESS = "pasarPRESS_BTN.png";
    private static final String INDEX_PLAYER1 = "switch_jugador1.png";
    private static final String INDEX_PLAYER2   = "switch_jugador2.png";
    
    private static final int W= 1100;
    private static final int H= 620;   
    private static final int CELL= 60;
    private static final int N= Tablero.SIZE;
    
    private final Battleship GAME;
    private final PlayerManager manager;
    private final Player player1;
    private final Player player2;
    
    private Tablero tableroDefensor;
    
    private final JLabel[][] matriz = new JLabel[N][N];
    private final boolean[][] fallosTurno = new boolean[N][N];
    private Integer lastMissRow = null;
    private Integer lastMissCol = null;
    
    private JTextArea txtLog;
    private JTextField txtFila;
    private JTextField txtColumna;
    private JLabel index;
    private JLabel btnShot;
    private JLabel btnRetirar;
    private JLabel btnPasar;
    
    public PartidaBattleshipWindow(PlayerManager manager, Player player1, Player player2){
        this.manager = manager;
        this.player1 = player1;
        this.player2 = player2;
        
        this.GAME = new Battleship(player1, player2, player1.getTablero(), player2.getTablero()); 
        GAME.iniciarPartida();           
        this.tableroDefensor = (GAME.getTurnoActual() == Battleship.Turno.JUGADOR1)? GAME.getTableroJugador2() : GAME.getTableroJugador1();
        configureWindow();
        buildUI();
        refreshTurnUI();
        refreshBoard();
        appendLog("INICIA LA PARTIDA (" + player1.getUsername()+ " VS " +player2.getUsername()+")\nTurno: "+ GAME.getJugadorActual().getUsername());
    }
    
    private void configureWindow() {
        setTitle("Battleship - Partida");
        setSize(W, H);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    private void buildUI(){
        JLabel bg = new JLabel(loadScaledIcon(BG, W, H));
        bg.setLayout(null);
        setContentPane(bg);
        
        int startX = 70;
        int startY = 57;
        
        for(int c = 0; c<N; c++){
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
                cell.setHorizontalAlignment(SwingConstants.CENTER);
                cell.setVerticalAlignment(SwingConstants.CENTER);

                cell.setForeground(Color.WHITE);
                cell.setBackground(new Color(35, 35, 40));
                cell.setOpaque(true);
                cell.setBorder(new LineBorder(new Color(80, 80, 90), 2));
                cell.setFont(new Font("Minecraft", Font.BOLD, 18));

                matriz[r][c] = cell;
                bg.add(cell);
            }
        }
        
        
        int posicionX= 600;
        int posicionY= 110;
        
        index = new JLabel(tryIcon(INDEX_PLAYER1, 290, 180));
        index.setBounds(posicionX + 70, posicionY - 60, 290, 50);
        bg.add(index);

        txtLog = new JTextArea();
        txtLog.setEditable(false);
        txtLog.setLineWrap(true);
        txtLog.setWrapStyleWord(true);
        txtLog.setFont(new Font("Minecraft", Font.PLAIN, 14));
        txtLog.setForeground(Color.WHITE);
        txtLog.setBackground(new Color(70, 75, 80));
        txtLog.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane sp = new JScrollPane(txtLog);
        sp.setBounds(posicionX + 27, posicionY - 20, 380, 170);
        sp.setBorder(BorderFactory.createEmptyBorder());
        bg.add(sp);

        txtFila = makeField(bg, posicionX + 180, posicionY + 165, 220, 45);
        txtColumna = makeField(bg, posicionX + 180, posicionY + 225, 220, 45);

        btnShot = createImageButton(BTN_SHOT, BTN_SHOT_PRESS, 140, 70, this::onShot);
        btnShot.setBounds(posicionX + 145, posicionY + 282, 140, 50);
        bg.add(btnShot);

        btnRetirar = createImageButton(BTN_RETIRAR, BTN_RETIRAR_PRESS, 170, 80, this::onRetirar);
        btnRetirar.setBounds(posicionX + 15, 465, 170, 80);
        bg.add(btnRetirar);

        btnPasar = createImageButton(BTN_PASAR, BTN_PASAR_PRESS, 170, 80, this::onPasar);
        btnPasar.setBounds(posicionX + 250, 465, 170, 80);
        bg.add(btnPasar);
        
    }
    
    private void onShot(){
        if(GAME.getEstado() != Battleship.Estado.EN_JUEGO)
            return;
        
        Integer fila = parse1to8(txtFila.getText());
        Integer columna = parse1to8(txtColumna.getText());
        
        if(fila == null || columna == null){
            Mensaje.showOk(this, "fondo5.png", "Ingresa las coordenadas de ataque (1-8).","okBTN.png", "okPRESS_BTN.png");
            return;
        }
        
        int row = fila - 1;
        int col = columna - 1;
        
        String elemento ="";
        if(tableroDefensor.getOcupacion(row, col)!=null)
            elemento = tableroDefensor.getOcupacion(row, col).getTipo().getNombre();
       
        
        Battleship.Disparo info;
        try {
            info = GAME.disparar(row, col);
        } catch (Exception ex) {
            Mensaje.showOk(this, "fondo5.png", ex.getMessage(),"okBTN.png", "okPRESS_BTN.png");//Obtiene el mensaje que escribi en battleship
            return;
        }
        
        if (info.resultado == ResultadoDisparo.REPETIDO) {
            Mensaje.showOk(this, "fondo5.png", "1 Disparo por turno, pasa al siguiente.","okBTN.png", "okPRESS_BTN.png");
            return;
        }
        
        if(info.resultado == ResultadoDisparo.AGUA){
            fallosTurno[row][col] = true;
        }
        
        if (info.resultado == ResultadoDisparo.AGUA) {
            lastMissRow = row;
            lastMissCol = col;
        } else {
            lastMissRow = null;
            lastMissCol = null;
        }
         
        String atacante = (info.turnoDisparo == Battleship.Turno.JUGADOR1) ? player1.getUsername() : player2.getUsername();//Ternario para conseguir el nombre del atacante
        
        switch (info.resultado) {
            case AGUA -> appendLog(atacante + " disparo (" + fila + "," + columna + "): AGUA");
            case IMPACTO -> appendLog(atacante + " disparo (" + fila + "," + columna + "): IMPACTO a un barco!");
            case HUNDIDO -> appendLog(atacante + " disparo (" + fila + "," + columna + "): HUNDIO el " + elemento + " de " + GAME.getOponenteActual().getUsername() + "!");
            default -> { /* no-op */ }
        }
        
        if (info.tableroRegenerado) {
            appendLog("* TABLERO REGENERADO (modo dinamico)");
        }

        tableroDefensor = (GAME.getTurnoActual() == Battleship.Turno.JUGADOR1) ? GAME.getTableroJugador2() : GAME.getTableroJugador1();//
        refreshBoard();
        
        if (info.findelJuego) {
            appendLog("FIN: Gano " + info.ganador.getUsername());
            Mensaje.showOk(this, "fondo5.png", "PARTIDA TERMINADA\nGANADOR: "+info.ganador.getUsername(),"okBTN.png", "okPRESS_BTN.png");
            dispose();
            new MenuPrincipalWindow(manager, player1).setVisible(true);
        }               
    }
    
    private void onPasar(){
       if (GAME.getEstado() != Battleship.Estado.EN_JUEGO) 
           return;

       boolean ok = GAME.pasarTurno();
       if (!ok) {
            Mensaje.showOk(this, "fondo5.png", "Dispara a una coordenada antes de pasar turno.","okBTN.png", "okPRESS_BTN.png");
            return;
       }
       
       txtFila.setText("");
       txtColumna.setText("");
       
       refreshTurnUI();
       
       tableroDefensor = (GAME.getTurnoActual() == Battleship.Turno.JUGADOR1) ? GAME.getTableroJugador2() : GAME.getTableroJugador1();
       
       for(int r = 0; r<N; r++){
           for(int c = 0; c<N; c++){
               fallosTurno[r][c] = false;
           }
       }
        
       lastMissRow = null;
       lastMissCol = null;
       refreshBoard();
       
       appendLog("----");
       appendLog("Turno: " + GAME.getJugadorActual().getUsername());
        
    }
    
    private void onRetirar(){
        Mensaje.showConfirmWithAction(this, "fondo3.png", "Seguro deseas retirarte? ","retirarBTN.png", "retirarPRESS_BTN.png", "cancelBTN.png", "cancelPRESS_BTN.png",() -> {
            String retirado = GAME.getJugadorActual().getUsername();
            String ganador = GAME.getOponenteActual().getUsername();       
            GAME.retirarJugadorActual();
        
            appendLog("! " + retirado + " se retiro. Gana " + ganador);
            Mensaje.showOk(this, "fondo5.png", "PARTIDA TERMINADA\n" + retirado + " se retiro. Ganador: " + ganador,"okBTN.png", "okPRESS_BTN.png");
        
            dispose();
            new MenuPrincipalWindow(manager, player1).setVisible(true); 
        });
    }
    
    private void appendLog(String msg) {
        txtLog.append(msg + "\n");
        txtLog.setCaretPosition(txtLog.getDocument().getLength());
    }
    
    private Integer parse1to8(String s) {
        try {
            int v = Integer.parseInt(s.trim());
            return (v >= 1 && v <= 8) ? v : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    private boolean isTutorial() {
        return player1.getMode() == Player.Mode.TUTORIAL;
    }
    
    private void refreshTurnUI(){
        if (GAME.getTurnoActual() == Battleship.Turno.JUGADOR1) {
            if (index != null) index.setIcon(tryIcon(INDEX_PLAYER1, 290, 180));
        } else {
            if (index != null) index.setIcon(tryIcon(INDEX_PLAYER2, 290, 180));
        }
    } 
    
    private void refreshBoard(){
        boolean tutorial = isTutorial();

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {

                boolean shot = tableroDefensor.fueDisparado(r, c);

                if (!shot) {
                    if (tutorial) {
                        Instance inst = tableroDefensor.getOcupacion(r, c);
                        if (inst != null) {
                            matriz[r][c].setText(inst.getTipo().getCodigo());
                        } else {
                            matriz[r][c].setText("~");
                        }
                    } else {
                        matriz[r][c].setText("~");
                    }
                    continue;
                }

                boolean hit = tableroDefensor.fueImpacto(r, c);

                if (!hit) {
                    boolean esUltimoFallo = (lastMissRow != null && lastMissCol != null
                        && lastMissRow == r && lastMissCol == c);

                    matriz[r][c].setText(esUltimoFallo ? "F" : "~");
                    continue;
                }

                Instance inst = tableroDefensor.getOcupacion(r, c);

                if (inst != null && inst.estaHundido()) {
                    matriz[r][c].setText(inst.getTipo().getCodigo());
                } else {
                    matriz[r][c].setText("X"); 
                }
            }
        }

        repaint();
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
        if (url == null) throw new RuntimeException("No se encontro: " + ASSETS + file);
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
}
