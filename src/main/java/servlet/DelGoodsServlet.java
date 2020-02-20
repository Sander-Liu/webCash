package servlet;

import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/delGoods")
public class DelGoodsServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String goodId = req.getParameter("id");
        int goodIdInt=Integer.valueOf(goodId);
        boolean effect=this.delGoods(goodIdInt);
        if(effect){
            System.out.println("商品下架成功");
        }else{
            System.out.println("商品下架失败");
        }
    }
    private boolean delGoods(int goodIdInt){

        Connection connection=null;
        PreparedStatement preparedStatement=null;
        try{
            String sql="delete from goods where id=?";
            connection=DBUtil.getConnection(true);
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1,goodIdInt);
            return preparedStatement.executeUpdate()==1;

        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            DBUtil.close(connection,preparedStatement,null);
        }
        return false;
    }
}
