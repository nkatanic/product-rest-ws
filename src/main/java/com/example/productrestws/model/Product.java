package com.example.productrestws.model;

import javax.persistence.*;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true, length = 10)
    private String code;

    private String name;

    @Column(name = "price_hrk")
    private float priceHrk;

    @Column(name = "price_eur")
    private float priceEur;

    private String description;

    @Column(name = "is_available")
    private boolean isAvailable;

    public Product(){
        
    }

    public Product(String code, String name, float priceHrk, String description, boolean isAvailable) {
        this.code = code;
        this.name = name;
        this.priceHrk = priceHrk;        
        this.description = description;
        this.isAvailable = isAvailable;

        // Set by the controller after calling the HNB API
        this.priceEur = 0; 
    }
    
    public long getId() {
        return this.id;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPriceHrk() {
        return this.priceHrk;
    }

    public void setPriceHrk(float priceHrk) {
        this.priceHrk = priceHrk;
    }

    public float getPriceEur() {
        return this.priceEur;
    }

    public void setPriceEur(float priceEur) {
        this.priceEur = priceEur;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsAvailable() {
        return this.isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
