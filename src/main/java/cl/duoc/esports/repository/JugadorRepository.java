package cl.duoc.esports.repository;

import cl.duoc.esports.model.Casual;
import cl.duoc.esports.model.Competitivo;
import cl.duoc.esports.model.Jugador;
import cl.duoc.esports.utils.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class JugadorRepository {
    private Connection coneLocal;

    public JugadorRepository() {
    }

    //  Agregar Jugador:
    public boolean agregarJugador(Jugador j) throws SQLException {
        boolean inserto = false;
        try {
            coneLocal = new Conexion().getCone();
            String sql = "INSERT INTO jugador (nickname, videojuego, nivel, tipo, puntos) VALUES (?,?,?,?,?)";
            PreparedStatement ps = coneLocal.prepareStatement(sql);

            String tipo = "Competitivo";
            if (j instanceof Casual) {
                tipo = "Casual";
            }

            ps.setString(1, j.getNickname());
            ps.setString(2, j.getVideojuego());
            ps.setInt(3, j.getNivel());
            ps.setString(4, tipo);
            ps.setInt(5, j.getPuntos());
            int res = ps.executeUpdate();
            if (res == 1) {
                inserto = true;
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        } finally {
            coneLocal.close();
        }
        return inserto;
    }

    // Eliminar Jugador:
    public boolean eliminarJugador(Jugador j) throws SQLException {
        boolean eliminar = false;
        try {
            coneLocal = new Conexion().getCone();
            String sql = "DELETE FROM jugador WHERE nickname = ?";
            PreparedStatement ps = coneLocal.prepareStatement(sql);
            ps.setString(1, j.getNickname());

            int res = ps.executeUpdate();
            if (res == 1) {
                eliminar = true;
            }

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        } finally {
            coneLocal.close();
        }
        return eliminar;
    }

    // Ver Jugadores:
    public ArrayList<Jugador> verJugadores() throws SQLException {
        ArrayList<Jugador> lista = new ArrayList<>();
        try {
            coneLocal = new Conexion().getCone();
            String sql = "SELECT * FROM jugador";
            PreparedStatement ps = coneLocal.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String nickname = rs.getString(2);
                String videojuego = rs.getString(3);
                int nivel = rs.getInt(4);
                String tipo = rs.getString(6);
                int cantidad = rs.getInt(7);

                Jugador j;
                if ("Casual".equals(tipo)) {
                    j = new Casual(nickname, videojuego, nivel, cantidad);
                } else {
                    j = new Competitivo(nickname, videojuego, nivel, cantidad);
                }
                lista.add(j);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        } finally {
            coneLocal.close();
        }
        return lista;
    }

    // Modificar Jugador:
    public boolean modificarJugador(Jugador j) throws SQLException {
        boolean modificar = false;
        try {
            coneLocal = new Conexion().getCone();
            String sql = "UPDATE jugador SET videojuego = ?, nivel = ?, puntos = ?, tipo = ? WHERE nickname = ?";
            PreparedStatement ps = coneLocal.prepareStatement(sql);

            String tipo = "Competitivo";
            if (j instanceof Casual) {
                tipo = "Casual";
            }

            ps.setString(1, j.getVideojuego());
            ps.setInt(2, j.getNivel());
            ps.setInt(3, j.getPuntos());
            ps.setString(4, tipo);
            ps.setString(5, j.getNickname());
            int res = ps.executeUpdate();
            if (res == 1) {
                modificar = true;
            }
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }finally{
            coneLocal.close();
        }
        return  modificar;
    }

    // Ver Jugador:
    public Jugador verJugador(String  nickname) throws SQLException {
        Jugador j = null;
        try {
            coneLocal = new Conexion().getCone();
            String sql = "SELECT * FROM jugador WHERE nickname = ?";
            PreparedStatement ps = coneLocal.prepareStatement(sql);
            ps.setString(1, nickname);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String videojuego = rs.getString(3);
                int nivel = rs.getInt(4);
                String tipo = rs.getString(6);
                int cantidad = rs.getInt(7);

                if ("Casual".equals(tipo)) {
                    j = new Casual(nickname, videojuego, nivel, cantidad);
                } else {
                    j = new Competitivo(nickname, videojuego, nivel, cantidad);
                }
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        } finally {
            coneLocal.close();
        }
        return j;
    }
}
