

import java.util.LinkedList;
import java.util.Queue;

public class Jugador {
    //parte privada
    private String nombre;
    private Queue<Ficha> fichas;

    public Jugador(String nombre) {
        this.nombre = nombre;
        fichas = new LinkedList<>();
    }

    public String getNombre() {
        return this.nombre;
    }
    public Queue<Ficha> getFichas() {
       return fichas;
    }

    public void recibirFicha(Ficha f) {
        fichas.add(f);
    }

    public Ficha buscarFicha(int valor1, int valor2) {
        if (valor1 < 0 || valor1 > 6 || valor2 < 0 || valor2 > 6) {
            throw new IllegalArgumentException();
        }

        int n = fichas.size();
        for (int i = 0; i < n; i++) {
            Ficha ficha = fichas.poll();

            boolean tieneValorBuscado = (ficha.getLadoA() == valor1 || ficha.getLadoA() == valor2 ||
                    ficha.getLadoB() == valor1 || ficha.getLadoB() == valor2);

            if (tieneValorBuscado) {
                return ficha;
            } else {
                fichas.add(ficha);
            }
        }
        return null;
    }


    public boolean tieneFichas() {

        return !fichas.isEmpty();
    }

    public int calcularPuntosRestantes() {
        int suma = 0;
        for (Ficha ficha : fichas) {
            suma += ficha.getLadoA() + ficha.getLadoB();
        }
        return suma;
    }

    @Override
    public String toString() {
        return nombre + " " + fichas;
}}
