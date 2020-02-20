package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.OrderStatus;
import entity.Account;
import entity.Order;
import entity.OrderItem;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/browseOrder")
public class BrowseOrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=utf8");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession();
        Account account = (Account) session.getAttribute("user");

        List<Order> orderList = query(account.getId());
        if (orderList == null) {
            System.out.println("没有订单");
        } else {
            ObjectMapper mapper = new ObjectMapper();
            PrintWriter printWriter = resp.getWriter();
            mapper.writeValue(printWriter, orderList);
            Writer writer = resp.getWriter();
            writer.write(printWriter.toString());

        }

    }

    private List<Order> query(int id) {
        List<Order> orderList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String sql = getSQL("@query_order_by_account");
            connection = DBUtil.getConnection(true);
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            Order order = null;
            while (resultSet.next()) {
                if (order == null) {
                    order = parseOrder(resultSet);
                    orderList.add(order);
                }
                if (!resultSet.getString("order_id").equals(order.getId())) {
                    order = parseOrder(resultSet);
                    orderList.add(order);
                }
                //订单
//                Order order = parseOrder(resultSet);
//                orderList.add(order);
                //订单项
                OrderItem orderItem = parseOrderItem(resultSet);
                order.orderItemList.add(orderItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, preparedStatement, resultSet);
        }

        return orderList;
    }

    private OrderItem parseOrderItem(ResultSet resultSet) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(resultSet.getInt("item_id"));
        orderItem.setOrderId(resultSet.getString("order_id"));
        orderItem.setGoodsId(resultSet.getInt("goods_id"));
        orderItem.setGoodsName(resultSet.getString("goods_name"));
        orderItem.setGoodsIntroduce(resultSet.getString("goods_introduce"));
        orderItem.setGoodsNum(resultSet.getInt("goods_num"));
        orderItem.setGoodsUnit(resultSet.getString("goods_unit"));
        orderItem.setGoodsPrice(resultSet.getInt("goods_price"));
        orderItem.setGoodsDiscount(resultSet.getInt("goods_discount"));
        return orderItem;
    }

    private Order parseOrder(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setId(resultSet.getString("order_id"));
        order.setAccount_id(resultSet.getInt("account_id"));
        order.setAccount_name(resultSet.getString("account_name"));
        order.setCreate_time(resultSet.getString("create_time"));
        order.setFinish_time(resultSet.getString("finish_time"));
        order.setActual_amount(resultSet.getInt("actual_amount"));
        order.setTotal_money(resultSet.getInt("total_money"));
        order.setOrderstatus(OrderStatus.valueOf(resultSet.getInt("order_status")));
        return order;
    }

    private String getSQL(String SQLName) {
        //获取文件资源
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("script/" + SQLName.substring(1) + ".sql");
        if (in == null) {
            throw new RuntimeException("加载sql文件出错");
        } else {
            //字节流转为字符流
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            try {
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(" ").append(line);
                }
                System.out.println("sb:" + stringBuilder);
                return stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("加载sql语句出错");
            }
        }
    }
}
