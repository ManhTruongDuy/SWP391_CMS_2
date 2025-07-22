package model;

import java.sql.Date;

public class MedicineRes {
    private int id;
    private String name;
    private Double price;
    private Date manuDate;
    private Date expDate;
    private int quantity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getManuDate() {
        return manuDate;
    }

    public void setManuDate(Date manuDate) {
        this.manuDate = manuDate;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public MedicineRes(int id, String name,Double price,Date manuDate, Date expDate, int quantity) {
        this.id = id;
        this.name = name;
        this.manuDate = manuDate;
        this.price = price;
        this.expDate = expDate;
        this.quantity = quantity;
    }
}
