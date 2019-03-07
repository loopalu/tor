package util;

import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;

public class DataSourceProvider {

    private static String dbUrl = null;

    private static BasicDataSource dataSource = null;

    public static void setDbUrl(String url) {
        dbUrl = url;
    }

    public static DataSource getDataSource() {
        if (dataSource != null) {
            return dataSource;
        }

        if (dbUrl == null) {
            throw new IllegalStateException(
                    "Database url not configured. Use setDbUrl()");
        }

        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        dataSource.setUrl(dbUrl);
        dataSource.setMaxActive(3);

        return dataSource;
    }

}
