package model.accounts;

public class PharmacistAccount {
    private int ID;
    private String name;
    private String mobile;
    private String email;
    private String username;
    private String password;

    public PharmacistAccount() {
    }

    public PharmacistAccount(int ID, String name, String mobile, String email, String username, String password) {
        this.ID = ID;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

}
