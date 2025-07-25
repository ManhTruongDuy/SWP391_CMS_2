package model;

public class AccountPatient {
    private int account_patient_id;
    private String username;
    private String password;
    private String email;
    private String status;

    public AccountPatient() {
    }

    public AccountPatient(int account_patient_id, String username, String password, String email, String status) {
        this.account_patient_id = account_patient_id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.status = status;
    }

    public int getAccount_patient_id() {
        return account_patient_id;
    }

    public void setAccount_patient_id(int account_patient_id) {
        this.account_patient_id = account_patient_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

