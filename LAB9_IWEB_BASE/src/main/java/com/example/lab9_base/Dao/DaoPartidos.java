package com.example.lab9_base.Dao;

import com.example.lab9_base.Bean.Arbitro;
import com.example.lab9_base.Bean.Estadio;
import com.example.lab9_base.Bean.Partido;
import com.example.lab9_base.Bean.Seleccion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DaoPartidos extends DaoBase {
    public ArrayList<Partido> listaDePartidos() {
        ArrayList<Partido> partidos = new ArrayList<>();
        String sql = "SELECT p.idPartido, p.fecha, p.numeroJornada, " +
                "sLocal.idSeleccion AS idSeleccionLocal, sLocal.nombre AS nombreLocal, sLocal.tecnico AS tecnicoLocal, " +
                "e.idEstadio, e.nombre AS nombreEstadio, e.provincia, e.club, " +
                "sVisitante.idSeleccion AS idSeleccionVisitante, sVisitante.nombre AS nombreVisitante, sVisitante.tecnico AS tecnicoVisitante, " +
                "a.idArbitro, a.nombre AS nombreArbitro, a.pais " +
                "FROM partido p " +
                "JOIN seleccion sLocal ON p.seleccionLocal = sLocal.idSeleccion " +
                "JOIN estadio e ON sLocal.estadio_idEstadio = e.idEstadio " +
                "JOIN seleccion sVisitante ON p.seleccionVisitante = sVisitante.idSeleccion " +
                "JOIN arbitro a ON p.arbitro = a.idArbitro";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Partido partido = new Partido();
                partido.setIdPartido(rs.getInt("idPartido"));
                partido.setFecha(rs.getString("fecha"));
                partido.setNumeroJornada(rs.getInt("numeroJornada"));

                Seleccion seleccionLocal = new Seleccion();
                seleccionLocal.setIdSeleccion(rs.getInt("idSeleccionLocal"));
                seleccionLocal.setNombre(rs.getString("nombreLocal"));
                seleccionLocal.setTecnico(rs.getString("tecnicoLocal"));

                Estadio estadio = new Estadio();
                estadio.setIdEstadio(rs.getInt("idEstadio"));
                estadio.setNombre(rs.getString("nombreEstadio"));
                estadio.setProvincia(rs.getString("provincia"));
                estadio.setClub(rs.getString("club"));

                seleccionLocal.setEstadio(estadio);
                partido.setSeleccionLocal(seleccionLocal);

                Seleccion seleccionVisitante = new Seleccion();
                seleccionVisitante.setIdSeleccion(rs.getInt("idSeleccionVisitante"));
                seleccionVisitante.setNombre(rs.getString("nombreVisitante"));
                seleccionVisitante.setTecnico(rs.getString("tecnicoVisitante"));
                partido.setSeleccionVisitante(seleccionVisitante);

                Arbitro arbitro = new Arbitro();
                arbitro.setIdArbitro(rs.getInt("idArbitro"));
                arbitro.setNombre(rs.getString("nombreArbitro"));
                arbitro.setPais(rs.getString("pais"));
                partido.setArbitro(arbitro);

                partidos.add(partido);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return partidos;
    }


    public void crearPartido(Partido partido) {
        String sql = "INSERT INTO partido (seleccionLocal, seleccionVisitante, arbitro, fecha, numeroJornada) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, partido.getSeleccionLocal().getIdSeleccion());
            pstmt.setInt(2, partido.getSeleccionVisitante().getIdSeleccion());
            pstmt.setInt(3, partido.getArbitro().getIdArbitro());
            pstmt.setString(4, partido.getFecha());
            pstmt.setInt(5, partido.getNumeroJornada());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
