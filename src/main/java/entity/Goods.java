package entity;

import lombok.Data;

import java.sql.PreparedStatement;

@Data
public class Goods {
    private Integer id;
    private String name;
    private String introduce;
    private Integer stock;
    private String unit;
    private Integer price;
    private Integer discount;
    private Integer buyGoodsNum;

    public int getPriceInt() {
        return price;
    }

    public double getPrice() {
        return price * 1.0 / 100;
    }
}
