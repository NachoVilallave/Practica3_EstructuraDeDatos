import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

public class JuegoDomino {
    private final Stack<Ficha> pozo;
    private final Deque<Ficha> cadena;
    private final Jugador[] jugadores;
    private final int NJUGADORES;
    private final int NFICHAS;
    private int turno;

    // a) Constructor
    public JuegoDomino(String nombresJugadores[], int nFichas) {
        if (nombresJugadores == null || nombresJugadores.length < 2 || nombresJugadores.length > 4) {
            throw new IllegalArgumentException();
        }
        this.NJUGADORES = nombresJugadores.length;
        this.NFICHAS = nFichas;
        this.pozo = new Stack<>();
        this.cadena = new LinkedList<>();
        this.jugadores = new Jugador[NJUGADORES];

        for (int i = 0; i < NJUGADORES; i++) {
            this.jugadores[i] = new Jugador(nombresJugadores[i]);
        }
        this.turno = 0;
    }

    public JuegoDomino(String nombresJugadores[]) {
        this(nombresJugadores, 7);
    }

    public void prepararPartida() {
        crearYMezclarFichas();
        repartirFichasIniciales();
        turno = 0;
    }

    public void crearYMezclarFichas() {
        for (int i = 0; i <= 6; i++) {
            for (int j = i; j <= 6; j++) {
                pozo.add(new Ficha(i, j));
            }
        }
        Collections.shuffle(pozo);
    }

    // b) Repartir fichas iniciales
    private void repartirFichasIniciales() {
        for (int i = 0; i < NFICHAS; i++) {
            for (Jugador j : jugadores) {
                if (!pozo.isEmpty()) {
                    j.getFichas().add(pozo.pop());
                }
            }
        }
    }

    public void jugar() {
        System.out.println("--- ESTADO INICIAL ---");
        for (int j = 0; j < NJUGADORES; j++)
            System.out.println(jugadores[j]);

        System.out.println("Fichas en pozo (durmientes): " + pozo.size());
        System.out.println("----------------------\n");

        Ficha primeraFicha = jugadores[turno].getFichas().poll();
        cadena.add(primeraFicha);

        System.out.println("\n--- TURNO DE " + jugadores[turno].getNombre().toUpperCase() + " ---");
        System.out.println(jugadores[turno].getNombre() + " inicia la cadena con la ficha " + primeraFicha);
        mostrarCadena();

        this.turno = (this.turno + 1) % this.NJUGADORES;
        Jugador actual = this.jugadores[turno];
        int pasesConsecutivos = 0;

        boolean finJuego = false;
        while (!finJuego) {
            System.out.println("\n--- TURNO DE " + actual.getNombre().toUpperCase() + " ---");
            boolean colocoFicha = jugarTurno(actual);

            if (!actual.tieneFichas()) {
                System.out.println("\n¡" + actual.getNombre() + " se ha quedado sin fichas!");
                finJuego = true;
            } else {
                if (colocoFicha) {
                    pasesConsecutivos = 0;
                } else {
                    pasesConsecutivos++;
                    System.out.println(actual.getNombre() + " NO ha podido colocar ninguna ficha y pasa.");
                    if (pasesConsecutivos == NJUGADORES) {
                        System.out.println("\n¡Todos los jugadores han pasado consecutivamente! El juego está cerrado.");
                        finJuego = true;
                    }
                }
            }

            mostrarCadena();

            if (!finJuego) {
                this.turno = (this.turno + 1) % NJUGADORES;
                actual = this.jugadores[turno];
            }
        }
        determinarGanador();
    }

    public Jugador getJugadorActual() {
        return jugadores[turno];
    }

