package servlet;

public class OrderRow {
    private String itemName;
    private Integer quantity;
    private Integer price;

    public OrderRow() {}

    public OrderRow(String itemName, Integer quantity, Integer price) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "{" +
                "itemName:'" + itemName + '\'' +
                ", quantity:" + quantity +
                ", price:" + price +
                '}';
    }
}
