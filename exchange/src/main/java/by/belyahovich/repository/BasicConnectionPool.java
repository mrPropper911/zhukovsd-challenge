package by.belyahovich.repository;

import by.belyahovich.utils.ReservationException;
import org.apache.log4j.Logger;
import org.sqlite.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BasicConnectionPool implements ConnectionPool{

    private static final Logger log = Logger.getLogger(BasicConnectionPool.class);

    private final String url;
    private final List<Connection> connectionPool;
    private final List<Connection> usedConnections = new ArrayList<>();
    private static final int INITIAL_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;
    public static final int MAX_TIMEOUT = 5;//sec

    private BasicConnectionPool(String url, List<Connection> connectionPool) {
        this.url = url;
        this.connectionPool = connectionPool;
    }

    public static BasicConnectionPool create (String url){
        try {
            DriverManager.registerDriver(new JDBC());
        } catch (SQLException e) {
            log.error("Could not init register SQLite JDBC driver: " + e.getMessage());
            throw new ReservationException("Could not init register SQLite JDBC driver");
        }

        List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
        for (int i = 0; i < INITIAL_POOL_SIZE; i++){
            pool.add(createConnection(url));
        }
        return new BasicConnectionPool(url, pool);
    }

    @Override
    public Connection getConnection() {
        if (connectionPool.isEmpty()){
            if (usedConnections.size() < MAX_POOL_SIZE){
                connectionPool.add(createConnection(url));
            } else {
                log.error("Maximum pool size reached, no available connections!");
                throw new ReservationException(
                        "Maximum pool size reached, no available connections!");
            }
        }

        Connection connection = connectionPool.remove(connectionPool.size() - 1);

        try {
            if (!connection.isValid(MAX_TIMEOUT)){
                connection = createConnection(url);
            }
        } catch (SQLException e) {
            log.error("Error get connection: " + e.getMessage());
            throw new ReservationException("Server error: error get connection");
        }

        usedConnections.add(connection);
        return connection;
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);
        return usedConnections.remove(connection);
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public void shutDown() throws SQLException {
        usedConnections.forEach(this::releaseConnection);
        for (Connection connection: connectionPool) {
            connection.close();
        }
        connectionPool.clear();
        log.info("All connection to DB close, connection pool cleared");
    }

    private static Connection createConnection(String url){
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            log.error("Error creating connection" + e.getMessage());
            throw new ReservationException("Server error: error creating connection to database");
        }
    }

}
