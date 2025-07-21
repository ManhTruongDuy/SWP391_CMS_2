package model;

import java.io.Serializable;

public class MedicineStatistic implements Serializable {
    private String medicineName;
    private int totalQuantity;
    private int totalPrescriptions;

    public MedicineStatistic() {
    }

    public MedicineStatistic(String medicineName, int totalQuantity, int totalPrescriptions) {
        this.medicineName = medicineName;
        this.totalQuantity = totalQuantity;
        this.totalPrescriptions = totalPrescriptions;
    }

    public String getMedicineName() {
        return medicineName;
    }
    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }
    public int getTotalQuantity() {
        return totalQuantity;
    }
    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
    public int getTotalPrescriptions() {
        return totalPrescriptions;
    }
    public void setTotalPrescriptions(int totalPrescriptions) {
        this.totalPrescriptions = totalPrescriptions;
    }
} 