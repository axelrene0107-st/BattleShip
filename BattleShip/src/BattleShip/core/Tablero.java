/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BattleShip.core;

/**
 *
 * @author axelr
 */
import java.util.*;

public class Tablero {
    public static final int SIZE = 8;

    private final Instance[][] ocupacion;
    private final boolean[][] disparado;
    private final List<Instance> barcos;

    public Tablero() {
        ocupacion = new Instance[SIZE][SIZE];
        disparado = new boolean[SIZE][SIZE];
        barcos = new ArrayList<>();
    }

    //Metodo que devuelve false si fila o columna >8 o <1
    public static boolean dentro(int fila, int columna) {
        return fila >= 0 && fila < SIZE && columna >= 0 && columna < SIZE;
    }

    public Instance getOcupacion(int fila, int columna) {
        return dentro(fila, columna) ? ocupacion[fila][columna] : null;
    }

    //Switch para los valores de las direccion 
    static int dr(Direccion dir) {
        return switch (dir) {
            case NORTE -> -1;
            case SUR   ->  1;
            case ESTE  ->  0;
            case OESTE ->  0;
        };
    }

    
    static int dc(Direccion dir) {
        return switch (dir) {
            case NORTE ->  0;
            case SUR   ->  0;
            case ESTE  ->  1;
            case OESTE -> -1;
        };
    }

    //Para verificar si esta fuera de los limites de la matriz
    public boolean puedeColocar(int fila, int columna, Direccion dir, Barco tipo) {
        int longitud = tipo.getTamanio();
        int dRow = dr(dir);
        int dCol = dc(dir);

        for (int i = 0; i < longitud; i++) {
            int row = fila + i * dRow;
            int col = columna + i * dCol;

            if (!dentro(row, col)) 
                return false;
            if (ocupacion[row][col] != null) 
                return false;
        }
        return true;
    }

    public boolean colocarBarco(int fila, int columna, Direccion dir, Barco tipo) {
        if (!puedeColocar(fila, columna, dir, tipo)) 
            return false;

        Instance barco = new Instance(tipo);
        int longitud = tipo.getTamanio();
        int dRow = dr(dir);
        int dCol = dc(dir);

        for (int i = 0; i < longitud; i++) {
            int row = fila + i * dRow;
            int col = columna + i * dCol;
            ocupacion[row][col] = barco;
        }

        barcos.add(barco);
        return true;
    }

