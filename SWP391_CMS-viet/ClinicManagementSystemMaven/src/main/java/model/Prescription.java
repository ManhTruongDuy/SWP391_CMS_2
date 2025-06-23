package model;

import java.sql.Date;

public class Prescription {
    private int id;
    private  MedicineRecord medicineRecord;
    private Doctor doctor;
    private Date prescriptionDate;
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public MedicineRecord getMedicineRecord() {
        return medicineRecord;
    }

    public void setMedicineRecord(MedicineRecord medicineRecord) {
        this.medicineRecord = medicineRecord;
    }

    public Date getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(Date prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
