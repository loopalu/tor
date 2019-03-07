package servlet;

import com.sun.org.apache.xpath.internal.operations.Or;
import util.DataSourceProvider;
import util.FileUtil;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {
    private static final String URL = "jdbc:hsqldb:mem:i377;shutdown=true";

    private void insertOrderRowToOrder(Long orderId, String orderNumber, OrderRow orderRow) {
        String sql = "INSERT INTO orderrows (id, orderNumber, itemName, quantity, price) VALUES (?, ?, ?, ? ,?)";
        try (Connection connection = DriverManager.getConnection(URL);
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, orderId);
            statement.setString(2, orderNumber);
            statement.setString(3, orderRow.getItemName());
            statement.setInt(4, orderRow.getQuantity());
            statement.setInt(5, orderRow.getPrice());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Order insertOrder(Order order) {
        String sql1 = "INSERT INTO orders (id, orderNumber) VALUES (next value for seq1, ?);";
        List<OrderRow> orderRows = order.getOrderRows();
        //System.out.println("insertOrder orderRows length " + orderRows.size());
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement statement = connection.prepareStatement(sql1, new String[] {"id"})) {
            statement.setString(1,order.getOrderNumber());
            statement.executeUpdate();
            ResultSet set = statement.getGeneratedKeys();
            set.next();
            Long orderId = set.getLong("id");
            if (orderRows != null && orderRows.size() > 0) {
                for (OrderRow orderRow : orderRows) {
                    insertOrderRowToOrder(orderId, order.getOrderNumber(), orderRow);
                }
            }
            Order newOrder = new Order(orderId, order.getOrderNumber(), order.getOrderRows());
            //System.out.println(newOrder);
            return newOrder;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Order getOrder(Long id) {
        String sql1 = "SELECT * FROM orders LEFT JOIN orderrows ON orders.id = orderrows.id" +
                " WHERE orderrows.id = " + id +";";
        List<OrderRow> orderRows = new ArrayList<>();
        String orderNumber = "";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql1);
            while (resultSet.next()) {
                orderNumber = resultSet.getString("orders.orderNumber");
                String itemName = resultSet.getString("itemName");
                Integer quantity = resultSet.getInt("quantity");
                Integer price = resultSet.getInt("price");
                //System.out.println("itemName "+itemName);

                if (itemName != null) {
                    OrderRow orderRow = new OrderRow(itemName,quantity,price);
                    orderRows.add(orderRow);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Order newOrder = new Order(id,orderNumber,orderRows);
        //System.out.println("getOrder newOrder "+newOrder.toString());
        return newOrder;
    }

    public List<Order> getOrderList() {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL);
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT id FROM orders;");
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Order order = getOrder(id);
                orders.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return orders;
    }

    public void deleteOrder(Long id) {
        String sql1 = "DELETE FROM orders WHERE id = ?;";
        String sql2 = "DELETE FROM orderrows WHERE orderNumber = ?;";
        Order order = getOrder(id);
        String orderNumber = order.getOrderNumber();

        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement statement = connection.prepareStatement(sql2)) {

            statement.setString(1, orderNumber);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement statement = connection.prepareStatement(sql1)) {

            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAll() {
        try (Connection connection = DriverManager.getConnection(URL);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(FileUtil.readFileFromClasspath("schema.sql")); // teeb k6ik nullist
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
