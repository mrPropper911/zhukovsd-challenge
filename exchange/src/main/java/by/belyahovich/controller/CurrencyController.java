package by.belyahovich.controller;

import by.belyahovich.domain.Currencies;
import by.belyahovich.service.CurrenciesService;
import by.belyahovich.service.impl.CurrenciesServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet(name = "CurrencyController", urlPatterns = "/api/v1/currency/*")
public class CurrencyController extends HttpServlet {

    private final CurrenciesService currenciesService = new CurrenciesServiceImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                resp.sendError(400, "The currency code is missing from the address");
                return;
            }
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            Optional<Currencies> currenciesByCode = currenciesService.getByCode(pathInfo.substring(1).toUpperCase());

            if (currenciesByCode.isEmpty()) {
                resp.sendError(404, "Currency not found");
            } else {
                String currenciesJson = objectMapper.writeValueAsString(currenciesByCode.get());
                PrintWriter printWriter = resp.getWriter();
                printWriter.print(currenciesJson);
                printWriter.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
