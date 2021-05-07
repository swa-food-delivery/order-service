package microfood.orders.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;

@Entity
@Table(name = "ORDER_ITEMS")
@Data
public class OrderItem {
    @Id
    @Column(name = "order_item_id")
    @GeneratedValue
    private UUID orderItemId;

    @Column(name = "item_id", columnDefinition = "uuid")
    @Type(type = "pg-uuid")
    private UUID itemId;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_price")
    private Double itemPrice;

    @Column(name = "item_quantity")
    private Integer itemQuantity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private Order order;
}
