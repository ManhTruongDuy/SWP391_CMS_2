package model.accounts;

public class SysAdminAccount {
    private int admin_id;
    private String fullName;
    private String phone;
    private String email;
    private String department;
    private String username;
    private String password;
    private String status;

    public SysAdminAccount() {
    }

    public SysAdminAccount(int admin_id, String fullName, String phone, String email,String department, String username, String password, String status) {
        this.admin_id = admin_id;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.department = department;
        this.username = username;
        this.password = password;
        this.status = status;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getStatus() {
        return status;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
