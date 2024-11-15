package com.example.lab9_base.Controller;

import com.example.lab9_base.Bean.Arbitro;
import com.example.lab9_base.Dao.DaoArbitros;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ArbitroServlet", urlPatterns = {"/ArbitroServlet"})
public class ArbitroServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        RequestDispatcher view;
        ArrayList<String> opciones = new ArrayList<>();
        opciones.add("nombre");
        opciones.add("pais");

        DaoArbitros daoArbitros = new DaoArbitros();

        switch (action) {
            case "buscar":
                String tipo = request.getParameter("tipo");
                String criterio = request.getParameter("buscar");
                ArrayList<Arbitro> resultadoBusqueda;

                if ("pais".equals(tipo)) {
                    resultadoBusqueda = daoArbitros.busquedaPais(criterio);
                } else {
                    resultadoBusqueda = daoArbitros.busquedaNombre(criterio);
                }

                request.setAttribute("listaArbitros", resultadoBusqueda);
                view = request.getRequestDispatcher("/arbitros/list.jsp");
                view.forward(request, response);
                break;


            case "guardar":
                String nombre = request.getParameter("nombre").trim();
                String pais = request.getParameter("pais").trim();

                if (nombre.isEmpty() || pais.isEmpty()) {
                    request.setAttribute("error", "Todos los campos son obligatorios.");
                    view = request.getRequestDispatcher("/arbitros/form.jsp");
                    view.forward(request, response);
                    return;
                }

                boolean exito = daoArbitros.crearArbitro(nombre, pais);
                if (exito) {
                    response.sendRedirect(request.getContextPath() + "/ArbitroServlet?action=lista");
                } else {
                    request.setAttribute("error", "El nombre del árbitro ya existe. Intente con otro nombre.");
                    view = request.getRequestDispatcher("/arbitros/form.jsp");
                    view.forward(request, response);
                }
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DaoArbitros daoArbitros = new DaoArbitros();

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        RequestDispatcher view;
        ArrayList<String> paises = new ArrayList<>();
        paises.add("Peru");
        paises.add("Chile");
        paises.add("Argentina");
        paises.add("Paraguay");
        paises.add("Uruguay");
        paises.add("Colombia");
        ArrayList<String> opciones = new ArrayList<>();
        opciones.add("nombre");
        opciones.add("pais");

        switch (action) {
            case "lista":
                ArrayList<Arbitro> listaArbitros = daoArbitros.listarArbitros();
                request.setAttribute("listaArbitros", listaArbitros);
                view = request.getRequestDispatcher("/arbitros/list.jsp");
                view.forward(request, response);
                break;

            case "crear":
                ArrayList<String> listaPaises = daoArbitros.listarPaises();
                System.out.println("Número de países encontrados: " + listaPaises.size());
                request.setAttribute("listaPaises", listaPaises);
                view = request.getRequestDispatcher("/arbitros/form.jsp");
                view.forward(request, response);
                break;

            case "borrar":
                int idArbitro = Integer.parseInt(request.getParameter("id"));
                boolean exito = daoArbitros.borrarArbitro(idArbitro);
                if (exito) {
                    response.sendRedirect(request.getContextPath() + "/ArbitroServlet?action=lista");
                } else {
                    request.setAttribute("error", "El árbitro no existe o ya ha sido eliminado.");
                    view = request.getRequestDispatcher("/arbitros/list.jsp");
                    view.forward(request, response);
                }
                break;
                }
    }
}
