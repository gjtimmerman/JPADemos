package com.infosupport.demo1startwithjdbc;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.sql.*;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StartWithProgresTests {
    @Test
    @Order(1)
    void showThatConnectionToProgressIsNotNull() throws SQLException
    {
        Connection connection = createConnection();
        assertThat(connection).isNotNull();
        connection.close();
    }
    private Connection createConnection() throws SQLException
    {
        String url = "jdbc:postgresql://localhost:5432/cursistdb";
        String username = "cursist";
        String password = "PaSSw0rd";
        return DriverManager.getConnection(url,username,password);
    }

    @Test
    @Order(2)
    void dropTableCursist() throws SQLException
    {
        boolean executed;
        Connection connection = createConnection();
        Statement statement = connection.createStatement();
        executed = statement.execute("DROP TABLE IF EXISTS cursist;");
        assertThat(executed).isEqualTo(false);
        connection.close();
    }
    @Test
    @Order(3)
    void createTable() throws SQLException
    {
        Connection connection = createConnection();
        Statement statement = connection.createStatement();
        String sql = "CREATE TABLE cursist " +
                "(ID SERIAL PRIMARY KEY NOT NULL, " +
                "NAME TEXT NOT NULL, " +
                "AGE INT NOT NULL, " +
                "COUNTRY CHAR(50) constraint countryname_different unique" +
                ")";
        int executeUpdate = statement.executeUpdate(sql);
        assertThat(executeUpdate).isEqualTo(0);
        connection.close();
    }
    @Test
    @Order(4)
    void insertRows() throws SQLException
    {
        Connection connection = createConnection();
        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();
        int numberOfRowsInserted = 0;
        try {
            String sql = "INSERT INTO cursist (NAME, AGE, COUNTRY) " +
                    "VALUES ('Johan', 43, 'Nederland');";
            numberOfRowsInserted += statement.executeUpdate(sql);

            sql = "INSERT INTO cursist (NAME, AGE, COUNTRY) " +
                    "VALUES ('Allen', 25, 'Texas');";

            numberOfRowsInserted += statement.executeUpdate(sql);

            assertThat(numberOfRowsInserted).isEqualTo(2);
            sql = "INSERT INTO cursist (NAME, AGE, COUNTRY) " +
                    "VALUES ('Johan', 43, 'Belgie') RETURNING *;";
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            assertThat(resultSet.getInt("id")).isEqualTo(3L);
        }
        catch(SQLException se)
        {
            System.err.println("Something went wrong: " + se.getMessage());
            connection.rollback();
        }
        connection.commit();
        connection.close();
    }
    @Test
    @Order(5)
    void countNumberOfRowsInTableCursist() throws SQLException
    {
        Connection connection = createConnection();
        Statement statement = connection.createStatement();
        String sql = "SELECT COUNT(*) NumberOfRows from cursist;";
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        long numberOfRows = resultSet.getLong("NumberOfRows");
        assertThat(numberOfRows).isEqualTo(3L);
        connection.close();
    }
}
