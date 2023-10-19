package com.project.orderitem.model;

import com.project.item.model.Item;
import com.project.order.model.Order;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Table(name = "order_item")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    @EmbeddedId
    private OrderItemPK orderItemPK;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    private Item item;

    private int amount;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) return false;
        OrderItem orderItem = (OrderItem) obj;
        return orderItemPK != null && Objects.equals(orderItemPK, orderItem.getOrderItemPK());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}