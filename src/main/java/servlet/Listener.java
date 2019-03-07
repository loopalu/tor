package servlet;

import util.DataSourceProvider;
import util.FileUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;

@WebListener
public class Listener implements ServletRequestListener, ServletContextListener {


    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
        ServletRequest servletRequest = servletRequestEvent.getServletRequest();
        //System.out.println(LocalDateTime.now() + " ServletRequest destroyed. Remote IP="+servletRequest.getRemoteAddr());
    }

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        ServletRequest servletRequest = servletRequestEvent.getServletRequest();
        //System.out.println(LocalDateTime.now() + " ServletRequest initialized. Remote IP="+servletRequest.getRemoteAddr());
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        BasicDataSource dataSource = new BasicDataSource();

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
             statement.executeUpdate(FileUtil.readFileFromClasspath("schema.sql"));
        } catch (SQLException e) {
             throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}