    public boolean quitarBarcoEn(int fila, int columna) {
        if (!dentro(fila, columna)) 
            return false;

        Instance target = ocupacion[fila][columna];
        if (target == null) 
            return false;

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (ocupacion[r][c] == target) {
                    ocupacion[r][c] = null;
                }
            }
        }

        barcos.remove(target);
        return true;
    }
    
    public int contarPorTipo(Barco tipo){
        int conteo = 0;
        for(Instance b : barcos){
            if(b.getTipo() == tipo)
                conteo++;
        }
        
        return conteo;
    }

    //Recursividad en el reseteo de los tableros
    public void reset() {
        resetRecursivo(0);
        barcos.clear();
    }

    private void resetRecursivo(int i) {
        if (i >= SIZE * SIZE) return;

        int r = i / SIZE;
        int c = i % SIZE;

        ocupacion[r][c] = null;
        disparado[r][c] = false;

        resetRecursivo(i + 1);
    }

    
    public ResultadoDisparo disparar(int fila, int columna) {
        if (!dentro(fila, columna)) 
            throw new IllegalArgumentException("Coordenadas fuera de alcance!");//Importante en battleship al momento de intentar disparar.
        
        Instance b = ocupacion[fila][columna];
        if (b == null) {
            disparado[fila][columna] = true;
            return ResultadoDisparo.AGUA;
        }
        //Operador terniario
        if (disparado[fila][columna]) {
            return b.estaHundido() ? ResultadoDisparo.HUNDIDO : ResultadoDisparo.IMPACTO;
        }

        b.recibirImpacto();
        disparado[fila][columna] = true;
        return b.estaHundido() ? ResultadoDisparo.HUNDIDO : ResultadoDisparo.IMPACTO; //Operador terniario
    }

    
    public boolean fueDisparado(int fila, int columna) {
        return dentro(fila, columna) && disparado[fila][columna];
    }

    
    public boolean fueImpacto(int fila, int columna) {
        return dentro(fila, columna) && disparado[fila][columna] && ocupacion[fila][columna] != null;
    }

    
    public int barcosRestantes() {
        int cantidad = 0;
        for (Instance b : barcos) { 
            if (!b.estaHundido()) 
                cantidad++;
        }
        return cantidad;
    }

    
    public void limpiarOcupacion(){
        for(int r = 0; r<SIZE; r++){
            for(int c = 0; c < SIZE; c++){
                ocupacion[r][c]=null;
            }
        }
    }
    
    
    public List<Instance> getBarcosInstancias(){
        return new ArrayList<>(barcos);
    }
    
    
    public void reubicarBarcos() {
        List<Instance> hundidos = new ArrayList<>();
        List<Instance> vivos = new ArrayList<>();

        for (Instance b : barcos) { // foreach
            if(b.estaHundido()) hundidos.add(b);
            else vivos.add(b);
        }

        List<ShipSnapshot> snapshots = new ArrayList<>();
        for (Instance b : vivos) {
            List<Cell> cells = obtenerCeldasDe(b);
            for (Cell cell : cells) {
                cell.shot = disparado[cell.r][cell.c];
            }
            snapshots.add(new ShipSnapshot(b, cells));
        }

        for (ShipSnapshot snap : snapshots) {
            for (Cell cell : snap.celdas) {
                ocupacion[cell.r][cell.c] = null;
                disparado[cell.r][cell.c] = false; 
            }
        }

        Random rng = new Random();

        for (ShipSnapshot snap : snapshots) {
            Instance barco = snap.barco;
            int len = barco.getTipo().getTamanio();

            boolean colocado = false;
            for (int intento = 0; intento < 500 && !colocado; intento++) {
                Direccion dir = Direccion.values()[rng.nextInt(Direccion.values().length)];
                int fila = rng.nextInt(SIZE);
                int col = rng.nextInt(SIZE);

                if (!puedeColocar(fila, col, dir, barco.getTipo())) continue;

                List<Cell> nuevas = generarCeldas(fila, col, dir, len);

                List<Cell> viejasOrdenadas = new ArrayList<>(snap.celdas);
                List<Cell> nuevasOrdenadas = new ArrayList<>(nuevas);

                ordenarCeldas(viejasOrdenadas);
                ordenarCeldas(nuevasOrdenadas);
                
                for (int i = 0; i < len; i++) {
                    Cell nv = nuevasOrdenadas.get(i);
                    Cell ov = viejasOrdenadas.get(i);

                    ocupacion[nv.r][nv.c] = barco;
                    disparado[nv.r][nv.c] = ov.shot;
                }

                colocado = true;
            }

            if (!colocado) {
                for (Cell cell : snap.celdas) {
                    ocupacion[cell.r][cell.c] = barco;
                    disparado[cell.r][cell.c] = cell.shot;
                }
            }
        }
    }
    
    public boolean todosHundidos() {
        return barcosRestantes() == 0;
    }
    
    //Clase interna celda
    private static class Cell {
        int r, c;
        boolean shot;
        Cell(int r, int c) { 
            this.r = r; 
            this.c = c; 
        }
    }

    private static class ShipSnapshot {
        Instance barco;
        List<Cell> celdas;
        ShipSnapshot(Instance barco, List<Cell> celdas) {
            this.barco = barco;
            this.celdas = celdas;
        }
    }

    private List<Cell> obtenerCeldasDe(Instance target) {
        List<Cell> out = new ArrayList<>();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (ocupacion[r][c] == target) out.add(new Cell(r, c));
            }
        }
        return out;
    }

    private List<Cell> generarCeldas(int fila, int col, Direccion dir, int len) {
        int dRow = dr(dir);
        int dCol = dc(dir);

        List<Cell> out = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            out.add(new Cell(fila + i * dRow, col + i * dCol));
        }
        return out;
    }

    private void ordenarCeldas(List<Cell> cells) {
        Collections.sort(cells, Comparator.comparingInt((Cell a) -> a.r).thenComparingInt(a -> a.c));
    }
       

}
