package model.accounts;

public class DoctorAccount {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String department;
    private String username;
    private String password;
    private String status;

    public DoctorAccount() {
    }

    public DoctorAccount(int id, String name, String phone, String email, String department, String username, String password, String status) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.department = department;
        this.username = username;
        this.password = password;
        this.status = status;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
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
    public String getStatus() {
        return status;
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