    // f) Jugar turno
    public boolean jugarTurno(Jugador jugador) {
        if (cadena.isEmpty()) {
            if (!jugador.getFichas().isEmpty()) {
                Ficha primera = jugador.getFichas().poll();
                cadena.add(primera);
                return true;
            }
            return false;
        }

        int valIzq = cadena.peekFirst().getLadoA();
        int valDer = cadena.peekLast().getLadoB();

        // 1. Buscar en la mano
        Ficha fichaAColocar = null;
        for (Ficha f : jugador.getFichas()) {
            if (f.getLadoA() == valIzq || f.getLadoB() == valIzq ||
                    f.getLadoA() == valDer || f.getLadoB() == valDer) {
                fichaAColocar = f;
                break;
            }
        }

        if (fichaAColocar != null) {
            jugador.getFichas().remove(fichaAColocar);
            intentarColocar(fichaAColocar);
            return true;
        }

        // 2. Si no tiene en la mano, robar del pozo
        while (!pozo.isEmpty()) {
            Ficha robada = pozo.pop();
            jugador.getFichas().add(robada); // Añadimos la ficha robada a la mano primero

            boolean encaja = (robada.getLadoA() == valIzq || robada.getLadoB() == valIzq ||
                    robada.getLadoA() == valDer || robada.getLadoB() == valDer);

            if (encaja) {
                jugador.getFichas().remove(robada); // Si encaja, la removemos de la mano para jugarla
                intentarColocar(robada);
                return true;
            }
        }

        return false;
    }

    // e) Intentar colocar la ficha
    public boolean intentarColocar(Ficha f) {
        if (f == null) return false;
        if (cadena.isEmpty()) {
            cadena.add(f);
            return true;
        }

        int valIzq = cadena.peekFirst().getLadoA();
        int valDer = cadena.peekLast().getLadoB();

        boolean encajaIzq = (f.getLadoA() == valIzq || f.getLadoB() == valIzq);
        boolean encajaDer = (f.getLadoA() == valDer || f.getLadoB() == valDer);

        // Intentar encajar por la izquierda primero de forma natural
        if (encajaIzq) {
            colocarEnIzquierda(f, valIzq);
            return true;
        } else if (encajaDer) {
            colocarEnDerecha(f, valDer);
            return true;
        }

        return false;
    }

    // c) Colocar en la izquierda
    public void colocarEnIzquierda(Ficha f, int valorExtremo) {
        if (f.getLadoB() != valorExtremo) {
            f = f.invertir();
        }
        cadena.addFirst(f);
    }

    // d) Colocar en la derecha
    public void colocarEnDerecha(Ficha f, int valorExtremo) {
        if (f.getLadoA() != valorExtremo) {
            f = f.invertir();
        }
        cadena.addLast(f);
    }

    public void mostrarCadena() {
        StringBuilder sb = new StringBuilder("CADENA ACTUAL: ");
        for (Ficha f : cadena) {
            sb.append(f.toString()).append(" ");
        }
        System.out.println(sb.toString());
    }

    // g) Determinar ganador
    public Jugador determinarGanador() {
        Jugador ganador = null;
        int minPuntos = Integer.MAX_VALUE;
        int minFichas = Integer.MAX_VALUE;
        boolean empate = false;

        for (Jugador j : jugadores) {
            int puntos = 0;
            for (Ficha f : j.getFichas()) {
                puntos += f.getLadoA() + f.getLadoB();
            }
            int numFichas = j.getFichas().size();

            System.out.println("Jugador: " + j.getNombre() + " - Puntos: " + puntos + " - Fichas: " + numFichas);

            if (puntos < minPuntos) {
                minPuntos = puntos;
                minFichas = numFichas;
                ganador = j;
                empate = false;
            } else if (puntos == minPuntos) {
                if (numFichas < minFichas) {
                    minFichas = numFichas;
                    ganador = j;
                    empate = false;
                } else if (numFichas == minFichas) {
                    empate = true;
                }
            }
        }

        if (empate) {
            System.out.println("Empate total.");
            return null;
        } else if (ganador != null) {
            System.out.println("El ganador es: " + ganador.getNombre());
        }

        return ganador;
    }

    public Stack<Ficha> getPozo() {
        return this.pozo;
    }

    public Jugador[] getJugadores() {
        return this.jugadores;
    }

    public int fichasEnPozo() {
        return this.pozo.size();
    }

    public Object[] getCadena() {
        return this.cadena.toArray();
    }
}