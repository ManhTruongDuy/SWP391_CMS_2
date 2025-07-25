package model.accounts;

public class AccountPharmacist {
    private  String username;
    private String password;
    private String email;
    private String img;
    private String status;

    public AccountPharmacist() {
    }

    public AccountPharmacist(String username, String password, String email, String img, String status) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.img = img;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
