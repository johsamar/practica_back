package agents;

public class EstadoDelJuego {

    private int[][] tablero;
    private int jugador;

    public EstadoDelJuego() {
        this.tablero = new int[3][3];
        this.jugador = 1;
    }

    public int[][] getTablero() {
        return tablero;
    }

    public void setTablero(int[][] tablero) {
        this.tablero = tablero;
    }

    public int getJugador() {
        return jugador;
    }

    public void setJugador(int jugador) {
        this.jugador = jugador;
    }

    public boolean estaVacio(int fila, int columna) {
        return tablero[fila][columna] == 0;
    }

    public void hacerMovimiento(Movimiento movimiento) {
        tablero[movimiento.getFila()][movimiento.getColumna()] = jugador;
        jugador = (jugador == 1) ? 2 : 1;
    }

    public void revertirMovimiento(Movimiento movimiento) {
        tablero[movimiento.getFila()][movimiento.getColumna()] = 0;
        jugador = (jugador == 1) ? 2 : 1;
    }

    public int evaluar() {
        int sumaHorizontal = 0;
        int sumaVertical = 0;
        int sumaDiagonalPrincipal = 0;
        int sumaDiagonalSecundaria = 0;

        for (int i = 0; i < 3; i++) {
            sumaHorizontal += tablero[i][0] + tablero[i][1] + tablero[i][2];
            sumaVertical += tablero[0][i] + tablero[1][i] + tablero[2][i];
            sumaDiagonalPrincipal += tablero[0][0] + tablero[1][1] + tablero[2][2];
            sumaDiagonalSecundaria += tablero[0][2] + tablero[1][1] + tablero[2][0];
        }

        int valor = 0;

        if (sumaHorizontal == 3 * jugador) {
            return valor = 10;
        } else if (sumaHorizontal == -3 * jugador) {
            return valor = -10;
        } else if (sumaVertical == 3 * jugador) {
            return valor = 10;
        } else if (sumaVertical == -3 * jugador) {
            return valor = -10;
        } else if (sumaDiagonalPrincipal == 3 * jugador) {
            return valor = 10;
        } else if (sumaDiagonalPrincipal == -3 * jugador) {
            return valor = -10;
        } else if (sumaDiagonalSecundaria == 3 * jugador) {
            return valor = 10;
        } else if (sumaDiagonalSecundaria == -3 * jugador) {
            return valor = -10;
        }

        return valor;
    }

    public boolean esFinalDelJuego() {
        return (hayGanador() || estaLleno());
    }

    public boolean hayGanador() {
        return (sumaHorizontal == 3 * jugador || sumaVertical == 3 * jugador || sumaDiagonalPrincipal == 3 * jugador || sumaDiagonalSecundaria == 3 * jugador);
    }

    public boolean estaLleno() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] == 0) {
                    return false;
                }
            }
        }

        return true;
    }

    public List<Movimiento> obtenerMovimientosPosibles() {
        List<Movimiento> movimientos = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] == 0) {
                    movimientos.add(new Movimiento(i, j));
                }
            }
        }
