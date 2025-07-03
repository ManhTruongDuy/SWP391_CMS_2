/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.time.LocalDate;
/**
 *
 * @author admin duy manh
 */
public class Pharmacist {
    private int ID;
    private String name;
    private int account_pharmacist_id;
    private String mobile;

    public Pharmacist() {
    }

    public Pharmacist(int ID, String name, int account_pharmacist_id, String mobile) {
        this.ID = ID;
        this.name = name;
        this.account_pharmacist_id = account_pharmacist_id;
        this.mobile = mobile;
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

    public int getAccount_pharmacist_id() {
        return account_pharmacist_id;
    }

    public void setAccount_pharmacist_id(int account_pharmacist_id) {
        this.account_pharmacist_id = account_pharmacist_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    
    
    //validate
    
    public boolean emailChecker(String email){
        //check rong
        if (email == null) {
            return false;
        }
        //check format email
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
    
    public boolean mobileChecker(String mobile){
        //check sdt rong
        if (mobile==null) {
            System.out.println("Mobile phone number cannot be blank");
            return false;
        }
        //check sdt hop le
        if (mobile.matches("^\\(\\d{3}\\)-\\d{3}-\\d{4}$")) {
            System.out.println("Invalid mobile number");
            return false;
        }
        return true;
    }
}
