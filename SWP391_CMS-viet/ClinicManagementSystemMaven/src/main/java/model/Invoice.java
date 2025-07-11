package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Invoice {
    private int invoiceId;
    private int patientId;
    private int medicineRecordId;
    private LocalDateTime issueDate;
    private BigDecimal totalAmount;
    private String status;

    // Constructor đầy đủ
    public Invoice(int invoiceId, int patientId, int medicineRecordId, LocalDateTime issueDate, BigDecimal totalAmount, String status) {
        this.invoiceId = invoiceId;
        this.patientId = patientId;
        this.medicineRecordId = medicineRecordId;
        this.issueDate = issueDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }
//123
    // Constructor không có ID (cho trường hợp tạo mới, ID sẽ auto tăng)
    public Invoice(int patientId, int medicineRecordId, LocalDateTime issueDate, BigDecimal totalAmount, String status) {
        this.patientId = patientId;
        this.medicineRecordId = medicineRecordId;
        this.issueDate = issueDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // Getter & Setter
    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getMedicineRecordId() {
        return medicineRecordId;
    }

    public void setMedicineRecordId(int medicineRecordId) {
        this.medicineRecordId = medicineRecordId;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceId=" + invoiceId +
                ", patientId=" + patientId +
                ", medicineRecordId=" + medicineRecordId +
                ", issueDate=" + issueDate +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                '}';
    }
}