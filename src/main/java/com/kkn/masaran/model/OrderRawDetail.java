package com.kkn.masaran.model;

import javax.persistence.*;


@Entity
public class OrderRawDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private OrderRaw orderRaw;
    @ManyToOne(fetch = FetchType.EAGER)
    private RawMaterial rawMaterial;

    private long hargaNego;

    public OrderRawDetail() {
    }

    public OrderRawDetail(OrderRaw o, RawMaterial p) {
        this.rawMaterial = p;
        setOrder(o);
    }

    public long getId() {
        return id;
    }

    public OrderRaw getOrderRaw() {
        return orderRaw;
    }

    public void setOrder(OrderRaw orderRaw) {
        this.orderRaw = orderRaw;
        orderRaw.addOrderRawDetail(this);
    }

    public RawMaterial getRawMaterial() {
        return rawMaterial;
    }

    public void setRawMaterial(RawMaterial rawMaterial) {
        this.rawMaterial = rawMaterial;
    }

    public int getPrice() {
        return rawMaterial.getPrice() * orderRaw.getQuantities();
    }

    public int getDiscountedPrice() {
        return this.rawMaterial.getPrice(orderRaw.getQuantities());
    }

    public long getHargaNego() {
        return hargaNego;
    }

    public void setHargaNego(long hargaNego) {
        this.hargaNego = hargaNego;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "id=" + id +
                ", order=" + orderRaw.getId() +
                ", raw-material=" + rawMaterial +
                '}';
    }
}
