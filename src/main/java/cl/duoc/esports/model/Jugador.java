package cl.duoc.esports.model;

public abstract class Jugador{
    protected String nickname;
    protected String videojuego;
    protected int nivel;

    public Jugador() {
    }

    public Jugador(String nickname, String videojuego, int nivel) {
        setNickname(nickname);
        setVideojuego(videojuego);
        setNivel(nivel);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        if(nickname==null || nickname.isBlank()){
            throw new IllegalArgumentException("El nickname no puede estar vacio o en blanco.");
        }
        this.nickname = nickname;
    }

    public String getVideojuego() {
        return videojuego;
    }

    public void setVideojuego(String videojuego) {
        if(videojuego==null || videojuego.isBlank()){
            throw new IllegalArgumentException("El videojuego no puede estar vacio o en blanco.");
        }
        this.videojuego = videojuego;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        if(nivel<=0){
            throw new IllegalArgumentException("El nivel debe ser positivo.");
        }
        this.nivel = nivel;
    }

    @Override
    public String toString() {
        return "Jugador{" +
                "nickname='" + nickname + '\'' +
                ", videojuego='" + videojuego + '\'' +
                ", nivel=" + nivel +
                '}';
    }

    public abstract int calcularPuntos();

    public int getPuntos(){
        return calcularPuntos();
    }

}
