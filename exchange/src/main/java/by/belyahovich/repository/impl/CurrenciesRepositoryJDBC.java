package by.belyahovich.repository.impl;

import by.belyahovich.domain.Currencies;
import by.belyahovich.repository.BasicConnectionPool;
import by.belyahovich.repository.ConnectionPool;
import by.belyahovich.repository.CrudRepository;
import by.belyahovich.utils.ReservationException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

public class CurrenciesRepositoryJDBC implements CrudRepository<Currencies> {

    private static final Logger log = LogManager.getLogger(CurrenciesRepositoryJDBC.class);
    private static final ConnectionPool connectionPool;
    private static final String DB_URL;
    private static volatile CurrenciesRepositoryJDBC instance;

    static {
        try {
            InputStream resourceAsStream = CurrenciesRepositoryJDBC.class.getResourceAsStream("/config.properties");
            Properties properties = new Properties();
            properties.load(resourceAsStream);
            DB_URL = properties.getProperty("db.url");
            connectionPool = BasicConnectionPool.create(DB_URL);
        } catch (IOException e) {
            log.error("Properties load exception: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static CurrenciesRepositoryJDBC getInstance() {
        CurrenciesRepositoryJDBC localInstance = instance;
        if (localInstance == null) {
            synchronized (CurrenciesRepositoryJDBC.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new CurrenciesRepositoryJDBC();
                }
            }
        }
        return localInstance;
    }

    @Override
    public Iterable<Currencies> getAll() throws SQLException {
        List<Currencies> currenciesList = new ArrayList<>(Collections.emptyList());
        Connection connection = connectionPool.getConnection();

        try {
            String querySelectAll = """
                    select * from currencies
                    """;

            PreparedStatement preparedStatement = connection.prepareStatement(querySelectAll);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                currenciesList.add(Currencies.newBuilder()
                        .setId(resultSet.getInt("id"))
                        .setCode(resultSet.getString("code"))
                        .setFullName(resultSet.getString("full_name"))
                        .setSign(resultSet.getString("sign"))
                        .build());
            }
        } finally {
            connectionPool.releaseConnection(connection);
        }
        return currenciesList;
    }

    @Override
    public Optional<Currencies> getByCode(String code) throws SQLException{
        Connection connection = connectionPool.getConnection();
        Currencies currencies;
        try {
            String querySelectByCode = """
                    select * from currencies where code = (?)
                    """;

            PreparedStatement preparedStatement = connection.prepareStatement(querySelectByCode);
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                return Optional.empty();
            }

            currencies = Currencies.newBuilder()
                    .setId(resultSet.getInt("id"))
                    .setCode(resultSet.getString("code"))
                    .setFullName(resultSet.getString("full_name"))
                    .setSign(resultSet.getString("sign"))
                    .build();

        }  finally {
            connectionPool.releaseConnection(connection);
        }

