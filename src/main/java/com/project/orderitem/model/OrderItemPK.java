package com.project.orderitem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemPK implements Serializable {
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "item_id")
    private Long itemId;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) return false;
        OrderItemPK orderItemPK = (OrderItemPK) obj;
        return orderId != null && Objects.equals(orderId, orderItemPK.orderId)
                && itemId != null && Objects.equals(itemId, orderItemPK.itemId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}