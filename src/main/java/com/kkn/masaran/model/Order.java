package com.kkn.masaran.model;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ARDI on 11/2/2016.
 */
@Entity
@Table(name="aw_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String note;
    private int quantities;
    private int totalPrices;
    private int discountAmount;
    private long hargaNego = 0;
    private int banyakNego = 3;
    private boolean canNego = true;


    /**
     * Order Status Explanation
     * 0 = customer still shopping
     * 1 = customer checkout and pay dp, waiting for confirmation from catering
     * 2 = catering say OK and starting to prepare order
     * 3 = order shipped and completed
     */
    private int status;
    public static int ORDER_STATUS_CART = 0;
    public static int ORDER_STATUS_PENDING = 1;
    public static int ORDER_STATUS_WAITING = 2;
    public static int ORDER_STATUS_COMPLETE = 3;

    //relationship begins
    @ManyToOne
    private Juragan juragan;
    @ManyToOne
    private Businessman businessman;
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();
    //relationship ends


    public Order() {
        this.status = ORDER_STATUS_CART;
    }

    public long getId() {
        return id;
    }

    public int getQuantities() {
        return quantities;
    }

    public void setQuantities(int quantities) {
        this.quantities = quantities;
    }

    public int getTotalPrices() {
        return totalPrices;
    }

    public void updateTotalPrices() {
        int tmp = 0;
        for (OrderDetail od: this.orderDetails) {
            tmp += od.getDiscountedPrice() * this.quantities;
            System.out.println("tmp " + tmp);
        }
        totalPrices = tmp;
    }

    public int getNoDiscountPrice() {
        int noDiscount = 0;
        for (OrderDetail od: this.orderDetails) {
            noDiscount += od.getProduct().getPrice() * this.quantities;
        }
        return noDiscount;
    }

    public int getDiscountAmount() {
        return this.discountAmount;
    }

    public void setDiscountAmount() {
        this.discountAmount = getTotalPrices() - getNoDiscountPrice();
    }

    public void setTotalPrices(int totalPrices) {
        this.totalPrices = totalPrices;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public void addOrderDetail(OrderDetail orderDetail) {
        this.orderDetails.add(orderDetail);
    }

    public void addOrderDetail(List<OrderDetail> orderDetail) {
        this.orderDetails.addAll(orderDetail);
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean waitJuragan()
    {
        if(this.getJuragan()==null) return true;
        else return false;
    }

    public boolean isCart() {
        return this.status == ORDER_STATUS_CART;
    }

    public boolean isPending() {
        return this.status == ORDER_STATUS_PENDING;
    }

    public boolean isWaiting() {
        return this.status == ORDER_STATUS_WAITING;
    }

    public boolean isComplete() {
        return this.status == ORDER_STATUS_COMPLETE;
    }

    public boolean isNotComplete() {
        return this.status != ORDER_STATUS_COMPLETE;
    }

    public Juragan getJuragan() {
        return juragan;
    }

    public void setJuragan(Juragan juragan) {
        this.juragan = juragan;
    }

    public Businessman getBusinessman() {
        return businessman;
    }

    public void setBusinessman(Businessman businessman) {
        this.businessman = businessman;
    }

    public String getPlainTextStatus() {
        String plainText = "";
        if(isCart()) {
            plainText = "Menunggu pembayaran DP dari pembeli.";
        } else if (isPending()) {
            plainText = "Menunggu UMKM konfirmasi harga";
        } else if (isWaiting()) {
            plainText = "Menunggu Admin konfirmasi harga";
        } else if (isComplete()) {
            plainText = "Pesanan selesai";
        }
        return plainText;
    }



    public long getHargaNego() {
        return hargaNego;
    }

    public void setHargaNego(long hargaNego) {
        this.hargaNego = hargaNego;
    }

    public int getBanyakNego() {
        return banyakNego;
    }

    public void setBanyakNego(int banyakNego) {
        this.banyakNego = banyakNego;
    }

    public boolean isCanNego() {
        return canNego;
    }

    public void setCanNego(boolean canNego) {
        this.canNego = canNego;
    }
}
