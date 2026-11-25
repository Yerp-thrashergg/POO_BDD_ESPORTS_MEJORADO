package cl.duoc.esports.model;

public class  Casual extends Jugador{
    private int partidasJugadas;

    public Casual() {
    }

    public Casual(String nickname, String videojuego, int nivel, int partidasJugadas) {
        super(nickname, videojuego, nivel);
        setPartidasJugadas(partidasJugadas);
    }

    public int getPartidasJugadas() {
        return partidasJugadas;
    }

    public void setPartidasJugadas(int partidasJugadas) {
        if(partidasJugadas<0){
            throw new IllegalArgumentException("La cantidad de partidad jugadas no puede ser negativa.");
        }
        this.partidasJugadas = partidasJugadas;
    }

    @Override
    public String toString() {
        return super.toString() + "Casual{" +
                "partidasJugadas=" + partidasJugadas +
                '}';
    }

    @Override
    public int calcularPuntos() {
        return (super.nivel * 10) + (this.partidasJugadas * 2);
    }
}
