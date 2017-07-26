package com.kkn.masaran.model;


import org.springframework.data.util.Pair;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private String name;
    private String description;
    private String photo;
    private boolean isSold = false;
    private long stock;

    // Format
    // min quantity - price | min quantity 2 - price 2 | ....
    // 1-1000|10-950|30-920|100-900
    private String priceData;

    //relationship begins
    @ManyToOne
    private Businessman businessman;
    //relationship ends

    public Product(){
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId(){
        return id;
    }

    public Businessman getBusinessman() {
        return businessman;
    }

    public void setBusinessman(Businessman businessman) {
        this.businessman = businessman;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public ArrayList<Pair<Integer, Integer>> getPricePair() {
        ArrayList<Pair<Integer, Integer>> pricePair = new ArrayList<>();
        for (String stringPair: this.priceData.split("\\|")) {
            int minQuantity = Integer.parseInt(stringPair.split("\\-")[0]);
            int price = Integer.parseInt(stringPair.split("\\-")[1]);
            pricePair.add(Pair.of(minQuantity, price));
        }
        return pricePair;
    }

    // Default if we get a price without specify the quantity,
    // then assume the quantity is 1 or get it's highest price
    public int getPrice() {
        return getPricePair().get(0).getSecond();
    }

    public int getPrice(int quantity) {
        int price = getPrice();
        System.out.println(price);
        for (Pair<Integer, Integer> pricePair: getPricePair()) {
            if (quantity < pricePair.getFirst()) {
                break;
            }
            System.out.println(pricePair.getFirst());
            System.out.println(pricePair.getSecond());
            price = pricePair.getSecond();
        }
        return price;
    }

    public void setPrice(int price) {
        this.priceData = "1-" + Integer.toString(price);
    }

    public void setPrice(String priceData) {
        this.priceData = priceData;
    }

    public boolean isSold() {
        return isSold;
    }

    public void setSold(boolean sold) {
        isSold = sold;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }
}
