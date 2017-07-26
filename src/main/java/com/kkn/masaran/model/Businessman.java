package com.kkn.masaran.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Set;

/**
 * Created by ARDI on 6/20/2017.
 */
// TODO CATERING


@Entity
public class Businessman extends User{
    private String businessName;
    private String address;

    @Column(columnDefinition="text")
    private String description;
    private String phoneNumber;
    private long balance = 1000000;

    //relationship begins - businessman jual
    @OneToMany(mappedBy = "businessman", fetch = FetchType.EAGER)
    private List<Product> products;
    @OneToMany(mappedBy = "businessman", fetch = FetchType.EAGER)
    private List<Order> orders;
    //relationship ends

    @OneToMany(mappedBy = "businessman", fetch = FetchType.EAGER)
    private List<OrderRaw> orderRaws;

    public Businessman(){
        super();
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<OrderRaw> getOrderRaws() {
        return orderRaws;
    }

    public void setOrderRaws(List<OrderRaw> orderRaws) {
        this.orderRaws = orderRaws;
    }

    public boolean hasProduct(Product products){
        for(Product containedProduct: getProducts()){
            if(containedProduct.getId() == products.getId()){
                return true;
            }
        }
        return false;
    }

    public Set<String> getAvailableCategory(){
        Set<String> categories = null;
        for(Product product : products){

        }
        return categories;
    }
}
