package model;

public class PrescriptionItem {
    private int idPrescriptionItem;
    private int prescriptionId;
    private int medicineId;
    private float quantity;

    public PrescriptionItem() {
    }

    public PrescriptionItem(int idPrescriptionItem, int prescriptionId, int medicineId, float quantity) {
        this.idPrescriptionItem = idPrescriptionItem;
        this.prescriptionId = prescriptionId;
        this.medicineId = medicineId;
        this.quantity = quantity;
    }

    public int getIdPrescriptionItem() {
        return idPrescriptionItem;
    }

    public void setIdPrescriptionItem(int idPrescriptionItem) {
        this.idPrescriptionItem = idPrescriptionItem;
    }

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }
}

