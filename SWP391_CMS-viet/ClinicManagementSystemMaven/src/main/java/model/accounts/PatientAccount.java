package model.accounts;

public class PatientAccount {
    private int patient_id;
    private String full_name;
    private String gender;
    private String phone;

    public PatientAccount() {
    }

    public PatientAccount(int patient_id, String full_name, String gender, String phone) {
        this.patient_id = patient_id;
        this.full_name = full_name;
        this.gender = gender;
        this.phone = phone;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
