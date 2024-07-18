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
import java.util.Optional;

@WebServlet(name = "ExchangeRateController", urlPatterns = "/api/v1/exchangeRate/*")
public class ExchangeRateController extends HttpServlet {

    private final ExchangeRatesService exchangeRatesService;
    private final ObjectMapper objectMapper;
    private final Logger log;

    public ExchangeRateController() {
        exchangeRatesService = new ExchangeRatesServiceImpl();
        objectMapper = new ObjectMapper();
        log = Logger.getLogger(ExchangeRateController.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.length() != 7) {
            ErrorHandler.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "The pair's currency codes are missing in the address", resp);
            return;
        }

        try {
            Optional<ExchangeRatesResponse> exchangeRatesByCode =
                    exchangeRatesService.getByCode(pathInfo.substring(1).toUpperCase());

            if (exchangeRatesByCode.isEmpty()) {
                ErrorHandler.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "Exchange rate for pair not found", resp);
            } else {
                PrintWriter writer = resp.getWriter();
                writer.print(objectMapper.writeValueAsString(exchangeRatesByCode.get()));
                writer.flush();
            }
        } catch (SQLException | IOException e) {
            log.error("Server error: " + e.getMessage());
            ErrorHandler.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Server side error", resp);
        }
    }
}
