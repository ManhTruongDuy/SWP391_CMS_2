package model.accounts;

public class SystemAdminFullInsertAccount {
    private int admin_id;
    private String full_name;
    private String department;
    private String phone;
    private String username;
    private String password;
    private String email;
    private String img;
    private String status;

    public SystemAdminFullInsertAccount() {
    }

    public SystemAdminFullInsertAccount(int admin_id, String full_name, String department, String phone, String username, String password, String email, String img, String status) {
        this.admin_id = admin_id;
        this.full_name = full_name;
        this.department = department;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.email = email;
        this.img = img;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
