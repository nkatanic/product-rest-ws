package com.example.productrestws.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true, length = 10)
    @NotBlank(message = "Code is mandatory field")
    @Size(message = "Code size must be equal to 10", min = 10, max = 10)
    private String code;

    @NotBlank(message = "Name is mandatory field")
    private String name;

    @Column(name = "price_hrk")
    @NotNull(message = "priceHrk cannot be null")
    @PositiveOrZero(message = "priceHrk must be >= 0")
    private float priceHrk;

    @Column(name = "price_eur")
    @NotNull(message = "priceHrk cannot be null")
    @PositiveOrZero(message = "priceHrk must be >= 0")
    private float priceEur;

    @NotBlank(message = "Description is mandatory field")
    private String description;

    @Column(name = "is_available")
    @NotNull(message = "IsAvailable cannot be null")
    private boolean isAvailable;

    public Product(){
        
    }

    public Product(String code, String name, float priceHrk, String description, boolean isAvailable) {
        this.code = code;
        this.name = name;
        this.priceHrk = priceHrk;        
        this.description = description;
        this.isAvailable = isAvailable;

        // Calculated and set by the controller after calling the HNB API
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
