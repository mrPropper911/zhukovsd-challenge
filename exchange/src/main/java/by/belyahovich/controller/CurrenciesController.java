package by.belyahovich.controller;

import by.belyahovich.domain.Currencies;
import by.belyahovich.dto.CurrenciesRequest;
import by.belyahovich.dto.CurrenciesResponse;
import by.belyahovich.service.CurrenciesService;
import by.belyahovich.service.impl.CurrenciesServiceImpl;
import by.belyahovich.utils.ErrorHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "CurrenciesController", urlPatterns = "/api/v1/currencies")
public class CurrenciesController extends HttpServlet {

    private final CurrenciesService currenciesService;
    private final ObjectMapper objectMapper;
    private final Logger log;

    public CurrenciesController() {
        currenciesService = new CurrenciesServiceImpl();
        objectMapper = new ObjectMapper();
        log = LogManager.getLogger(CurrenciesController.class);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            List<CurrenciesResponse> allCurrenciesJsonString = currenciesService.getAll();
            PrintWriter writer = resp.getWriter();
            writer.print(objectMapper.writeValueAsString(allCurrenciesJsonString));
            writer.flush();
        } catch (Exception e) {
            log.error("Server error: " + e.getMessage());
            ErrorHandler.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Server side error", resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String codeCurrencies = req.getParameter("code");
        String nameCurrencies = req.getParameter("name");
        String signCurrencies = req.getParameter("sign");

        //validation
        if (codeCurrencies == null || nameCurrencies == null || signCurrencies == null ||
                codeCurrencies.equals("") || nameCurrencies.equals("") || signCurrencies.equals("")) {
            ErrorHandler.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "A required form field is empty or missing", resp);
            return;
        }

        CurrenciesRequest currenciesRequest = CurrenciesRequest.newBuilder()
                .setCode(codeCurrencies)
                .setName(nameCurrencies)
                .setSign(signCurrencies).build();
        try {
            CurrenciesResponse savedCurrencies = currenciesService.save(currenciesRequest);
            resp.getWriter().print(objectMapper.writeValueAsString(savedCurrencies));
        } catch (SQLException e) {
            ErrorHandler.sendError(HttpServletResponse.SC_CONFLICT, "A currency with this code already exists", resp);
        } catch (Exception e) {
            log.error("Error from server: " + e.getMessage());
            ErrorHandler.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error ...", resp);
        }

    }
}
