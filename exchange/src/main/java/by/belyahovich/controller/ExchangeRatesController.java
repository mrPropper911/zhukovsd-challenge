package by.belyahovich.controller;

import by.belyahovich.dto.ExchangeRatesResponse;
import by.belyahovich.service.ExchangeRatesService;
import by.belyahovich.service.impl.ExchangeRatesServiceImpl;
import by.belyahovich.utils.ErrorHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ExchangeRatesController", urlPatterns = "/api/v1/exchangeRates")
public class ExchangeRatesController extends HttpServlet {

    private final ExchangeRatesService exchangeRatesService;
    private final ObjectMapper objectMapper;

    private final Logger log;

    public ExchangeRatesController() {
        exchangeRatesService = new ExchangeRatesServiceImpl();
        objectMapper = new ObjectMapper();
        log = Logger.getLogger(ExchangeRatesController.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            List<ExchangeRatesResponse> allExchangeRateList = exchangeRatesService.getAll();
            PrintWriter writer = resp.getWriter();
            writer.print(objectMapper.writeValueAsString(allExchangeRateList.toArray()));
            writer.flush();
        } catch (SQLException | IOException e) {
            log.error("Server error: " + e.getMessage());
            ErrorHandler.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Server side error", resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

    }
}
