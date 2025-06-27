package model;

import java.time.LocalDate;

public class MedicineWarehouse {
    public int medicine_id;
    public String name;
    public int quantity;
    public LocalDate manuDate;
    public LocalDate expDate;
    public float price;
    public int warehouse_id;

    public MedicineWarehouse() {
    }

    public MedicineWarehouse(int medicine_id, String name, int quantity, LocalDate manuDate, LocalDate expDate, float price, int warehouse_id) {
        this.medicine_id = medicine_id;
        this.name = name;
        this.quantity = quantity;
        this.manuDate = manuDate;
        this.expDate = expDate;
        this.price = price;
        this.warehouse_id = warehouse_id;
    }

    public int getMedicine_id() {
        return medicine_id;
    }

    public void setMedicine_id(int medicine_id) {
        this.medicine_id = medicine_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getManuDate() {
        return manuDate;
    }

    public void setManuDate(LocalDate manuDate) {
        this.manuDate = manuDate;
    }

    public LocalDate getExpDate() {
        return expDate;
    }

    public void setExpDate(LocalDate expDate) {
        this.expDate = expDate;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getWarehouse_id() {
        return warehouse_id;
    }

    public void setWarehouse_id(int warehouse_id) {
        this.warehouse_id = warehouse_id;
    }
}
