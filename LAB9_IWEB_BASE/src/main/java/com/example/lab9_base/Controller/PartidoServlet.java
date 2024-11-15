package com.example.lab9_base.Controller;

import com.example.lab9_base.Bean.Arbitro;
import com.example.lab9_base.Bean.Partido;
import com.example.lab9_base.Bean.Seleccion;
import com.example.lab9_base.Dao.DaoArbitros;
import com.example.lab9_base.Dao.DaoPartidos;
import com.example.lab9_base.Dao.DaoSelecciones;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "PartidoServlet", urlPatterns = {"/PartidoServlet", ""})
public class PartidoServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "guardar" : request.getParameter("action");
        RequestDispatcher view;
        DaoPartidos daoPartidos = new DaoPartidos();
        switch (action) {
            case "guardar":
                String fecha = request.getParameter("fecha");
                String jornadaStr = request.getParameter("jornada");
                String idSeleccionLocalStr = request.getParameter("local");
                String idSeleccionVisitanteStr = request.getParameter("visitante");
                String idArbitroStr = request.getParameter("arbitro");

                if (fecha == null || fecha.isEmpty() ||
                        jornadaStr == null || jornadaStr.isEmpty() ||
                        idSeleccionLocalStr == null || idSeleccionLocalStr.isEmpty() ||
                        idSeleccionVisitanteStr == null || idSeleccionVisitanteStr.isEmpty() ||
                        idArbitroStr == null || idArbitroStr.isEmpty()) {

                    request.setAttribute("error", "Todos los campos son obligatorios.");
                    request.getRequestDispatcher("partidos/form.jsp").forward(request, response);
                    return;
                }

                int numeroJornada = Integer.parseInt(jornadaStr);
                int idSeleccionLocal = Integer.parseInt(idSeleccionLocalStr);
                int idSeleccionVisitante = Integer.parseInt(idSeleccionVisitanteStr);
                int idArbitro = Integer.parseInt(idArbitroStr);

                if (idSeleccionLocal == idSeleccionVisitante) {
                    request.setAttribute("error", "La selección visitante no puede ser igual que la selección local.");
                    request.getRequestDispatcher("partidos/form.jsp").forward(request, response);
                    return;
                }

                ArrayList<Partido> listaPartidos = daoPartidos.listaDePartidos();
                for (Partido partidoExistente : listaPartidos) {
                    if (partidoExistente.getNumeroJornada() == numeroJornada &&
                            partidoExistente.getSeleccionLocal().getIdSeleccion() == idSeleccionLocal &&
                            partidoExistente.getSeleccionVisitante().getIdSeleccion() == idSeleccionVisitante) {

                        request.setAttribute("error", "El partido ya existe en la base de datos.");
                        request.getRequestDispatcher("partidos/form.jsp").forward(request, response);
                        return;
                    }
                }

                Partido partido = new Partido();
                partido.setFecha(fecha);
                partido.setNumeroJornada(numeroJornada);
                Seleccion seleccionLocal = new Seleccion();
                seleccionLocal.setIdSeleccion(idSeleccionLocal);
                partido.setSeleccionLocal(seleccionLocal);
                Seleccion seleccionVisitante = new Seleccion();
                seleccionVisitante.setIdSeleccion(idSeleccionVisitante);
                partido.setSeleccionVisitante(seleccionVisitante);
                Arbitro arbitro = new Arbitro();
                arbitro.setIdArbitro(idArbitro);
                partido.setArbitro(arbitro);

                daoPartidos.crearPartido(partido);
                response.sendRedirect(request.getContextPath() + "/PartidoServlet?action=lista");
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        RequestDispatcher view;
        DaoPartidos daoPartidos = new DaoPartidos();
        DaoSelecciones daoSelecciones = new DaoSelecciones();
        DaoArbitros daoArbitros = new DaoArbitros();

        switch (action) {
            case "lista":

                ArrayList<Partido> listaPartidos = daoPartidos.listaDePartidos();
                request.setAttribute("listaPartidos", listaPartidos);
                view = request.getRequestDispatcher("index.jsp");
                view.forward(request, response);
                break;

            case "crear":
                ArrayList<Seleccion> listaSelecciones = daoSelecciones.listarSelecciones();
                ArrayList<Arbitro> listaArbitros = daoArbitros.listarArbitros();
                request.setAttribute("listaSelecciones", listaSelecciones);
                request.setAttribute("listaArbitros", listaArbitros);
                view = request.getRequestDispatcher("partidos/form.jsp");
                view.forward(request, response);
                break;
        }
    }
}
