package model;

import java.time.LocalDate;

public class MedicineAdmin {
    private int id;
    private String name;
    private LocalDate manuDate;
    private LocalDate expDate;
    private int quantity;
    private float price;

    public MedicineAdmin(int id, String name, LocalDate manuDate, LocalDate expDate, int quantity, float price) {
        this.id = id;
        this.name = name;
        this.manuDate = manuDate;
        this.expDate = expDate;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public LocalDate getManuDate() { return manuDate; }
    public LocalDate getExpDate() { return expDate; }
    public int getQuantity() { return quantity; }
    public float getPrice() { return price; }

    // Setters (nếu cần)
    public void setPrice(float price) { this.price = price; }
}