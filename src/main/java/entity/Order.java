package entity;

import common.OrderStatus;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Order {
    private String id;
    private Integer account_id;
    private String account_name;
    private String create_time;
    private String finish_time;
    private Integer actual_amount;
    private Integer total_money;
    private OrderStatus orderstatus;
    //订单项的内容也需要存储到当前订单内
    public List<OrderItem> orderItemList = new ArrayList<>();

    public int getActual_amountInt() {
        return actual_amount;
    }

    public double getActual_amount() {
        return actual_amount * 1.0 / 100;
    }

    public int getTotal_moneyInt() {
        return total_money;
    }

    public double getTotal_money() {
        return total_money * 1.0 / 100;
    }

    public double getDiscount() {
        return (this.getTotal_money() - this.getActual_amount());
    }
}
