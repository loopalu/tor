package servlet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private String orderNumber;
    private List<OrderRow> orderRows;

    @Override
    public String toString() {
        return "{" +
                "id:" + this.id +
                ", orderNumber:'" + this.orderNumber + '\'' +
                ", orderRows:" + this.orderRows +
                '}';
    }
}
