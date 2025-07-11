/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.time.LocalDate;
/**
 *
 * @author admin
 */
public class Medicine {
    public int medicine_id;
    public String name;
    public int unit_id;
    public int category_id;
    public String ingredient;
    public String usage;
    public String preservation;
    public LocalDate manuDate;
    public LocalDate expDate;
    public int quantity;
    public float price;
    public int warehouse_id;

    public Medicine() {
    }

    public Medicine(int medicine_id, String name, int unit_id, int category_id, String ingredient, String usage, String preservation, LocalDate manuDate, LocalDate expDate, int quantity, float price, int warehouse_id) {
        this.medicine_id = medicine_id;
        this.name = name;
        this.unit_id = unit_id;
        this.category_id = category_id;
        this.ingredient = ingredient;
        this.usage = usage;
        this.preservation = preservation;
        this.manuDate = manuDate;
        this.expDate = expDate;
        this.quantity = quantity;
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

    public int getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(int unit_id) {
        this.unit_id = unit_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getPreservation() {
        return preservation;
    }

    public void setPreservation(String preservation) {
        this.preservation = preservation;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public boolean dateChecker(LocalDate manufacture_date, LocalDate expiration_date, LocalDate import_date){
        if (manufacture_date.isAfter(expiration_date)){
            System.out.println("Invalid date");
            return false;
        }
        if (expiration_date.isAfter(LocalDate.now())){
            System.out.println("Medicine has expired");
            return false;
        }
        if (manufacture_date.isAfter(import_date) || expiration_date.isBefore(import_date)){
            System.out.println("Invalid import date");
            return false;
        }
        return true;
    } 
}
