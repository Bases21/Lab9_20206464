package com.example.lab9_base.Dao;

import com.example.lab9_base.Bean.Arbitro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DaoArbitros extends DaoBase {

    public ArrayList<Arbitro> listarArbitros() {
        ArrayList<Arbitro> arbitros = new ArrayList<>();
        String sql = "SELECT * FROM arbitro";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Arbitro arbitro = new Arbitro();
                arbitro.setIdArbitro(rs.getInt("idArbitro"));
                arbitro.setNombre(rs.getString("nombre"));
                arbitro.setPais(rs.getString("pais"));
                arbitros.add(arbitro);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arbitros;
    }

    public ArrayList<Arbitro> busquedaPais(String pais) {
        ArrayList<Arbitro> arbitros = new ArrayList<>();
        String sql = "SELECT * FROM arbitro WHERE pais LIKE ?";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + pais + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Arbitro arbitro = new Arbitro();
                arbitro.setIdArbitro(rs.getInt("idArbitro"));
                arbitro.setNombre(rs.getString("nombre"));
                arbitro.setPais(rs.getString("pais"));
                arbitros.add(arbitro);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arbitros;
    }
    public ArrayList<Arbitro> busquedaNombre(String nombre) {
        ArrayList<Arbitro> arbitros = new ArrayList<>();
        String sql = "SELECT * FROM arbitro WHERE nombre LIKE ?";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + nombre + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Arbitro arbitro = new Arbitro();
                arbitro.setIdArbitro(rs.getInt("idArbitro"));
                arbitro.setNombre(rs.getString("nombre"));
                arbitro.setPais(rs.getString("pais"));
                arbitros.add(arbitro);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arbitros;
    }

    public ArrayList<String> listarPaises() {
        ArrayList<String> paises = new ArrayList<>();
        String sql = "SELECT DISTINCT pais FROM arbitro";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String pais = rs.getString("pais");
                paises.add(pais);
                System.out.println("País encontrado: " + pais);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paises;
    }

    public boolean borrarArbitro(int id) {
        String verificarSql = "SELECT COUNT(*) FROM arbitro WHERE idArbitro = ?";
        String borrarSql = "DELETE FROM arbitro WHERE idArbitro = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement verificarStmt = conn.prepareStatement(verificarSql);
             PreparedStatement borrarStmt = conn.prepareStatement(borrarSql)) {
            verificarStmt.setInt(1, id);
            ResultSet rs = verificarStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                borrarStmt.setInt(1, id);
                borrarStmt.executeUpdate();
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean crearArbitro(String nombre, String pais) {
        String verificarSql = "SELECT COUNT(*) FROM arbitro WHERE nombre = ?";
        String insertarSql = "INSERT INTO arbitro (nombre, pais) VALUES (?, ?)";

        try (Connection conn = this.getConnection();
             PreparedStatement verificarStmt = conn.prepareStatement(verificarSql);
             PreparedStatement insertarStmt = conn.prepareStatement(insertarSql)) {
            verificarStmt.setString(1, nombre);
            ResultSet rs = verificarStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false;
            }
            insertarStmt.setString(1, nombre);
            insertarStmt.setString(2, pais);
            insertarStmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
