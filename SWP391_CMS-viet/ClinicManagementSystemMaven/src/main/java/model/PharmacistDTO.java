package model;

public class PharmacistDTO {
    private String action;
    private int pharmacistId;
    private String name;
    private String mobile;
    private int accountPharmacistId;
    private String username;
    private String email;
    private String password;
    private String status;
    private String img;

    // Getters và Setters
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public int getPharmacistId() { return pharmacistId; }
    public void setPharmacistId(int pharmacistId) { this.pharmacistId = pharmacistId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public int getAccountPharmacistId() { return accountPharmacistId; }
    public void setAccountPharmacistId(int accountPharmacistId) { this.accountPharmacistId = accountPharmacistId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getImg() { return img; }
    public void setImg(String img) { this.img = img; }
}