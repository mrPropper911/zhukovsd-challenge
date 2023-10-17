package by.belyahovich.controller;

import by.belyahovich.repository.CurrenciesRepository;
import by.belyahovich.repository.impl.CurrenciesRepositoryJDBC;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebServlet(name = "Currencies", urlPatterns = {"/api/v1/currencies"})
public class CurrenciesController extends HttpServlet {

//    private CurrenciesRepository currenciesRepository = new CurrenciesRepositoryJDBC();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {

            ServletOutputStream outputStream = resp.getOutputStream();

            outputStream.print("<html>");
            outputStream.print("<h1>Some array</h1>");
            outputStream.print("</html>");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
