

import java.util.LinkedList;
import java.util.Queue;

public class Jugador {
    //parte privada
    private String nombre;
    private Queue<Ficha> fichas;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.fichas = new LinkedList<>();
    }

    public String getNombre() {
        return this.nombre;
    }
    public Queue<Ficha> getFichas() {
        return this.fichas;
    }

    public void recibirFicha(Ficha f) {
        this.fichas.add(f);
    }

    public Ficha buscarFicha(int valor1, int valor2) {
        int numFichas = this.fichas.size(); // Guardamos cuántas fichas tiene para no hacer un bucle infinito

        for (int i = 0; i < numFichas; i++) {
            // 1. Sacamos la ficha por el extremo izquierdo
            Ficha f = this.fichas.poll();

            // 2. Comprobamos si tiene al menos uno de los números pedidos
            if (f.getLadoA() == valor1 || f.getLadoA() == valor2 ||
                    f.getLadoB() == valor1 || f.getLadoB() == valor2) {

                // Si nos sirve, la devolvemos (como hicimos poll(), ya está fuera de la mano)
                return f;
            } else {
                // 3. Si no nos sirve, la volvemos a meter por el extremo derecho
                this.fichas.add(f);
            }
        }
        // Si ha dado la vuelta a todas las fichas y ninguna sirve, devuelve null
        return null;
    }


    public boolean tieneFichas() {
        return !this.fichas.isEmpty();
    }

    public int calcularPuntosRestantes() {
        int suma = 0;
        for(Ficha f : this.fichas){
            suma += f.getPuntosTotales();
        }
        return suma;
    }

    @Override
    public String toString() {
        return this.nombre + " - Fichas: " + this.fichas.toString();
    }
}
