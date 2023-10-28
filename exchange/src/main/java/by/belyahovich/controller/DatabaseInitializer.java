package by.belyahovich.controller;

import by.belyahovich.repository.impl.CurrenciesRepositoryJDBC;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class DatabaseInitializer implements ServletContextListener {

    CurrenciesRepositoryJDBC currenciesCrudRepository;

    public DatabaseInitializer() {
        currenciesCrudRepository = CurrenciesRepositoryJDBC.getInstance();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        currenciesCrudRepository.initTables();
        currenciesCrudRepository.initCurrenciesData();
        currenciesCrudRepository.initExchangeRatesData();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //clear connection pool, close all connection
        currenciesCrudRepository.shutdownJDBC();
    }
}
