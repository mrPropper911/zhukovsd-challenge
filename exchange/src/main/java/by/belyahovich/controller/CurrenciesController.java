package by.belyahovich.controller;

import by.belyahovich.domain.Currencies;
import by.belyahovich.service.CurrenciesService;
import by.belyahovich.service.impl.CurrenciesServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CurrenciesController extends HttpServlet {

    private final CurrenciesService currenciesService = new CurrenciesServiceImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        List<Currencies> allCurrenciesJsonString = currenciesService.getAll();
        try {
            PrintWriter writer = resp.getWriter();
            writer.print(objectMapper.writeValueAsString(allCurrenciesJsonString));
            writer.flush();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
