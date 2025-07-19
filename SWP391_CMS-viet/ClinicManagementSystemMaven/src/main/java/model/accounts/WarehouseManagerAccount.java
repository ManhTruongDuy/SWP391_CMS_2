package model.accounts;

public class WarehouseManagerAccount {
    private int Warehouse_manager_id;
    private String full_name;
    private String phone;
    private String email;
    private String username;
    private String password;
    private String status;

    public WarehouseManagerAccount(int warehouse_manager_id) {
        Warehouse_manager_id = warehouse_manager_id;
    }

    public WarehouseManagerAccount(int warehouse_manager_id, String full_name, String phone, String email, String username, String password, String status) {
        Warehouse_manager_id = warehouse_manager_id;
        this.full_name = full_name;
        this.phone = phone;
        this.email = email;
        this.username = username;
        this.password = password;
        this.status = status;
    }

    public int getWarehouse_manager_id() {
        return Warehouse_manager_id;
    }

    public void setWarehouse_manager_id(int warehouse_manager_id) {
        Warehouse_manager_id = warehouse_manager_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
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
}