        return Optional.of(currencies);
    }

    @Override
    public Currencies save(Currencies currencies) throws SQLException {
        Connection connection = connectionPool.getConnection();

        try {
            connection.setAutoCommit(false);

            String querySaveCurrencies = """
                    insert into currencies (code, full_name, sign) VALUES (?, ?, ?)
                    """;

            PreparedStatement preparedStatement =
                    connection.prepareStatement(querySaveCurrencies);
            preparedStatement.setString(1, currencies.getCode());
            preparedStatement.setString(2, currencies.getFullName());
            preparedStatement.setString(3, currencies.getSign());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                log.info("Not row affected after update");
                throw new SQLException("Not row affected after update");
            }

            //get id saved currencies
            Statement statement = connection.createStatement();
            ResultSet generatedKeys = statement.executeQuery("select  last_insert_rowid()");

            if (generatedKeys.next()) {
                currencies.setId(generatedKeys.getInt(1));
            }

            connection.commit();
            return currencies;

        } finally {
            connection.rollback();
            connectionPool.releaseConnection(connection);
        }
    }

    public void initTables() {
        Connection connection = connectionPool.getConnection();

        try {
            String queryDropTableCurrencies = """
                    drop table if exists currencies
                    """;
            String queryDropTableExchangeRates = """
                    drop table if exists exchange_rates
                    """;
            String queryCreateCurrencies = """
                    create table currencies(
                        id integer primary key autoincrement,
                        code varchar unique,
                        full_name varchar,
                        sign varchar
                    )""";
            String queryCreateExchangeRate = """
                    create table exchange_rates(
                        id integer primary key autoincrement,
                        base_currency_id integer,
                        target_currency_id integer,
                        rate decimal(6),
                        foreign key (base_currency_id) references currencies (id),
                        foreign key (target_currency_id) references currencies (id)
                    )""";

            Statement statement = connection.createStatement();
            statement.addBatch(queryDropTableCurrencies);
            statement.addBatch(queryDropTableExchangeRates);
            statement.addBatch(queryCreateCurrencies);
            statement.addBatch(queryCreateExchangeRate);
            statement.executeBatch();

            log.info("Initialization of tables was successful");
        } catch (SQLException e) {
            log.error("An error occurred while initializing tables: " + e.getMessage());
            throw new ReservationException("Server error: an error occurred while initializing tables");
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    public void initCurrenciesData() {
        Connection connection = connectionPool.getConnection();

        try {
            String queryInsert = """
                    insert into currencies (code, full_name, sign) VALUES (?, ?, ?)
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(queryInsert);
            preparedStatement.setString(1, "AUD");
            preparedStatement.setString(2, "Australian dollar");
            preparedStatement.setString(3, "A$");
            preparedStatement.addBatch();

            preparedStatement.setString(1, "USD");
            preparedStatement.setString(2, "United States dollar");
            preparedStatement.setString(3, "$");
            preparedStatement.addBatch();

            preparedStatement.setString(1, "EUR");
            preparedStatement.setString(2, "Euro");
            preparedStatement.setString(3, "€");
            preparedStatement.addBatch();

            preparedStatement.setString(1, "RUB");
            preparedStatement.setString(2, "Russian Ruble");
            preparedStatement.setString(3, "₽");
            preparedStatement.addBatch();

            preparedStatement.setString(1, "CNY");
            preparedStatement.setString(2, "Yuan");
            preparedStatement.setString(3, "¥");
            preparedStatement.addBatch();
            preparedStatement.executeBatch();

            log.info("Insert data to table currencies successful");
        } catch (SQLException e) {
            log.error("Error insert data to table currencies: " + e.getMessage());
            throw new ReservationException("Server error: error insert data to table currencies");
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    //todo move to exchange repository
    public void initExchangeRatesData() {
        Connection connection = connectionPool.getConnection();

        try {
            String queryInsert = """
                    insert into exchange_rates (base_currency_id, target_currency_id, rate) VALUES (?, ?, ?)
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(queryInsert);
            //USD -> EUR
            preparedStatement.setInt(1, 2);
            preparedStatement.setInt(2, 3);
            preparedStatement.setDouble(3, 0.95);
            preparedStatement.addBatch();
            //RUB -> EUR
            preparedStatement.setInt(1, 4);
            preparedStatement.setInt(2, 3);
            preparedStatement.setDouble(3, 0.0097);
            preparedStatement.addBatch();
            //RUB -> CNY
            preparedStatement.setInt(1, 4);
            preparedStatement.setInt(2, 5);
            preparedStatement.setDouble(3, 0.075);
            preparedStatement.addBatch();
            //USD -> AUD
            preparedStatement.setInt(1, 2);
            preparedStatement.setInt(2, 1);
            preparedStatement.setDouble(3, 1.58);
            preparedStatement.addBatch();
            preparedStatement.executeBatch();

            log.info("Insert data to table exchange_rates successful");
        } catch (SQLException e) {
            log.error("Error insert data to table exchange_rates: " + e.getMessage());
            throw new ReservationException("Server error: error insert data to table exchange_rates");
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    public void shutdownJDBC(){
        try {
            connectionPool.shutDown();
        } catch (SQLException e) {
            log.error("Error closing database connections " + e.getMessage());
            throw new ReservationException("Error closing database connections");
        }
    }
}