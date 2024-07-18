package by.belyahovich.repository;

import by.belyahovich.domain.Currencies;
import by.belyahovich.repository.impl.CurrenciesRepositoryJDBC;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

public interface CrudRepository<T> {

    Iterable<T> getAll() throws SQLException;

    Optional<T> getByCode(String code) throws SQLException;

    Currencies save(T currencies) throws SQLException;

    default ConnectionPool getPoolConnection(){
        Logger log = Logger.getLogger(CrudRepository.class);
        InputStream resourceAsStream =
                CurrenciesRepositoryJDBC.class.getResourceAsStream("/config.properties");
        Properties properties = new Properties();
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            log.error("Properties load exception: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return  BasicConnectionPool.create(properties.getProperty("db.url"));
    }
}
