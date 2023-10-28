package by.belyahovich.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class BasicConnectionPoolTest {

    @Test
    void getConnection_whenCalled_thenCorrect() throws SQLException {
        ConnectionPool connectionPool = BasicConnectionPool.create("jdbc:sqlite:sample.db");
        Assertions.assertTrue(connectionPool.getConnection().isValid(1));
    }
}