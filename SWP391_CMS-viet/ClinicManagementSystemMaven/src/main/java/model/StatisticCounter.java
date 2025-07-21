package model;

import java.util.List;

public class StatisticCounter {
    private  int total_invoice;
    private  Double total_amount;
    private int total_patient;
    List<Invoice> invoices;

    public int getTotal_invoice() {
        return total_invoice;
    }

    public void setTotal_invoice(int total_invoice) {
        this.total_invoice = total_invoice;
    }

    public int getTotal_patient() {
        return total_patient;
    }

    public void setTotal_patient(int total_patient) {
        this.total_patient = total_patient;
    }

    public Double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(Double total_amount) {
        this.total_amount = total_amount;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    public StatisticCounter(int total_invoice, Double total_amount, int total_patient,List<Invoice> invoices) {
        this.total_invoice = total_invoice;
        this.total_amount = total_amount;
        this.total_patient = total_patient;
        this.invoices = invoices;
    }
}
