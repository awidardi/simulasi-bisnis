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
// TODO CUSTOMER
@Entity
public class Juragan extends User{
    private String juraganName;
    @Column(columnDefinition="text")
    private String description;

    //relationship begins - juragan beli
    @OneToMany(mappedBy = "juragan", fetch = FetchType.EAGER)
    private List<Order> orders;
    //relationship ends

    @OneToMany(mappedBy = "juragan", fetch = FetchType.EAGER)
    private List<RawMaterial> rawMaterials;
    @OneToMany(mappedBy = "juragan", fetch = FetchType.EAGER)
    private List<OrderRaw> orderRaws;


    public String getJuraganName() {
        return juraganName;
    }

    public void setJuraganName(String juraganName) {
        this.juraganName = juraganName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<RawMaterial> getRawMaterials() {
        return rawMaterials;
    }

    public void setRawMaterials(List<RawMaterial> rawMaterials) {
        this.rawMaterials = rawMaterials;
    }

    public List<OrderRaw> getOrderRaws() {
        return orderRaws;
    }

    public void setOrderRaws(List<OrderRaw> orderRaws) {
        this.orderRaws = orderRaws;
    }

    public boolean hasRawMaterial(RawMaterial rawMaterials){
        for(RawMaterial containedRawMaterial: getRawMaterials()){
            if(containedRawMaterial.getId() == rawMaterials.getId()){
                return true;
            }
        }
        return false;
    }

    public Set<String> getAvailableCategory(){
        Set<String> categories = null;
        for(RawMaterial rawMaterial : rawMaterials){

        }
        return categories;
    }
}
