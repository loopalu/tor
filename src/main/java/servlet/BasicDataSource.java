package servlet;

import util.DataSourceProvider;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class BasicDataSource {

    private static final String URL = "jdbc:hsqldb:mem:i377;shutdown=true";
    private Connection connection;


    public BasicDataSource() {
        DataSourceProvider.setDbUrl(URL);
        DataSource source = DataSourceProvider.getDataSource();
        try {
            this.connection = source.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
