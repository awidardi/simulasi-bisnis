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
@Table(name="aw_order_raw")
public class OrderRaw {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String note;
    private int quantities;
    private int totalPrices;


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
    @OneToMany(mappedBy = "orderRaw", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<OrderRawDetail> orderRawDetails = new ArrayList<>();
    //relationship ends


    public OrderRaw() {
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
        for (OrderRawDetail od: this.orderRawDetails) {
            tmp += od.getDiscountedPrice() * this.quantities;
            int promo = od.getRawMaterial().getPromo();
            System.out.println("promo + " + promo);
            tmp = tmp - tmp*promo/100;
            System.out.println("tmp + " + tmp);
        }
        totalPrices = tmp;
    }

    public int getNoDiscountPrice() {
        int noDiscount = 0;
        for (OrderRawDetail od: this.orderRawDetails) {
            noDiscount += od.getRawMaterial().getPrice() * this.quantities;
        }
        return noDiscount;
    }

    public int getDiscountAmount() {
        return getTotalPrices() - getNoDiscountPrice();
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

    public Businessman getBusinessman(){
        return businessman;
    }

    public void setBusinessman(Businessman businessman){
        this.businessman = businessman;
    }

    public Juragan getJuragan(){
        return juragan;
    }

    public void setJuragan(Juragan juragan){
        this.juragan = juragan;
    }

    public List<OrderRawDetail> getOrderRawDetails() {
        return orderRawDetails;
    }

    public void setOrderRawDetails(List<OrderRawDetail> orderRawDetails) {
        this.orderRawDetails = orderRawDetails;
    }

    public void addOrderRawDetail(OrderRawDetail orderRawDetail) {
        this.orderRawDetails.add(orderRawDetail);
    }

    public void addOrderRawDetail(List<OrderRawDetail> orderRawDetail) {
        this.orderRawDetails.addAll(orderRawDetail);
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getPlainTextStatus() {
        String plainText = "";
        if(isCart()) {
            plainText = "Menunggu pembayaran DP dari pembeli.";
        } else if (isPending()) {
            plainText = "Menunggu konfirmasi penerimaan pesanan dari juragan.";
        } else if (isWaiting()) {
            plainText = "Menunggu pesanan diantarkan oleh juragan.";
        } else if (isComplete()) {
            plainText = "Pesanan selesai";
        }
        return plainText;
    }
}
