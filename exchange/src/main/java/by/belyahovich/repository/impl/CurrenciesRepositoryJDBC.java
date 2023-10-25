package by.belyahovich.repository.impl;

import by.belyahovich.domain.Currencies;
import by.belyahovich.repository.BasicConnectionPool;
import by.belyahovich.repository.ConnectionPool;
import by.belyahovich.repository.CurrenciesRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

public class CurrenciesRepositoryJDBC implements CurrenciesRepository {

    private static final Logger log = LogManager.getLogger(CurrenciesRepositoryJDBC.class);
    private static final ConnectionPool connectionPool;
    private static String DB_URL;
    private static volatile CurrenciesRepositoryJDBC instance;

    static {
        InputStream resourceAsStream = CurrenciesRepositoryJDBC.class.getResourceAsStream("/config.properties");
        if (resourceAsStream != null) {
            Properties properties = new Properties();
            try {
                properties.load(resourceAsStream);
            } catch (IOException e) {
                log.error("Properties load exception: " + e.getMessage());
                throw new RuntimeException(e);
            }
            DB_URL = properties.getProperty("db.url");
        } else {
            log.error("Resources config.properties not found");
        }
        connectionPool = BasicConnectionPool.create(DB_URL);

        //init data
        initTables();
        initCurrenciesData();
        initExchangeRatesData();
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

    public static void initTables() {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
        } catch (SQLException e) {
            log.error("Error get connection: " + e.getMessage());
            throw new RuntimeException(e);
        }
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

            log.info("Init tables successful");
        } catch (SQLException e) {
            log.error("Init database exception: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            boolean releaseConnection = connectionPool.releaseConnection(connection);
            if (releaseConnection) {
                log.info("ConnectionPool release");
            } else {
                log.error("ConnectionPool release error");
            }
        }
    }

    public static void initCurrenciesData() {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
        } catch (SQLException e) {
            log.error("Error get connection: " + e.getMessage());
            throw new RuntimeException(e);
        }
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
            throw new RuntimeException(e);
        } finally {
            if (connectionPool.releaseConnection(connection)) {
                log.info("Connection pool release successfully");
            } else {
                log.error("ConnectionPool release error");
            }
        }
    }

    public static void initExchangeRatesData() {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
        } catch (SQLException e) {
            log.error("Error get connection: " + e.getMessage());
            throw new RuntimeException(e);
        }
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
            connectionPool.releaseConnection(connection);
            log.info("Insert data to table exchange_rates successful");
        } catch (SQLException e) {
            log.error("Error insert data to table exchange_rates: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Currencies save(Currencies currencies) throws SQLException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
        } catch (SQLException e) {
            log.error("Error get connection: " + e.getMessage());
            throw new RuntimeException(e);
        }

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
                log.error("Error: not row affected after update");
                throw new SQLException("Creating currencies failed, not row affected");
            }

            //get id saved currencies
            Statement statement = connection.createStatement();
            ResultSet generatedKeys = statement.executeQuery("select  last_insert_rowid()");
            if (generatedKeys.next()) {
                currencies.setId(generatedKeys.getInt(1));
            }
            connection.commit();

            log.info("Transaction commit");
            log.info("Save currencies succeed: " + currencies.getCode());
            return currencies;

        } catch (SQLException e) {
            connection.rollback();
            log.error("Transaction rollback...");
            throw new SQLException();
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public Iterable<Currencies> getAll() {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
        } catch (SQLException e) {
            log.error("Error get connection: " + e.getMessage());
            throw new RuntimeException(e);
        }

        List<Currencies> currenciesList = new ArrayList<>(Collections.emptyList());

        try {
            connection = DriverManager.getConnection(DB_URL);

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
            log.info("Get all currency successful");
        } catch (SQLException e) {
            throw new RuntimeException();
        } finally {
            connectionPool.releaseConnection(connection);
        }
        return currenciesList;
    }

    @Override
    public Optional<Currencies> getByCode(String code) {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
        } catch (SQLException e) {
            log.error("Error get connection: " + e.getMessage());
            throw new RuntimeException(e);
        }
        Currencies currencies = null;
        try {
            connection = DriverManager.getConnection(DB_URL);
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
            log.info("Get by code successful: " + code);

        } catch (SQLException e) {
            log.error("Error Get by code:" + code);
            log.error(e.getMessage());
            throw new RuntimeException();
        } finally {
            connectionPool.releaseConnection(connection);
        }

        return Optional.ofNullable(currencies);
    }
}
