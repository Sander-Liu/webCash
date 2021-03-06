package common;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum OrderStatus {
    PAYiING(1, "待支付"), OK(2, "支付完成");
    private int flg;
    private String desc;

    OrderStatus(int flg, String desc) {
        this.flg = flg;
        this.desc = desc;
    }

    //浏览订单时,拿到的是一个数字,相当于查找的是状态
    public static OrderStatus valueOf(int flg) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.flg == flg) {
                return orderStatus;
            }
        }
        throw new RuntimeException("orderStatus is not exit");
    }
}
