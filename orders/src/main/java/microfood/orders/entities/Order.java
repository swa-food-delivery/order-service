package microfood.orders.entities;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import lombok.Data;
import microfood.orders.OrderStatusEnum;

@Entity
@Table(name = "ORDERS")
@Data
public class Order {
    @Id
    @Column(name = "order_id")
    @GeneratedValue
    private UUID orderId;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum orderStatus;

    @Column(name = "username")
    private String username;

    @Column(name = "restaurant_id", columnDefinition = "uuid")
    @Type(type = "pg-uuid")
    private UUID restaurantId;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "notes")
    private String notes;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on")
    private Date createdOn;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "order",
            cascade = CascadeType.ALL
    )
    private List<OrderItem> orderItems;
}
