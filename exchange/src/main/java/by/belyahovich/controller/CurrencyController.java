package by.belyahovich.controller;

import by.belyahovich.dto.CurrenciesResponse;
import by.belyahovich.service.CurrenciesService;
import by.belyahovich.service.impl.CurrenciesServiceImpl;
import by.belyahovich.utils.ErrorHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet(name = "CurrencyController", urlPatterns = "/api/v1/currency/*")
public class CurrencyController extends HttpServlet {
    private final CurrenciesService currenciesService;
    private final ObjectMapper objectMapper;
    private final Logger log;

    public CurrencyController() {
        currenciesService = new CurrenciesServiceImpl();
        objectMapper = new ObjectMapper();
        log = Logger.getLogger(CurrencyController.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                ErrorHandler.sendError(HttpServletResponse.SC_BAD_REQUEST, "The currency code is missing from the address", resp);
                return;
            }
            Optional<CurrenciesResponse> currenciesByCode =
                    currenciesService.getByCode(pathInfo.substring(1).toUpperCase());

            if (currenciesByCode.isEmpty()) {
                ErrorHandler.sendError(HttpServletResponse.SC_NOT_FOUND, "Currency not found", resp);
            } else {
                String currenciesJson = objectMapper.writeValueAsString(currenciesByCode.get());
                PrintWriter printWriter = resp.getWriter();
                printWriter.print(currenciesJson);
                printWriter.flush();
            }
        } catch (Exception e) {
            log.error("Server error: " + e.getMessage());
            ErrorHandler.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Server side error", resp);
        }
    }
}
