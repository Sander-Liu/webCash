package servlet;

import entity.Goods;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/updateGoods")
public class UpdateGoods extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String id = req.getParameter("goodsID");
        int goodId = Integer.valueOf(id);
        String name = req.getParameter("name");
        String introduce = req.getParameter("introduce");
        String stock = req.getParameter("stock");
        String unit = req.getParameter("unit");
        String price = req.getParameter("price");
        double doublePrice = Double.valueOf(price);
        int realPrice = new Double(doublePrice * 100).intValue();
        String discount = req.getParameter("discount");
        //查看当前的goodsId是否存在
        Goods goods = this.getGoods(goodId);
        if (goods == null) {
            System.out.println("没有该商品");
            resp.sendRedirect("index.html");
        } else {
            goods.setName(name);
            goods.setIntroduce(introduce);
            goods.setStock(Integer.valueOf(stock));
            goods.setUnit(unit);
            goods.setPrice(Integer.valueOf(realPrice));
            goods.setDiscount(Integer.valueOf(discount));
            boolean effect = this.modifyGoods(goods);
            if (effect) {
                resp.sendRedirect("goodsbrowse.html");
            } else {
                resp.sendRedirect("index.html");
            }
        }


    }

    private Goods getGoods(int goodsId) {
        Goods goods = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String sql = "select * from goods where id=?";
            connection = DBUtil.getConnection(true);
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, goodsId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                goods = this.extractGoods(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, preparedStatement, resultSet);
        }
        return goods;
    }

    private Goods extractGoods(ResultSet resultSet) throws SQLException {
        Goods goods = new Goods();
        goods.setId(resultSet.getInt("id"));
        goods.setName(resultSet.getString("name"));
        goods.setIntroduce(resultSet.getString("introduce"));
        goods.setStock(resultSet.getInt("stock"));
        goods.setUnit(resultSet.getString("unit"));
        goods.setPrice(resultSet.getInt("price"));
        goods.setDiscount(resultSet.getInt("discount"));
        return goods;
    }

    private boolean modifyGoods(Goods goods) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean effect = false;
        try {
            String sql = "update goods set name=?,introduce=?,stock=?,unit=?,price=?,discount=?" +
                    " where id=?";
            connection = DBUtil.getConnection(true);
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, goods.getName());
            preparedStatement.setString(2, goods.getIntroduce());
            preparedStatement.setInt(3, goods.getStock());
            preparedStatement.setString(4, goods.getUnit());
            preparedStatement.setInt(5, goods.getPriceInt());
            preparedStatement.setInt(6, goods.getDiscount());
            preparedStatement.setInt(7, goods.getId());
            effect = (preparedStatement.executeUpdate() == 1);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, preparedStatement, null);
        }
        return effect;
    }
}
