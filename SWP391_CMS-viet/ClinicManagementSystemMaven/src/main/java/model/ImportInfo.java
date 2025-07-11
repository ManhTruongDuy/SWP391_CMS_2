package model;

import java.time.LocalDate;

public class ImportInfo {
    private int ImportID;
    private int DistributorID;
    private int medicine_id;
    private LocalDate ImportDate;
    private float ImportAmount;

    public ImportInfo() {
    }

    public ImportInfo(int importID, int distributorID, int medicine_id, LocalDate importDate, float importAmount) {
        ImportID = importID;
        DistributorID = distributorID;
        this.medicine_id = medicine_id;
        ImportDate = importDate;
        ImportAmount = importAmount;
    }

    public int getImportID() {
        return ImportID;
    }

    public void setImportID(int importID) {
        ImportID = importID;
    }

    public int getDistributorID() {
        return DistributorID;
    }

    public void setDistributorID(int distributorID) {
        DistributorID = distributorID;
    }

    public int getMedicine_id() {
        return medicine_id;
    }

    public void setMedicine_id(int medicine_id) {
        this.medicine_id = medicine_id;
    }

    public LocalDate getImportDate() {
        return ImportDate;
    }

    public void setImportDate(LocalDate importDate) {
        ImportDate = importDate;
    }

    public float getImportAmount() {
        return ImportAmount;
    }

    public void setImportAmount(float importAmount) {
        ImportAmount = importAmount;
    }
}
