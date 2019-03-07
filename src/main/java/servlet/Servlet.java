package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import util.Util;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@WebServlet({"/api/orders", "/orders/form", "/api/orders/report"})
public class Servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private int count = 0; // Number of orders.
    private int averageOrderAmount = 0; // Average price.
    private int turnoverWithoutVAT = 0; // Whole price.
    private int turnoverVAT = 0;
    private int turnoverWithVAT = 0;
    private int randomJamaGitiPushiJaoks = 0;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        OrderDao orderDao = new OrderDao();
        String requestURL = request.getRequestURI();

        if (requestURL.equals("/api/orders/report")) {
            System.out.println("doGet api orders report");
            List<Order> orders = orderDao.getOrderList();
            count = orders.size();
            int sum = 0;
            int quantity = 0;
            for (int i = 0; i<orders.size(); i++) {
                Order order = orders.get(i);
                List<OrderRow> orderRows = order.getOrderRows();
                for (OrderRow orderRow : orderRows) {
                    sum+= orderRow.getPrice() * orderRow.getQuantity();
                    quantity += orderRow.getQuantity();
                }
            }
            averageOrderAmount = sum/count;
            turnoverWithoutVAT = sum;
            turnoverVAT = sum/5;
            turnoverWithVAT = turnoverWithoutVAT + turnoverVAT;
            String output = "{\n" +
                    "       \"count\": "+count+",\n" +
                    "       \"averageOrderAmount\": "+averageOrderAmount+",\n" +
                    "       \"turnoverWithoutVAT\": "+turnoverWithoutVAT+",\n" +
                    "       \"turnoverVAT\": "+turnoverVAT+",\n" +
                    "       \"turnoverWithVAT\": "+turnoverWithVAT+"\n" +
                    "   }";
            System.out.println(output);
            response.setHeader("Content-Type", "application/json");
            response.getWriter().print(output);

        } else {
            if (request.getParameter("id") != null) {
                ObjectMapper mapper = new ObjectMapper();
                Long data = Long.parseLong(request.getParameter("id"));
                Order order = orderDao.getOrder(data);
                response.setHeader("Content-Type", "application/json");
                response.getWriter().print(mapper.writeValueAsString(order));
            } else {
                ObjectMapper mapper = new ObjectMapper();
                ArrayList<String> output = new ArrayList();

                List<Order> orderList = orderDao.getOrderList();

                for (int i = 0; i < orderList.size(); i++) {
                    output.add(mapper.writeValueAsString(orderList.get(i)));
                }
                response.setHeader("Content-Type", "application/json");
                response.getWriter().print(output);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //System.out.println("doPost");
        String requestURL = request.getRequestURI();
        if (requestURL.equals("/api/orders")) {
            fromPostRequest(request,response);
        } else if (requestURL.equals("/orders/form")){
            fromFormInput(request,response);
        }
    }

    private void fromFormInput(HttpServletRequest request, HttpServletResponse response) throws IOException {
        OrderDao orderDao = new OrderDao();
        String orderNumber = request.getParameter("orderNumber");
        if (orderNumber.length() < 2) {
            String output = "{\n" +
                    "      \"errors\": [\n" +
                    "          {\n" +
                    "             \"code\": \"too_short_number\"\n" +
                    "          }\n" +
                    "      ]\n" +
                    "   }";
            response.setStatus(400);
            response.getWriter().print(output);
        } else {
            Order order = new Order();
            order.setOrderNumber(orderNumber);
            orderDao.insertOrder(order);
            response.setHeader("Content-Type", "application/json");
            response.getWriter().print(order.getId());
        }
    }

    private void fromPostRequest(HttpServletRequest request, HttpServletResponse response) {
        //System.out.println("fromPostRequest");
        OrderDao orderDao = new OrderDao();
        ObjectMapper mapper = new ObjectMapper();
        String data;
        Order order;
        try {
            data = Util.asString(request.getInputStream());
            order = new ObjectMapper().readValue(data, Order.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (order.getOrderNumber().length() < 2) {
            String output = "{\n" +
                    "      \"errors\": [\n" +
                    "          {\n" +
                    "             \"code\": \"too_short_number\"\n" +
                    "          }\n" +
                    "      ]\n" +
                    "   }";
            response.setHeader("Content-Type", "application/json");
            response.setStatus(400);
            try {
                response.getWriter().print(output);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Order order1 = orderDao.insertOrder(order);
            response.setHeader("Content-Type", "application/json");
            try {
                response.getWriter().print(mapper.writeValueAsString(order1));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void doDelete( HttpServletRequest request, HttpServletResponse response) {
        OrderDao orderDao = new OrderDao();
        if (request.getParameterMap().containsKey("id")) {
            Long id = Long.parseLong(request.getParameter("id"));
            orderDao.deleteOrder(id);
        } else {
            orderDao.deleteAll();
        }
    }
}
