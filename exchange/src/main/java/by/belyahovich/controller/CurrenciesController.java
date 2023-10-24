package by.belyahovich.controller;

import by.belyahovich.domain.Currencies;
import by.belyahovich.dto.CurrenciesMapper;
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
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CurrenciesController", urlPatterns = "/api/v1/currencies")
public class CurrenciesController extends HttpServlet {

    Logger log = LogManager.getLogger(CurrenciesController.class);
    private final CurrenciesService currenciesService = new CurrenciesServiceImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();
    CurrenciesMapper currenciesMapper;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        List<Currencies> allCurrenciesJsonString = new ArrayList<>();

        try {
            allCurrenciesJsonString = currenciesService.getAll();
        } catch (Exception e){
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            log.error("Get error: " + e.getMessage());
            return;
        }

        try {
            PrintWriter writer = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            writer.print(objectMapper.writeValueAsString(allCurrenciesJsonString));
            writer.flush();
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            log.error("Get error with writer: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)  {
        CurrenciesRequest currenciesRequest = CurrenciesRequest.newBuilder()
                .setCode(req.getParameter("code"))
                .setName(req.getParameter("name"))
                .setSign(req.getParameter("sign")).build();
        // TODO: 10/25/2023 add last exception
        try {
            CurrenciesResponse savedCurrencies = currenciesService.save(currenciesRequest);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().print(objectMapper.writeValueAsString(savedCurrencies));
        } catch (SQLException e){
            ErrorHandler.sendError(HttpServletResponse.SC_CONFLICT, "A currency with this code already exists", resp);
        } catch (Exception e){
            throw new RuntimeException();
        }

    }
}
