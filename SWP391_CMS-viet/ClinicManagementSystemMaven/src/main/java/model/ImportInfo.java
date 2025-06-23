package model;

import java.time.LocalDate;

public class ImportInfo {
    private int idImportInfo;
    private int medicineId;
    private LocalDate importDate;
    private float quantity;
    private float importPrice;
    private int distributorId;

    public ImportInfo() {
    }

    public ImportInfo(int idImportInfo, int medicineId, LocalDate importDate, float quantity, float importPrice, int distributorId) {
        this.idImportInfo = idImportInfo;
        this.medicineId = medicineId;
        this.importDate = importDate;
        this.quantity = quantity;
        this.importPrice = importPrice;
        this.distributorId = distributorId;
    }

    public int getIdImportInfo() {
        return idImportInfo;
    }

    public void setIdImportInfo(int idImportInfo) {
        this.idImportInfo = idImportInfo;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public LocalDate getImportDate() {
        return importDate;
    }

    public void setImportDate(LocalDate importDate) {
        this.importDate = importDate;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public float getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(float importPrice) {
        this.importPrice = importPrice;
    }

    public int getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(int distributorId) {
        this.distributorId = distributorId;
    }
}

