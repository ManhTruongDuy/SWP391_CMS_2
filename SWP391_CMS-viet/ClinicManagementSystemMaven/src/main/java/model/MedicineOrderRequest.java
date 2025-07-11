package model;

import java.util.List;

public class MedicineOrderRequest {
    private String prescriptionId;
    private List<MedicineCounter> items;

    public String getPrescriptionId() { return prescriptionId; }
    public void setPrescriptionId(String prescriptionId) { this.prescriptionId = prescriptionId; }

    public List<MedicineCounter> getItems() { return items; }
    public void setItems(List<MedicineCounter> items) { this.items = items; }
}


