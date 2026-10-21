public class Ficha {
    private int ladoA;
    private int ladoB;

    public Ficha(int ladoA, int ladoB) {
       if((ladoA < 0 || ladoA > 6) || (ladoB < 0 || ladoB > 6)){
           throw new IllegalArgumentException();
       }
        this.ladoA = ladoA;
        this.ladoB = ladoB;
    }

    public int getLadoA() {
        return this.ladoA;
    }
    public int getLadoB() {
        return this.ladoB;
    }

    public int getPuntosTotales() {
        return this.ladoA + this.ladoB;
    }

    // Invierte la ficha para acomodarla a la cadena si es necesario
    public Ficha invertir() {
        Ficha nueva = new Ficha(getLadoB(), getLadoA());
        return nueva;
    }

    @Override
    public String toString() {
        return "[" + getLadoA() + "|" + getLadoB() + "]";
    }
}
